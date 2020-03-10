package dev.tinchx.regions.utilities.command;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public abstract class RootArgument {

    private String name, description;
    private String[] aliases;

    @Setter
    private String permission;
    @Setter
    private boolean onlyPlayers;

    public RootArgument(String name) {
        this(name, null);
    }

    public RootArgument(String name, String description) {
        this(name, description, null);
    }

    public RootArgument(String name, String description, String permission) {
        this(name, description, permission, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public RootArgument(String name, String description, String permission, String... aliases) {
        this(name, description, permission, aliases, false);
    }

    public RootArgument(String name, String description, String permission, String[] aliases, boolean onlyPlayers) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.aliases = Arrays.copyOf(aliases, aliases.length);
        this.onlyPlayers = onlyPlayers;
    }

    public abstract String getUsage(String label);

    public abstract void execute(CommandSender sender, String label, String[] args);

    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        return Collections.emptyList();
    }
}