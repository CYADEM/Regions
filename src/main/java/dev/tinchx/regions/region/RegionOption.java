package dev.tinchx.regions.region;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum RegionOption {
    SAFE_ZONE(Arrays.asList("&7If enabled, no one will", "&7be damaged in this region.")),
    DENY_DROP(Arrays.asList("&7If enabled, no one will", "&7have permissions to drop items.")),
    DENY_PEARL(Arrays.asList("&7If someone throw pearl to this zone", "&7pearl teleportation will be cancelled.")),
    DENY_THROW_PEARL(Arrays.asList("&7If the player is in this region", "&7and throw pearl, teleport will be cancelled.")),
    DENY_CHAT(Arrays.asList("&7Chat will be disallowed to everybody", "&7who are in this region.")),
    DENY_COMMANDS(Arrays.asList("&7Commands will be cancelled in this region.", "&7Except operators!")),
    DENY_MOB_SPAWN(Arrays.asList("&7Mob spawning event will be", "&7cancelled in this region.")),
    DENY_PLACE_BLOCKS(Arrays.asList("&7If enabled, no one will be", "&7allowed to place blocks in this region.", "&7Except operators!")),
    DENY_BREAK_BLOCKS(Arrays.asList("&7If enabled, no one will be", "&7allowed to break blocks in this region.", "&7Except operators!")),
    DENY_ENTITY_EXPLODE(Arrays.asList("&7If enabled, TNT and Creepers", "&7or another explosion, will be cancelled."));

    private List<String> description;
}