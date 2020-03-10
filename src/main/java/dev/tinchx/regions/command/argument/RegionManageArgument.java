package dev.tinchx.regions.command.argument;

import com.google.common.collect.Lists;
import dev.tinchx.regions.RegionsPlugin;
import dev.tinchx.regions.config.Config;
import dev.tinchx.regions.inventory.InventoryMaker;
import dev.tinchx.regions.region.Region;
import dev.tinchx.regions.region.RegionOption;
import dev.tinchx.regions.utilities.ColorText;
import dev.tinchx.regions.utilities.RegionUtils;
import dev.tinchx.regions.utilities.command.RootArgument;
import dev.tinchx.regions.utilities.item.ItemMaker;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public final class RegionManageArgument extends RootArgument {

    private Config lang = RegionsPlugin.getInstance().getLang();

    public RegionManageArgument() {
        super("manage", "Manage a region options");
        setOnlyPlayers(true);
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <regionName>";
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage(ColorText.translate("&cUsage: " + getUsage(label)));
        } else {
            Region region = Region.getByName(args[1]);
            if (region == null) {
                player.sendMessage(ColorText.translate(lang.getString("LANG.REGION.NOT-CREATED").replace("{NAME}", args[1])));
            } else {
                InventoryMaker inventoryMaker = new InventoryMaker("Region Management", 1);

                for (int i = 0; i < 9; i++) {
                    inventoryMaker.setItem(i, new InventoryMaker.ClickableItem() {
                        @Override
                        public void onClick(InventoryClickEvent event) {

                        }

                        @Override
                        public ItemStack getItemStack() {
                            return new ItemMaker(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).create();
                        }
                    });
                }

                inventoryMaker.setItem(1, new InventoryMaker.ClickableItem() {
                    @Override
                    public void onClick(InventoryClickEvent event) {
                        InventoryMaker maker = new InventoryMaker("Select a color", 2);

                        Arrays.stream(ChatColor.values()).filter(color -> color != ChatColor.UNDERLINE &&
                                color != ChatColor.BOLD && color != ChatColor.STRIKETHROUGH &&
                                color != ChatColor.MAGIC && color != ChatColor.ITALIC && color != ChatColor.RESET).
                                forEach(color -> maker.addItem(new InventoryMaker.ClickableItem() {
                                    @Override
                                    public void onClick(InventoryClickEvent event) {
                                        region.setColor(color);
                                        region.save();
                                        player.closeInventory();
                                        player.sendMessage(ColorText.translate("&7You just changed " + region.getName() + "'s color to " + color + WordUtils.capitalizeFully(color.name().replace("_", " ") + "&7.")));
                                    }

                                    @Override
                                    public ItemStack getItemStack() {
                                        return new ItemMaker(Material.WOOL).setDyeColor(RegionUtils.getDyeColor(color)).setDisplayName(color + WordUtils.capitalizeFully(color.name().replace("_", " ")))
                                                .addLore("", "&7Please click here to use this color!", "").create();
                                    }
                                }));

                        player.openInventory(maker.getCurrentPage());
                    }

                    @Override
                    public ItemStack getItemStack() {
                        return new ItemMaker(Material.WOOL).setDisplayName("&9&l* &e&lRegion Color &9&l*").create();
                    }
                });

                inventoryMaker.setItem(4, new InventoryMaker.ClickableItem() {
                    @Override
                    public void onClick(InventoryClickEvent event) {
                        InventoryMaker maker = new InventoryMaker("Region Options", 2);

                        int i = 0;

                        for (RegionOption option : RegionOption.values()) {
                            maker.setItem(i++, new InventoryMaker.ClickableItem() {
                                @Override
                                public void onClick(InventoryClickEvent event) {
                                    region.toggle(option);
                                    //sender.sendMessage(ColorText.translate("&7This option has been " + (region.get(option) ? "&aenabled" : "&cdisabled") + "&7!"));
                                }

                                @Override
                                public ItemStack getItemStack() {
                                    List<String> strings = Lists.newArrayList(option.getDescription());
                                    strings.add(" ");
                                    strings.add("  &7This option is " + (region.get(option) ? "&aenabled" : "&cdisabled") + "&7!");
                                    strings.add("  &7- &eClick here to toggle option &7-");
                                    return new ItemMaker(Material.INK_SACK).setDisplayName((region.get(option) ? "&a" : "&c")
                                            + "&l" + WordUtils.capitalizeFully(option.name().replace("_", " "))).setDurability((region.get(option) ? 10 : 1)).addLore(strings).create();
                                }
                            }, player, true);
                        }

                        player.openInventory(maker.getCurrentPage());
                    }

                    @Override
                    public ItemStack getItemStack() {
                        List<String> strings = Lists.newArrayList();
                        strings.add("&7&m" + StringUtils.repeat("-", 20));
                        strings.add(" ");
                        int size = strings.size();
                        for (RegionOption option : RegionOption.values()) {
                            if (!region.get(option)) {
                                continue;
                            }
                            strings.add(" &7- &e" + WordUtils.capitalizeFully(option.name().replace("_", " ")));
                        }
                        if ((strings.size() - 2) <= 0) {
                            strings.add("&cThere are no options enabled in this region!");
                        } else {
                            strings.add(size, "&7Displaying " + region.getColor() + region.getName() + "'s Options&7:");
                            strings.add(size + 1, " ");
                        }
                        strings.add(" ");
                        strings.add("&7&m" + StringUtils.repeat("-", 20));
                        return new ItemMaker(Material.ANVIL).setDisplayName("&9&l* &c&lRegion Management &9&l*").addLore(strings).create();
                    }
                });

                inventoryMaker.setItem(7, new InventoryMaker.ClickableItem() {
                    @Override
                    public void onClick(InventoryClickEvent event) {
                        InventoryMaker maker = new InventoryMaker("You want delete " + region.getName() + '?', 1);

                        for (int i = 0; i < 9; i++) {
                            maker.setItem(i, new InventoryMaker.ClickableItem() {
                                @Override
                                public void onClick(InventoryClickEvent event) {
                                    player.closeInventory();
                                    player.sendMessage(ColorText.translate("&cProcedure cancelled."));
                                }

                                @Override
                                public ItemStack getItemStack() {
                                    return new ItemMaker(Material.STAINED_GLASS_PANE).setDurability(14).setDisplayName("&cCancel").create();
                                }
                            });
                        }

                        maker.setItem(4, new InventoryMaker.ClickableItem() {
                            @Override
                            public void onClick(InventoryClickEvent event) {
                                player.closeInventory();
                                player.sendMessage(ColorText.translate("&cDeleting..."));
                                player.performCommand("region delete " + region.getName());
                            }

                            @Override
                            public ItemStack getItemStack() {
                                return new ItemMaker(Material.STAINED_GLASS_PANE).setDurability(13).setDisplayName("&a&lConfirm :)").create();
                            }
                        });

                        player.openInventory(maker.getCurrentPage());
                    }

                    @Override
                    public ItemStack getItemStack() {
                        return new ItemMaker(Material.WOOL).setDyeColor(DyeColor.RED).setDisplayName("&9&l* &4&lDelete Region &9&l*").addLore("&7Click to delete this region!").create();
                    }
                });

                /*int i = 0;

                for (RegionOption option : RegionOption.values()) {
                    inventoryMaker.setItem(i++, new InventoryMaker.ClickableItem() {
                        @Override
                        public void onClick(InventoryClickEvent event) {
                            region.toggle(option);
                            sender.sendMessage(ColorText.translate("&7This option has been " + (region.get(option) ? "&aenabled" : "&cdisabled") + "&7!"));
                        }

                        @Override
                        public ItemStack getItemStack() {
                            List<String> strings = Lists.newArrayList(option.getDescription());
                            strings.add(" ");
                            strings.add("  &7This option is " + (region.get(option) ? "&aenabled" : "&cdisabled") + "&7!");
                            strings.add("  &7- &eClick here to toggle option &7-");
                            return new ItemMaker((region.get(option) ? Material.EMERALD_BLOCK : Material.REDSTONE)).setDisplayName((region.get(option) ? "&a" : "&c")
                                    + "&l" + WordUtils.capitalizeFully(option.name().replace("_", " "))).addLore(strings).create();
                        }
                    }, player, true);
                }*/

                player.openInventory(inventoryMaker.getCurrentPage());
            }
        }
    }
}
