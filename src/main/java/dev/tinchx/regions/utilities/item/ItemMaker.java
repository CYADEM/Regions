package dev.tinchx.regions.utilities.item;

import dev.tinchx.regions.utilities.ColorText;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemMaker {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemMaker(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemMaker(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemMaker setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemMaker setDisplayName(String name) {
        itemMeta.setDisplayName(ColorText.translate(name));
        return this;
    }

    public ItemMaker setOwner(String name) {
        if (itemStack.getType() == Material.SKULL_ITEM) {
            SkullMeta meta = (SkullMeta) itemMeta;
            meta.setOwner(name);
            itemMeta.setDisplayName(name);
        }
        return this;
    }

    public ItemMaker setDurability(int durability) {
        itemStack.setDurability((short) durability);
        return this;
    }

    public ItemMaker addLore(String lore) {
        Object object = itemMeta.getLore();
        if (object == null) object = new ArrayList<>();

        ((List) object).add(ColorText.translate(lore));
        itemMeta.setLore((List<String>) object);
        return this;
    }

    public ItemMaker addLore(List<String> lore) {
        itemMeta.setLore(ColorText.translate(lore));
        return this;
    }

    public ItemMaker addLore(String... lore) {
        List<String> strings = new ArrayList<>();
        for (String string : lore) {
            strings.add(ColorText.translate(string));
        }
        itemMeta.setLore(strings);
        return this;
    }

    public ItemMaker setEnchant(Enchantment enchantment, int level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }


    public ItemMaker setUnbreakable(boolean unbreakable) {
        itemMeta.spigot().setUnbreakable(unbreakable);
        return this;
    }

    public ItemMaker setColor(Color color) {
        if (itemStack.getType() != null && itemStack.getType().name().contains("LEATHER")) {
            LeatherArmorMeta armorMeta = (LeatherArmorMeta) itemMeta;
            armorMeta.setColor(color);
        }
        return this;
    }

    public ItemMaker setDyeColor(DyeColor dyeColor) {
        if (itemStack.getType() == Material.WOOL) {
            itemStack.setDurability(dyeColor.getWoolData());
        }
        return this;
    }

    public ItemStack create() {
        if (itemMeta != null) {
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

}