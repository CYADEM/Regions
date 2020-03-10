package dev.tinchx.regions.utilities;

import com.google.common.base.Preconditions;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

import java.util.List;
import java.util.stream.Collectors;

public class RegionUtils {

    public static DyeColor getDyeColor(ChatColor color) {
        switch (color) {
            case AQUA:
            case BLUE:
                return DyeColor.LIGHT_BLUE;
            case BLACK:
                return DyeColor.BLACK;
            case DARK_AQUA:
                return DyeColor.CYAN;
            case DARK_BLUE:
                return DyeColor.BLUE;
            case DARK_GRAY:
                return DyeColor.GRAY;
            case DARK_GREEN:
                return DyeColor.GREEN;
            case DARK_PURPLE:
                return DyeColor.PURPLE;
            case DARK_RED:
            case RED:
                return DyeColor.RED;
            case GOLD:
                return DyeColor.ORANGE;
            case GRAY:
                return DyeColor.SILVER;
            case GREEN:
                return DyeColor.LIME;
            case LIGHT_PURPLE:
                return DyeColor.MAGENTA;
            case YELLOW:
                return DyeColor.YELLOW;
            default:
                return DyeColor.WHITE;
        }
    }

    public static List<String> getCompletions(String[] args, List<String> input) {
        Preconditions.checkNotNull((Object) args);
        Preconditions.checkArgument(args.length != 0);
        String argument = args[args.length - 1];
        return input.stream().filter(string -> string.regionMatches(true, 0, argument, 0, argument.length())).limit(80).collect(Collectors.toList());
    }
}