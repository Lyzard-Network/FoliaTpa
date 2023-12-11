package me.jeyzer.config.generic;

import me.jeyzer.utils.YamlFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public class SubDirectory implements YamlDirectory {

    private final Map<String, YamlDirectory> directories = new HashMap<>();
    private final Map<String, YamlFile> files = new HashMap<>();

    public SubDirectory(File path) {
        if (!path.isDirectory())
            return;

        for (File entity : path.listFiles()) {
            String name = entity.getName();

            if (entity.isDirectory()) {
                directories.put(name, new SubDirectory(entity));
                continue;
            }

            if (!name.endsWith(".yml"))
                continue;

            files.put(name, YamlFile.unsafe(entity));
        }
    }

    @Nullable
    public YamlDirectory into(String path) {
        return directories.get(path);
    }

    @Nullable
    public YamlFile yaml(String name) {
        return files.get(name + ".yml");
    }

    @NotNull
    @Override
    public Iterator<YamlFile> iterator() {
        return collect().iterator();
    }

    @Override
    public void forEachSub(Consumer<? super YamlFile> consumer) {
        directories.values().forEach(directory -> {
            directory.forEach(consumer);
            directory.forEachSub(consumer);
        });
    }

    @Override
    public Collection<YamlFile> collect() {
        return List.copyOf(files.values());
    }

    @Override
    public void reload() {
        forEach(YamlFile::reload);
        forEachSub(YamlFile::reload);
    }

}