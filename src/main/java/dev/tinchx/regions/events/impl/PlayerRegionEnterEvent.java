package dev.tinchx.regions.events.impl;

import dev.tinchx.regions.events.PlayerBase;
import dev.tinchx.regions.region.Region;
import lombok.Getter;
import org.bukkit.entity.Player;

public class PlayerRegionEnterEvent extends PlayerBase {

    @Getter
    private Region region;

    public PlayerRegionEnterEvent(Player player, Region region) {
        super(player);
        this.region = region;
    }
}