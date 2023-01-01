package me.wholesome_seal.jasonbourne.tasks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.function.SenderMessage;

public class LootinTime extends BukkitRunnable {
    private JasonBourne plugin;
    @SuppressWarnings("unused")
    private FileConfiguration config;

    public static BukkitTask task;

    public LootinTime(JasonBourne plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public BukkitTask runTaskLater(Plugin plugin, long delay) {
        EndGame.task = super.runTaskLater(plugin, delay);
        return EndGame.task;
    }
    
    @Override
    public void run() {
        Player player = this.plugin.currentPlayer;
        if (player == null) return;

        LootinTime.task = null;
        this.plugin.currentPlayer = null;
        String playerName = player.getName();

        if (this.plugin.defaultWorld == null) return;
        SenderMessage.sendPrivate(player, "Your time to collect the loot has ended.");

        String command = "mvtp " + playerName + " " + this.plugin.defaultWorld.getName();
        Bukkit.dispatchCommand(this.plugin.console, command);
    }
}
