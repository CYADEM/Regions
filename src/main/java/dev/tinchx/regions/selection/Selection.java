package dev.tinchx.regions.selection;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public final class Selection {

    private static List<Selection> selections = Lists.newArrayList();

    private Player player;
    @Setter
    private Location firstPosition, secondPosition;

    public Selection(Player player) {
        this.player = player;
    }

    public boolean ready() {
        return firstPosition != null && secondPosition != null;
    }

    public void remove() {
        selections.remove(this);
    }

    public static Selection getOrCreate(Player player) {
        Selection selection = selections.stream().filter(found -> found.getPlayer() == player).findFirst().orElse(null);

        if (selection == null) {
            selections.add(selection = new Selection(player));
        }

        return selection;
    }
}