package me.jeyzer.config.generic;

import me.jeyzer.utils.YamlFile;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;

public interface YamlDirectory extends Iterable<YamlFile> {

    @Nullable YamlDirectory into(String path);

    @Nullable YamlFile yaml(String name);

    void forEachSub(Consumer<? super YamlFile> consumer);

    Collection<YamlFile> collect();

    void reload();

}