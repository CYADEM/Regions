package dev.tinchx.regions.region;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dev.tinchx.regions.RegionsPlugin;
import dev.tinchx.regions.cuboid.Cuboid;
import dev.tinchx.regions.utilities.document.DocumentSerializer;
import dev.tinchx.regions.utilities.location.LocationUtils;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

@Getter
public final class Region implements DocumentSerializer {

    @Getter
    private static List<Region> regions = Lists.newArrayList();

    private String name;
    private Location firstPosition, secondPosition;
    @Setter
    private ChatColor color;
    private Map<RegionOption, Boolean> regionOptions;

    public Region(String name, Location firstPosition, Location secondPosition) {
        this.name = name;
        this.firstPosition = firstPosition;
        this.secondPosition = secondPosition;
        this.color = ChatColor.WHITE;
        this.regionOptions = Maps.newHashMap();

        regions.add(this);
    }

    public void toggle(RegionOption option) {
        regionOptions.put(option, !get(option));
    }

    public boolean get(RegionOption option) {
        return regionOptions.getOrDefault(option, false);
    }

    public Cuboid getCubo() {
        if (firstPosition == null || secondPosition == null) {
            return null;
        }
        return new Cuboid(firstPosition, secondPosition);
    }

    @Override
    public Document serialize() {
        Document document = new Document();
        document.put("name", name);
        document.put("firstPosition", LocationUtils.getString(firstPosition));
        document.put("secondPosition", LocationUtils.getString(secondPosition));
        document.put("color", color.name());
        JsonArray optionsArray = new JsonArray();

        regionOptions.forEach((option, aBoolean) -> {
            JsonObject object = new JsonObject();
            object.addProperty("name", option.name());
            object.addProperty("value", aBoolean);
            optionsArray.add(object);
        });

        if (optionsArray.size() > 0) {
            document.put("regionOptions", optionsArray.toString());
        }
        return document;
    }

    public void delete() {
        RegionsPlugin.getInstance().getMongoManager().getCollection().deleteOne(Filters.eq("name", name));
        regions.remove(this);
    }

    public void save() {
        RegionsPlugin.getInstance().getMongoManager().getCollection().replaceOne(Filters.eq("name", name), serialize(), new UpdateOptions().upsert(true));
    }

    public static Region getByLocation(Location location) {
        return regions.stream().filter(region -> region.getCubo() != null && region.getCubo().contains(location)).findFirst().orElse(null);
    }

    public static Region getByName(String name) {
        return regions.stream().filter(region -> region.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static void loadRegions() {
        try (MongoCursor cursor = RegionsPlugin.getInstance().getMongoManager().getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                Document document = (Document) cursor.next();
                try {
                    Region region = new Region(document.getString("name"), LocationUtils.getLocation(document.getString("firstPosition")), LocationUtils.getLocation(document.getString("secondPosition")));

                    region.setColor(ChatColor.valueOf(document.getString("color")));
                    if (document.containsKey("regionOptions")) {
                        if (document.get("regionOptions") instanceof String) {
                            JsonArray array = new JsonParser().parse(document.getString("regionOptions")).getAsJsonArray();

                            for (JsonElement jsonElement : array) {
                                JsonObject jsonObject = jsonElement.getAsJsonObject();
                                try {
                                    region.getRegionOptions().put(RegionOption.valueOf(jsonObject.get("name").getAsString()), jsonObject.get("value").getAsBoolean());
                                } catch (Exception ignored) {
                                }
                            }
                        }
                    }
                } catch (Exception ignored) {
                    System.out.println("[Regions] Region " + (document.containsKey("name") ? "'" + document.getString("name") + "'" : "'Unknown Name'") + " could not be loaded...");
                }
            }
        }
    }

}