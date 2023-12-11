package me.jeyzer.utils;

import lombok.Getter;
import lombok.SneakyThrows;

import me.jeyzer.FoliaTpa;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class YamlFile extends YamlConfiguration {

    @Getter private final String name;
    private final File file;

    private YamlFile(File file) {
        Bukkit.getLogger().info(file.getName());
        String name = file.getName();

        this.name = name.substring(0, name.length() - 4);
        this.file = file;
    }

    public YamlFile save() {
        try {
            save(file);
        }catch (IOException ignored) {}

        return this;
    }

    public YamlFile reload() {
        try {
            load(file);
        } catch (IOException | InvalidConfigurationException exception) {
            FoliaTpa.getInstance().getLogger().log(Level.SEVERE, "Error during " + file.getName() + "'s load", exception);
        }
        return this;
    }


    /*  ===  S T A T I C ===  */

    public static YamlFile resource(Plugin plugin, String name) {
        name = name + ".yml";
        File file = new File(plugin.getDataFolder(), name);

        if (!file.exists())
            plugin.saveResource(name, false);

        return unsafe(file);
    }

    @Nullable @SneakyThrows
    public static YamlFile unsafe(File file) {
        if (!file.exists() && !file.createNewFile())
            return null;

        return !file.exists() && !file.createNewFile() ? null : safe(file);
    }

    public static YamlFile safe(File file) {
        return new YamlFile(file).reload();
    }

    /*  ===  S T A T I C ===  */

}
