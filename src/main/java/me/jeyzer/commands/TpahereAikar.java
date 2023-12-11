package me.jeyzer.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import lombok.RequiredArgsConstructor;
import me.jeyzer.handler.TeleportRequests;
import me.jeyzer.handler.struct.TeleportType;
import me.jeyzer.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("tpahere")
@RequiredArgsConstructor
public class TpahereAikar extends BaseCommand {

    private final TeleportRequests requests;

    @Default
    public void execute(Player player, String[] args) {
        if (args.length != 1) {
            player.sendMessage(Messages.toComponent("TPA_HERE.USAGE"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Messages.toComponent("ERRORS.PLAYER-NOT-ONLINE"));
            return;
        }

        if (player.getName().equals(target.getName())) {
            player.sendMessage(Messages.toComponent("TPA.CANT_SELF"));
            return;
        }

        if (requests.search(player, target).exists()) {
            player.sendMessage(Messages.toComponent("TPA.ALREADY_SENT"));
            return;
        }

        requests.request(player, target, TeleportType.TPA_HERE);

        player.sendMessage(Messages.toComponent("TPA.SUCCESS", "player", target.getName()));
        target.sendMessage(Messages.toComponent("TPA_HERE.TARGET_RECEIVED", "player", player.getName()));

        target.sendMessage(Messages.toComplexComponent(
           "TPA_HERE.TARGET_ACCEPT",
           "TPA_HERE.TARGET_ACCEPT_HOVER",
           "/tpaccept " + player.getName()));

    }

}
