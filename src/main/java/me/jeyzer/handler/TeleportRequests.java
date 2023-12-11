package me.jeyzer.handler;

import me.jeyzer.handler.model.TeleportRequest;
import me.jeyzer.handler.struct.TeleportType;
import me.jeyzer.utils.Search;
import org.bukkit.entity.Player;

import java.util.*;

public class TeleportRequests {

    private final Map<UUID, List<TeleportRequest>> pending = new HashMap<>();

    public TeleportRequest request(Player requester, Player requested, TeleportType type) {
        List<TeleportRequest> requests = pending.computeIfAbsent(requested.getUniqueId(), k -> new ArrayList<>());
        TeleportRequest request = new TeleportRequest(this, requester, requested, type);
        requests.add(request);
        return request;
    }

    public Search<TeleportRequest> search(Player requester, Player requested) {
        List<TeleportRequest> requests = pending.get(requested.getUniqueId());

        if (requests == null)
            return Search.NO_RESULT;

        for (TeleportRequest request : requests) {
            if (request == null || request.getRequester() != requester)
                continue;

            return Search.of(request);
        }

        return Search.NO_RESULT;
    }

    public void delete(Player requester, Player requested) {
        List<TeleportRequest> requests = pending.get(requested.getUniqueId());

        if (requests == null)
            return;

        requests.removeIf(request -> request.isRequester(requester));
    }

    public void flush(Player player) {
        pending.remove(player.getUniqueId());
    }


}
