package me.jeyzer.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import lombok.RequiredArgsConstructor;
import me.jeyzer.handler.TeleportRequests;
import me.jeyzer.handler.model.TeleportRequest;
import me.jeyzer.handler.struct.TeleportType;
import me.jeyzer.utils.Messages;
import me.jeyzer.utils.Search;
import me.jeyzer.utils.TeleportUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("tpaccept")
@RequiredArgsConstructor
public class TpAcceptAikar extends BaseCommand {

    private final TeleportRequests requests;

    @Default
    public void execute(Player player, String[] args) {
        if (args.length != 1) {
            player.sendMessage(Messages.toComponent("TPA_ACCEPT.USAGE"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Messages.toComponent("ERRORS.PLAYER-NOT-ONLINE"));
            return;
        }

        Search<TeleportRequest> search = requests.search(target, player);

        if (!search.exists()) {
            player.sendMessage(Messages.toComponent("TPA_ACCEPT.NO_REQUEST"));
            return;
        }

        TeleportRequest request = search.result();
        TeleportUtils.startCountdownTeleport(request.getType() == TeleportType.TPA ? target : player, request.getType() == TeleportType.TPA ? player.getLocation() : target.getLocation());
        request.selfDestruct();
        player.sendMessage(Messages.toComponent("TPA_ACCEPT.ACCEPTED"));
        target.sendMessage(Messages.toComponent("TPA_ACCEPT.ACCEPTED"));
    }
}
