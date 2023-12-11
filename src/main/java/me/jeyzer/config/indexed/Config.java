package me.jeyzer.config.indexed;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.jeyzer.utils.Search;

@Getter
@RequiredArgsConstructor
public enum Config {

    HOME_GUI("home-gui"),
    REQUEST_GUI("request-gui"),
    RESET_ZONE("reset-zone"),
    CONFIG("config"),
    LANGUAGE("messages"),
    KITS("kits"),
    GEMS_SHOP("gems-shop"),
    WARPS("warps");

    private final String name;

    public static Config parse(String name) {
        return Search.exceptional(() -> Config.valueOf(name.toUpperCase()));
    }

}