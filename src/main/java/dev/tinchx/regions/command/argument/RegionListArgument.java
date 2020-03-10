package dev.tinchx.regions.command.argument;

import dev.tinchx.regions.RegionsPlugin;
import dev.tinchx.regions.config.Config;
import dev.tinchx.regions.region.Region;
import dev.tinchx.regions.utilities.ColorText;
import dev.tinchx.regions.utilities.command.RootArgument;
import dev.tinchx.regions.utilities.location.LocationUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class RegionListArgument extends RootArgument {

    private Config lang = RegionsPlugin.getInstance().getLang();

    public RegionListArgument() {
        super("list", "List of all regions created");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage(LocationUtils.getString(((Player) sender).getLocation()));
        }
        if (Region.getRegions().isEmpty()) {
            sender.sendMessage(ColorText.translate(lang.getString("LANG.REGION-EMPTY-LIST")));
        } else {
            sender.sendMessage(ColorText.translate(lang.getString("LANG.REGION-LIST-HEADER").replace("{SIZE}", String.valueOf(Region.getRegions().size()))));
            Region.getRegions().forEach(region -> sender.sendMessage(ColorText.translate(lang.getString("LANG.REGION-LIST-FORMAT").replace("{NAME}", region.getName()))));
            sender.sendMessage(ColorText.translate(lang.getString("LANG.REGION-LIST-FOOTER").replace("{SIZE}", String.valueOf(Region.getRegions().size()))));
        }
    }
}