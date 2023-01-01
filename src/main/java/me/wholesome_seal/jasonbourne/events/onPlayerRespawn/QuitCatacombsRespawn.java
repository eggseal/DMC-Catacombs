package me.wholesome_seal.jasonbourne.events.onPlayerRespawn;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.wholesome_seal.jasonbourne.JasonBourne;

public class QuitCatacombsRespawn implements Listener {
    private JasonBourne plugin;
    @SuppressWarnings("unused")
    private FileConfiguration config;

    public QuitCatacombsRespawn(JasonBourne plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }
    
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().equals(this.plugin.catacombWorld)) return;

        GameMode playerMode = player.getGameMode();
        if (playerMode.equals(GameMode.CREATIVE) || playerMode.equals(GameMode.SPECTATOR)) return;
        
        JasonBourne plugin = this.plugin;
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                plugin.sendPlayerToDefault(player, true);
            }
        };
        task.runTaskLater(this.plugin, 1);
    }
}
