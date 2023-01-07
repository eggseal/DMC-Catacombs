package me.wholesome_seal.jasonbourne.tasks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.wholesome_seal.jasonbourne.JasonBourne;
import net.md_5.bungee.api.ChatColor;

public class EndGame extends BukkitRunnable {
    private JasonBourne plugin;
    @SuppressWarnings("unused")
    private FileConfiguration config;

    public static BukkitTask task;
    public static BukkitRunnable instance;

    public EndGame(JasonBourne plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public BukkitTask runTaskLater(Plugin plugin, long delay) {
        task = super.runTaskLater(plugin, delay);
        instance = this;
        return EndGame.task;
    }

    @Override
    public void run() {
        Player player = this.plugin.currentPlayer;
        if (player == null) return;

        task = null;
        String playerName = player.getName();

        Bukkit.broadcastMessage(ChatColor.RED + playerName + " failed to complete the catacombs");
        player.setHealth(0);

        if (player.getWorld().equals(this.plugin.catacombWorld)) return;
        this.plugin.currentPlayer = null;
    }
}
