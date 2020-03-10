package dev.tinchx.regions.command.argument;

import dev.tinchx.regions.RegionsPlugin;
import dev.tinchx.regions.config.Config;
import dev.tinchx.regions.region.Region;
import dev.tinchx.regions.utilities.ColorText;
import dev.tinchx.regions.utilities.command.RootArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class RegionDeleteArgument extends RootArgument {

    private Config lang = RegionsPlugin.getInstance().getLang();

    public RegionDeleteArgument() {
        super("delete", "Delete a region");
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
            if (region == null) {
                player.sendMessage(ColorText.translate(lang.getString("LANG.REGION.NOT-CREATED").replace("{NAME}", args[1])));
            } else {
                player.sendMessage(ColorText.translate(lang.getString("LANG.REGION.DELETED").replace("{NAME}", region.getName())));
                region.delete();
            }
        }
    }
}