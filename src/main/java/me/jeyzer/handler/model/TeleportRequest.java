package me.jeyzer.handler.model;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import lombok.Getter;

import me.jeyzer.FoliaTpa;
import me.jeyzer.handler.TeleportRequests;

import me.jeyzer.handler.struct.TeleportType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeleportRequest {

    private final TeleportRequests manager;
    private final ScheduledTask selfDestructor;
    private final UUID requester, requested;
    @Getter private final TeleportType type;

    public TeleportRequest(TeleportRequests manager, Player requester, Player requested, TeleportType type) {
        this.manager = manager;
        this.requester = requester.getUniqueId();
        this.requested = requested.getUniqueId();
        this.type = type;

        selfDestructor = Bukkit.getServer().getRegionScheduler().runDelayed(FoliaTpa.getInstance(), requester.getLocation(), task -> manager.delete(requester, requested), 600L);
    }

    public boolean isRequester(Player player) {
        return player.getUniqueId().equals(requester);
    }

    public Player getRequester() {
        return Bukkit.getPlayer(requester);
    }

    public Player getRequested() {
        return Bukkit.getPlayer(requested);
    }

    public void selfDestruct() {
        selfDestructor.cancel();

        Player
           requester = getRequester(),
           requested = getRequested();

        if (requested == null)
            return;

        manager.delete(requester, requested);
    }

}
