package dev.tinchx.regions.utilities.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtils {

    public static String getString(Location loc) {
        StringBuilder builder = new StringBuilder();
        builder.append(loc.getX()).append(':');
        builder.append(loc.getY()).append(':');
        builder.append(loc.getZ()).append(':');
        builder.append(loc.getWorld().getName()).append(':');
        builder.append(loc.getYaw()).append(':');
        builder.append(loc.getPitch());
        return builder.toString();
    }

    public static Location getLocation(String s) {
        String[] data = s.split(":");
        double x = Double.parseDouble(data[0]);
        double y = Double.parseDouble(data[1]);
        double z = Double.parseDouble(data[2]);
        World world = Bukkit.getWorld(data[3]);
        float yaw = Float.parseFloat(data[4]);
        float pitch = Float.parseFloat(data[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }

}