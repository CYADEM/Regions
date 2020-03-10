package dev.tinchx.regions;

import dev.tinchx.regions.command.RegionCommand;
import dev.tinchx.regions.config.Config;
import dev.tinchx.regions.inventory.MakerListener;
import dev.tinchx.regions.listeners.RegionListener;
import dev.tinchx.regions.mongo.MongoManager;
import dev.tinchx.regions.region.Region;
import dev.tinchx.regions.utilities.command.CommandHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@Getter
public class RegionsPlugin extends JavaPlugin {

    private Config settings, lang;
    private MongoManager mongoManager;

    @Override
    public void onEnable() {
        load();
    }

    @Override
    public void onDisable() {
        Region.getRegions().forEach(Region::save);
        mongoManager.close();
    }

    private void load() {
        this.settings = new Config(this, "settings.yml");
        this.lang = new Config(this, "lang.yml");
        (this.mongoManager = new MongoManager()).load(this.settings);
        Region.loadRegions();

        registerCommands();
        registerListeners();
    }

    private void registerCommands() {
        CommandHandler handler = new CommandHandler(this);
        handler.register(new RegionCommand());
    }

    private void registerListeners() {
        Arrays.asList(new MakerListener(), new RegionListener()).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    public static RegionsPlugin getInstance() {
        return getPlugin(RegionsPlugin.class);
    }
}