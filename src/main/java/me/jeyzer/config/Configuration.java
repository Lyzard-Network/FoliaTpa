package me.jeyzer.config;

import me.jeyzer.config.generic.HeadDirectory;
import me.jeyzer.config.indexed.Config;
import me.jeyzer.utils.YamlFile;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class Configuration extends HeadDirectory {

    private final Map<Config, YamlFile> configurations = new EnumMap<>(Config.class);

    public Configuration(Plugin plugin) {
        super(plugin.getDataFolder());

        Arrays.stream(Config.values()).forEach(config -> configurations.put(config, YamlFile.resource(plugin, config.getName())));
    }

    public YamlFile find(@NotNull Config config) {
        return configurations.get(config);
    }

    public Collection<YamlFile> collect() {
        return List.copyOf(configurations.values());
    }

    @Override
    public void forEach(Consumer<? super YamlFile> consumer) {
        collect().forEach(consumer);
    }

    @Override
    public @Nullable YamlFile yaml(String name) {
        Config parsed = Config.parse(name);
        return parsed == null ? null : find(parsed);
    }

}
