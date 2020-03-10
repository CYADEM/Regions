package dev.tinchx.regions.command.argument;

import dev.tinchx.regions.RegionsPlugin;
import dev.tinchx.regions.config.Config;
import dev.tinchx.regions.listeners.RegionListener;
import dev.tinchx.regions.region.Region;
import dev.tinchx.regions.selection.Selection;
import dev.tinchx.regions.utilities.ColorText;
import dev.tinchx.regions.utilities.command.RootArgument;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class RegionCreateArgument extends RootArgument {

    private Config lang = RegionsPlugin.getInstance().getLang();

    public RegionCreateArgument() {
        super("create", "Create a region");
        setOnlyPlayers(true);
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <regionName>";
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage(ColorText.translate("&cUsage: " + getUsage(label)));
        } else {
            Region region = Region.getByName(args[1]);
            if (region != null) {
                player.sendMessage(ColorText.translate(lang.getString("LANG.REGION.ALREADY-CREATED").replace("{NAME}", region.getName())));
            } else {
                if (Bukkit.getPluginManager().getPlugin("WorldEdit") == null) {
                    player.sendMessage(ColorText.translate(lang.getString("LANG.REGION.INSTALL-WORLD-EDIT")));
                    return;
                }
                Selection selection = Selection.getOrCreate(player);
                if (!selection.ready()) {
                    player.sendMessage(ColorText.translate(lang.getString("LANG.REGION.MUST-SELECT")));
                    return;
                }

                if (!selection.getFirstPosition().getWorld().equals(selection.getSecondPosition().getWorld())) {
                    player.sendMessage(ColorText.translate(lang.getString("LANG.SAME-WORLD-SELECTION")));
                    return;
                }

                region = new Region(args[1], selection.getFirstPosition(), selection.getSecondPosition());
                region.save();

                player.sendMessage(ColorText.translate(lang.getString("LANG.REGION.CREATED").replace("{NAME}", region.getName())));
                player.getInventory().remove(RegionListener.getSelectionWand());
            }
        }
    }
}