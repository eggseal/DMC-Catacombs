package me.wholesome_seal.jasonbourne.tasks;

import java.util.ArrayList;
import java.util.Date;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.wholesome_seal.jasonbourne.CustomStorage;
import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.function.DataSetup;

public class CooldownManager extends BukkitRunnable {
    private JasonBourne plugin;
    public static BukkitTask task;

    public CooldownManager(JasonBourne plugin) {
        this.plugin = plugin;
    }

    @Override
    public BukkitTask runTaskTimer(Plugin plugin, long delay, long period) {
        CooldownManager.task = super.runTaskTimer(plugin, delay, period);
        return CooldownManager.task;
    }

    @Override
    public void run() {
        ArrayList<ArrayList<String>> cooldowns = DataSetup.getPlayersOnCooldown(this.plugin);
        cooldowns.removeIf((ArrayList<String> cooldown) -> {
            String playerEntry;
            try {
                playerEntry = cooldown.get(1);
            } catch (IndexOutOfBoundsException exception) {
                return true;
            }
            long cooldownLength = DataSetup.getCooldownTime(this.plugin) * 1000;

            long playerEntryTime = Long.valueOf(playerEntry);
            long currentTime = new Date().getTime();
            long passedTime = currentTime - playerEntryTime;
            return passedTime >= cooldownLength;
        });
        CustomStorage.config.set("catacomb-on-cooldown", cooldowns);
        CustomStorage.save();
    }
}