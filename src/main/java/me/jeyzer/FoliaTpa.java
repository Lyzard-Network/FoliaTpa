package me.jeyzer;

import lombok.Getter;
import me.jeyzer.commands.TpaCommand;
import me.jeyzer.commands.TpacceptCommand;
import me.jeyzer.commands.TpahereCommand;
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
        registerCommands();
        getServer().getPluginManager().registerEvents(new TeleportListener(requests), this);
    }

    private void registerCommands() {
        getCommand("tpa").setExecutor(new TpaCommand(requests));
        getCommand("tpaccept").setExecutor(new TpacceptCommand(requests));
        getCommand("tpahere").setExecutor(new TpahereCommand(requests));
    }

    @Override
    public void onDisable() {
        instance = null;
    }

}