package dev.tinchx.regions.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Config extends YamlConfiguration {

    private String fileName;
    private JavaPlugin javaPlugin;

    public Config(JavaPlugin javaPlugin, String fileName) {
        this.javaPlugin = javaPlugin;
        this.fileName = fileName;
        createNewFile();
    }

    private void createNewFile() {
        File folder = javaPlugin.getDataFolder();
        try {
            File file = new File(folder, fileName);
            if(!file.exists()) {
                if (javaPlugin.getResource(fileName) != null) {
                    javaPlugin.saveResource(fileName, false);
                } else {
                    save(file);
                }
            } else {
                load(file);
                save(file);
            }
        } catch (Exception ignored) {

        }
    }

    public void save() {
        File folder = javaPlugin.getDataFolder();
        try {
            save(new File(folder, this.fileName));
        } catch (Exception ignored) {
        }
    }
}