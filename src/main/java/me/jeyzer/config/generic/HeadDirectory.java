package me.jeyzer.config.generic;

import me.jeyzer.utils.YamlFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public class HeadDirectory implements YamlDirectory {

    private final Map<String, YamlDirectory> directories = new HashMap<>();

    public HeadDirectory(File init) {
        if (!init.isDirectory())
            return;

        File[] files = init.listFiles();

        if (files == null)
            return;

        for (File entity : files) {
            if (!entity.isDirectory())
                continue;

            directories.put(entity.getName(), new SubDirectory(entity));
        }
    }

    @Override
    public @Nullable YamlDirectory into(String path) {
        return directories.get(path);
    }

    @Override
    public @Nullable YamlFile yaml(String name) {
        return null;
    }

    @NotNull
    @Override
    public Iterator<YamlFile> iterator() {
        return Collections.emptyIterator();
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
        return List.of();
    }

    @Override
    public void reload() {
        forEach(YamlFile::reload);
        forEachSub(YamlFile::reload);
    }
}
