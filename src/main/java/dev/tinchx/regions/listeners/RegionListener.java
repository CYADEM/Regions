package dev.tinchx.regions.listeners;

import dev.tinchx.regions.RegionsPlugin;
import dev.tinchx.regions.config.Config;
import dev.tinchx.regions.events.impl.PlayerRegionEnterEvent;
import dev.tinchx.regions.events.impl.PlayerRegionLeaveEvent;
import dev.tinchx.regions.region.Region;
import dev.tinchx.regions.region.RegionOption;
import dev.tinchx.regions.selection.Selection;
import dev.tinchx.regions.utilities.ColorText;
import dev.tinchx.regions.utilities.item.ItemMaker;
import lombok.Getter;
import org.apache.commons.lang.WordUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public final class RegionListener implements Listener {

    private Config settings = RegionsPlugin.getInstance().getSettings(), lang = RegionsPlugin.getInstance().getLang();

    @Getter
    private static ItemStack selectionWand = new ItemMaker(Material.GOLD_HOE).setDisplayName("&5&lSelection Wand").addLore("", "&7Select two positions", "&7And finally use '/region create'", "").create();

    //CALLING REGION EVENTS

    @EventHandler(priority = EventPriority.MONITOR)
    final void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }
        Player player = event.getPlayer();
        Region region = Region.getByLocation(event.getTo()), pastRegion = Region.getByLocation(event.getFrom());

        if (pastRegion != null && pastRegion != region) {
            if (!new PlayerRegionLeaveEvent(player, pastRegion).call()) {
                event.setTo(from);
                return;
            }
        }

        if (region != null && region != pastRegion) {
            if (!new PlayerRegionEnterEvent(player, region).call()) {
                event.setTo(from);
            }
        }
    }

    //REGION EVENTS
    @EventHandler(priority = EventPriority.MONITOR)
    final void onPlayerRegionEnter(PlayerRegionEnterEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        if (settings.getBoolean("REGION.ENTER-MESSAGE")) {
            player.sendMessage(ColorText.translate(lang.getString("LANG.REGION.ENTER-MESSAGE").replace("{COLOR}", event.getRegion().getColor().toString()).replace("{REGION}", event.getRegion().getName())));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    final void onPlayerRegionLeave(PlayerRegionLeaveEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        if (settings.getBoolean("REGION.LEAVE-MESSAGE")) {
            player.sendMessage(ColorText.translate(lang.getString("LANG.REGION.LEAVE-MESSAGE").replace("{COLOR}", event.getRegion().getColor().toString()).replace("{REGION}", event.getRegion().getName())));
        }
    }

    //IMPORTANT EVENTS

    @EventHandler(priority = EventPriority.MONITOR)
    final void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Region region = Region.getByLocation(player.getLocation());
            if (region != null && region.get(RegionOption.SAFE_ZONE)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    final void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Region region = Region.getByLocation(player.getLocation());
        if (region != null && region.get(RegionOption.DENY_DROP)) {
            event.setCancelled(true);
            player.sendMessage(ColorText.translate(lang.getString("LANG.REGION.DROP-ITEM-CANCELLED")));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    final void onPlayerTeleport(PlayerTeleportEvent event) {
        //CHECK IF TP CAUSE IS AN ENDERPEARL
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            Player player = event.getPlayer();
            Region region = Region.getByLocation(event.getTo());
            if (region != null && region.get(RegionOption.DENY_PEARL)) {
                if (region == Region.getByLocation(player.getLocation())) {
                    return;
                }
                event.setCancelled(true);
                player.sendMessage(ColorText.translate(lang.getString("LANG.REGION.PEARL-DENIED")));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    final void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = player.getItemInHand();
        Region region = Region.getByLocation(player.getLocation());

        if (!event.hasItem() || region == null || !region.get(RegionOption.DENY_THROW_PEARL) || !event.getAction().name().startsWith("RIGHT")) {
            return;
        }

        if (stack.getType() == Material.ENDER_PEARL) {
            event.setCancelled(true);
            event.setUseItemInHand(Event.Result.DENY);
            player.updateInventory();

            player.sendMessage(ColorText.translate(lang.getString("LANG.REGION.THROW-PEARL-DENIED")));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    final void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Region region = Region.getByLocation(player.getLocation());

        if (region == null) {
            return;
        }

        if (event.getMessage().startsWith("/") && region.get(RegionOption.DENY_COMMANDS)) {
            event.setCancelled(true);
            player.sendMessage(ColorText.translate(lang.getString("LANG.REGION.COMMAND-CANCELLED")));
            return;
        }

        if (region.get(RegionOption.DENY_CHAT)) {
            event.setCancelled(true);
            player.sendMessage(ColorText.translate(lang.getString("LANG.REGION.CHAT-MESSAGE-CANCELLED")));
        }

    }

    @EventHandler
    final void onCreatureSpawn(CreatureSpawnEvent event) {
        Region region = Region.getByLocation(event.getEntity().getLocation());
        if (region == null || !region.get(RegionOption.DENY_MOB_SPAWN)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    final void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Region region = Region.getByLocation(event.getBlock().getLocation());
        if (region == null || !region.get(RegionOption.DENY_PLACE_BLOCKS) || (player.getGameMode() == GameMode.CREATIVE && player.isOp())) {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(ColorText.translate(lang.getString("LANG.REGION.CANT-PLACE-BLOCK").replace("{PLAYERNAME}", player.getName())));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    final void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Region region = Region.getByLocation(event.getBlock().getLocation());
        if (region == null || !region.get(RegionOption.DENY_BREAK_BLOCKS) || (player.getGameMode() == GameMode.CREATIVE && player.isOp())) {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(ColorText.translate(lang.getString("LANG.REGION.CANT-BREAK-BLOCK").replace("{PLAYERNAME}", player.getName())));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    final void onEntityExplode(EntityExplodeEvent event) {
        Region region = Region.getByLocation(event.getEntity().getLocation());
        if (region == null || !region.get(RegionOption.DENY_ENTITY_EXPLODE)) {
            return;
        }

        event.setCancelled(true);
    }

    //WAND SELECTION EVENTS
    @EventHandler
    final void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        player.getInventory().remove(selectionWand);
        Selection.getOrCreate(player).remove();
    }

    @EventHandler
    final void onPlayerUseSelection(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("root.command.region") || !event.hasItem() || !event.getItem().isSimilar(selectionWand)
                || !event.getAction().name().contains("BLOCK") || event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR) {
            return;
        }
        Selection selection = Selection.getOrCreate(player);
        Location location = event.getClickedBlock().getLocation();

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            selection.setFirstPosition(location);
        } else {
            selection.setSecondPosition(location);
        }

        String message = lang.getString("LANG.SELECTION." + (event.getAction() == Action.LEFT_CLICK_BLOCK ? "FIRST" : "SECOND") + "-LOCATION");
        message = message.replace("{X}", String.valueOf(location.getBlockX()));
        message = message.replace("{Y}", String.valueOf(location.getBlockY()));
        message = message.replace("{Z}", String.valueOf(location.getBlockZ()));
        message = message.replace("{MATERIAL}", WordUtils.capitalizeFully(location.getBlock().getType().name()));
        player.sendMessage(ColorText.translate(message));
    }

    @EventHandler
    final void onBlockBreakSelection(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = player.getItemInHand();
        if (stack != null && stack.isSimilar(getSelectionWand())) {
            event.setCancelled(true);
        }
    }
}