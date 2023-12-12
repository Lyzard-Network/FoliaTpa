package me.jeyzer.commands;

import lombok.RequiredArgsConstructor;
import me.jeyzer.handler.TeleportRequests;
import me.jeyzer.handler.model.TeleportRequest;
import me.jeyzer.handler.struct.TeleportType;
import me.jeyzer.utils.Messages;
import me.jeyzer.utils.Search;
import me.jeyzer.utils.TeleportUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class TpacceptCommand implements CommandExecutor {

    private final TeleportRequests requests;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String str, @NotNull String[] args) {
        if (!(sender instanceof Player))
            return false;

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(Messages.toComponent("TPA_ACCEPT.USAGE"));
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Messages.toComponent("ERRORS.PLAYER-NOT-ONLINE"));
            return false;
        }

        Search<TeleportRequest> search = requests.search(target, player);

        if (!search.exists()) {
            player.sendMessage(Messages.toComponent("TPA_ACCEPT.NO_REQUEST"));
            return false;
        }

        TeleportRequest request = search.result();
        TeleportUtils.startCountdownTeleport(request.getType() == TeleportType.TPA ? target : player, request.getType() == TeleportType.TPA ? player.getLocation() : target.getLocation());
        request.selfDestruct();
        player.sendMessage(Messages.toComponent("TPA_ACCEPT.ACCEPTED"));
        target.sendMessage(Messages.toComponent("TPA_ACCEPT.ACCEPTED"));
        return false;
    }
}
