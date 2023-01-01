package me.wholesome_seal.jasonbourne.events.onPlayerJoin;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;

import me.wholesome_seal.jasonbourne.JasonBourne;

public class QuitCatacombsJoin implements Listener {
    private JasonBourne plugin;
    @SuppressWarnings("unused")
    private FileConfiguration config;

    public QuitCatacombsJoin(JasonBourne plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer(); 
        if (!this.plugin.isExecutedOnCorrectWorld(player)) return;
        if (this.plugin.defaultWorld == null) return;

        GameMode playerMode = player.getGameMode();
        if (playerMode.equals(GameMode.CREATIVE) || playerMode.equals(GameMode.SPECTATOR)) return;

        this.plugin.sendPlayerToDefault(player, false);
    }
}
