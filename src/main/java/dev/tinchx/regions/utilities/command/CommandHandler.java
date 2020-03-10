package dev.tinchx.regions.utilities.command;

import dev.tinchx.regions.RegionsPlugin;
import dev.tinchx.regions.utilities.ColorText;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

public class CommandHandler {

    private JavaPlugin plugin;
    private CommandMap commandMap;

    public CommandHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(RootCommand command) {
        PluginCommand pluginCommand = getCommand(command.getName(), plugin);

        if (command.getDescription() != null) {
            pluginCommand.setDescription(command.getDescription());
        }
        if (command.getPermission() != null) {
            pluginCommand.setPermission(command.getPermission());
            pluginCommand.setPermissionMessage(ColorText.translate(RegionsPlugin.getInstance().getLang().getString("LANG.NO-PERMISSION")));
        }
        pluginCommand.setAliases(Arrays.asList(command.getAliases()));
        pluginCommand.setExecutor(command);
        pluginCommand.setTabCompleter(command);
        if (!getCommandMap().register(command.getName(), pluginCommand)) {
            pluginCommand.unregister(getCommandMap());
            getCommandMap().register(command.getName(), pluginCommand);
        }
    }

    private CommandMap getCommandMap() {
        if (commandMap != null) {
            return commandMap;
        }

        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);

            commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
        } catch (Exception ignored) {
        }

        return commandMap;
    }

    private PluginCommand getCommand(String name, Plugin owner) {
        PluginCommand command = null;

        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            command = constructor.newInstance(name, owner);
        } catch (Exception ignored) {
        }

        return command;
    }
}