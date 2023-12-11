package me.jeyzer.utils;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.jeyzer.FoliaTpa;
import me.jeyzer.config.indexed.Config;
import net.kyori.adventure.title.Title;
import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public final class TeleportUtils {

    /*
        TODO: Change practice, this method is cancer.
     */
    private static final Map<UUID, ScheduledTask> teleports = new HashMap<>();

    public static void startCountdownTeleport(Player player, Location toLocation) {
        ScheduledTask task = teleports.get(player.getUniqueId());

        if (task != null)
            task.cancel();

        int seconds = FoliaTpa.getInstance().getConfiguration().find(Config.CONFIG).getInt("teleport-cooldown");

        // Notify the player about the teleport countdown
        sendTeleportCountdownMessage(player, seconds);

        Location location = player.getLocation();
        int initialX = location.getBlockX();
        int initialY = location.getBlockY();
        int initialZ = location.getBlockZ();

        MutableInt cooldown = new MutableInt();

        teleports.put(player.getUniqueId(),
           Bukkit.getServer().getRegionScheduler().runAtFixedRate(FoliaTpa.getInstance(), player.getLocation(), finalTask -> {
               if (!player.isOnline()) {
                   teleports.remove(player.getUniqueId());
                   finalTask.cancel();
                   return;
               }

               // Check if the player moved
               if (hasPlayerMoved(player, initialX, initialY, initialZ)) {
                   handleTeleportFailure(player);
                   finalTask.cancel();
                   teleports.remove(player.getUniqueId());
                   return;
               }

               if (cooldown.intValue() == 0) {
                   handleTeleportSuccess(player, toLocation);
                   finalTask.cancel();
                   teleports.remove(player.getUniqueId());
                   return;
               }

               handleTeleportProgress(player, cooldown.intValue());
               cooldown.decrement();
           }, 0L, 20L));
    }

    private static boolean hasPlayerMoved(Player player, int initialX, int initialY, int initialZ) {
        Location playerLocation = player.getLocation();
        return playerLocation.getBlockX() != initialX || playerLocation.getBlockY() != initialY || playerLocation.getBlockZ() != initialZ;
    }

    private static void sendTeleportCountdownMessage(Player player, int seconds) {
        player.sendMessage(Messages.toComponent("TELEPORT_STARTED", "time", seconds));
    }

    private static void handleTeleportFailure(Player player) {
        Title title = Title.title(
           Messages.toComponent("TELEPORT_FAIL.TITLE"),
           Messages.toComponent("TELEPORT_FAIL.SUBTITLE"));

        player.showTitle(title);
        player.sendMessage(Messages.toComponent("TELEPORT_FAIL.CHAT"));
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
    }

    private static void handleTeleportSuccess(Player player, Location toLocation) {
        player.teleport(toLocation);

        Title title = Title.title(
           Messages.toComponent("TELEPORT_SUCCESS.TITLE"),
           Messages.toComponent("TELEPORT_SUCCESS.SUBTITLE"));

        player.showTitle(title);
        player.playSound(toLocation, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }

    private static void handleTeleportProgress(Player player, int cooldown) {
        Title title = Title.title(
           Messages.toComponent("TELEPORT_PROGRESS.TITLE"),
           Messages.toComponent("TELEPORT_PROGRESS.SUBTITLE", "timeleft", cooldown));

        player.showTitle(title);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        player.sendMessage(Messages.toComponent("TELEPORT_PROGRESS.CHAT", "timeleft", cooldown));
    }
}