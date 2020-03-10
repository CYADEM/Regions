package dev.tinchx.regions.command;

import dev.tinchx.regions.command.argument.*;
import dev.tinchx.regions.utilities.command.RootCommand;

import java.util.Arrays;

public final class RegionCommand extends RootCommand {

    public RegionCommand() {
        super("region", null, "regions");
        setPermission("root.command.region");

        Arrays.asList(new RegionCreateArgument(), new RegionDeleteArgument(), new RegionListArgument(),
                new RegionManageArgument(), new RegionWandArgument()).forEach(this::register);
    }
}
