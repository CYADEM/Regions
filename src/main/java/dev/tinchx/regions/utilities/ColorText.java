package dev.tinchx.regions.utilities;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class ColorText {

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> translate(List<String> strings) {
        return strings.stream().map(ColorText::translate).collect(Collectors.toList());
    }
}