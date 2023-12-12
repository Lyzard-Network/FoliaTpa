package me.jeyzer.commands;

import lombok.RequiredArgsConstructor;
import me.jeyzer.handler.TeleportRequests;
import me.jeyzer.handler.struct.TeleportType;
import me.jeyzer.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class TpahereCommand implements CommandExecutor {

    private final TeleportRequests requests;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String str, @NotNull String[] args) {
        if (!(sender instanceof Player))
            return false;

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(Messages.toComponent("TPA_HERE.USAGE"));
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Messages.toComponent("ERRORS.PLAYER-NOT-ONLINE"));
            return false;
        }

        if (player.getName().equals(target.getName())) {
            player.sendMessage(Messages.toComponent("TPA.CANT_SELF"));
            return false;
        }

        if (requests.search(player, target).exists()) {
            player.sendMessage(Messages.toComponent("TPA.ALREADY_SENT"));
            return false;
        }

        requests.request(player, target, TeleportType.TPA_HERE);

        player.sendMessage(Messages.toComponent("TPA.SUCCESS", "player", target.getName()));
        target.sendMessage(Messages.toComponent("TPA_HERE.TARGET_RECEIVED", "player", player.getName()));

        target.sendMessage(Messages.toComplexComponent(
           "TPA_HERE.TARGET_ACCEPT",
           "TPA_HERE.TARGET_ACCEPT_HOVER",
           "/tpaccept " + player.getName()));
        return false;
    }
}
