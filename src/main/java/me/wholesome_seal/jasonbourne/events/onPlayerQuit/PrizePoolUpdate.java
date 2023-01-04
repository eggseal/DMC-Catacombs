package me.wholesome_seal.jasonbourne.events.onPlayerQuit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.function.PrizeDisplay;
import me.wholesome_seal.jasonbourne.tasks.LootinTime;

public class PrizePoolUpdate implements Listener {
    private JasonBourne plugin;
    @SuppressWarnings("unused")
    private FileConfiguration config;

    public PrizePoolUpdate(JasonBourne plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }
    
    @EventHandler 
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (this.plugin.currentPlayer == null) return;
        if (this.plugin.currentPlayer != event.getPlayer()) return;
        if (!this.plugin.isExecutedOnCorrectWorld(event.getPlayer())) return;
        if (LootinTime.task == null) return;

        System.out.println("Player left on prize room");
        this.plugin.currentPlayer = null;
        LootinTime.task.cancel();
        LootinTime.task = null;

        PrizeDisplay.updateConfigPrize();
    }
}
