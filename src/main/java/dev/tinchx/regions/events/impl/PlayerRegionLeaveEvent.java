package dev.tinchx.regions.events.impl;

import dev.tinchx.regions.events.PlayerBase;
import dev.tinchx.regions.region.Region;
import lombok.Getter;
import org.bukkit.entity.Player;

public class PlayerRegionLeaveEvent extends PlayerBase {

    @Getter
    private Region region;

    public PlayerRegionLeaveEvent(Player player, Region region) {
        super(player);
        this.region = region;
    }
}