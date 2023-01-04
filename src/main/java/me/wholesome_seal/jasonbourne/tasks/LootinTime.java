package me.wholesome_seal.jasonbourne.tasks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.function.PrizeDisplay;
import me.wholesome_seal.jasonbourne.function.SenderMessage;

public class LootinTime extends BukkitRunnable {
    private JasonBourne plugin;
    @SuppressWarnings("unused")
    private FileConfiguration config;

    public static BukkitTask task;
    public static BukkitRunnable runnableTask;

    public LootinTime(JasonBourne plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public BukkitTask runTaskLater(Plugin plugin, long delay) {
        task = super.runTaskLater(plugin, delay);
        runnableTask = this;
        return EndGame.task;
    }
    
    @Override
    public void run() {
        task = null;
        runnableTask = null;
        PrizeDisplay.updateConfigPrize();

        Player player = this.plugin.currentPlayer;
        if (player == null) return;

        this.plugin.currentPlayer = null;
        String playerName = player.getName();

        SenderMessage.sendPrivate(player, "Your time to collect the loot has ended.");
        if (this.plugin.defaultWorld == null) return;

        String command = "mvtp " + playerName + " " + this.plugin.defaultWorld.getName();
        Bukkit.dispatchCommand(this.plugin.console, command);
    }
}
