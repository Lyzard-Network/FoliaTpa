package me.jeyzer;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import me.jeyzer.commands.TpAcceptAikar;
import me.jeyzer.commands.TpaAikar;
import me.jeyzer.commands.TpahereAikar;
import me.jeyzer.config.Configuration;
import me.jeyzer.handler.TeleportRequests;
import me.jeyzer.listener.TeleportListener;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class FoliaTpa extends JavaPlugin {

    @Getter private static FoliaTpa instance;

    private Configuration configuration;
    private TeleportRequests requests;

    @Override
    public void onEnable() {
        instance = this;
        requests = new TeleportRequests();
        configuration = new Configuration(this);
        getServer().getPluginManager().registerEvents(new TeleportListener(requests), this);
    }

    private void registerCommands() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new TpaAikar(requests));
        manager.registerCommand(new TpahereAikar(requests));
        manager.registerCommand(new TpAcceptAikar(requests));
    }

    @Override
    public void onDisable() {
        instance = null;
    }

}