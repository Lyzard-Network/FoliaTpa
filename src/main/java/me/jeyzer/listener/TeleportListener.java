package me.jeyzer.listener;

import lombok.RequiredArgsConstructor;
import me.jeyzer.handler.TeleportRequests;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class TeleportListener implements Listener {

    private final TeleportRequests requests;

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        requests.flush(event.getPlayer());
    }

}
