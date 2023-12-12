package me.jeyzer.config.indexed;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.jeyzer.utils.Search;

@Getter
@RequiredArgsConstructor
public enum Config {

    CONFIG("config"),
    LANGUAGE("messages");

    private final String name;

    public static Config parse(String name) {
        return Search.exceptional(() -> Config.valueOf(name.toUpperCase()));
    }

}