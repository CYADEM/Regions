package dev.tinchx.regions.command.argument;

import dev.tinchx.regions.RegionsPlugin;
import dev.tinchx.regions.config.Config;
import dev.tinchx.regions.listeners.RegionListener;
import dev.tinchx.regions.utilities.ColorText;
import dev.tinchx.regions.utilities.command.RootArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class RegionWandArgument extends RootArgument {

    private Config lang = RegionsPlugin.getInstance().getLang();

    public RegionWandArgument() {
        super("wand");
        setOnlyPlayers(true);
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        PlayerInventory inventory = player.getInventory();

        inventory.remove(RegionListener.getSelectionWand());
        inventory.addItem(RegionListener.getSelectionWand());

        player.updateInventory();

        player.sendMessage(ColorText.translate(lang.getString("LANG.SELECTION.WAND-RECEIVED")));
    }
}