package me.wholesome_seal.jasonbourne.events.onBlockBreak;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.function.GriefProtection;

public class EntranceBreakProtection implements Listener {
    private JasonBourne plugin;
    private FileConfiguration config;

    public EntranceBreakProtection(JasonBourne plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        GameMode gamemode = event.getPlayer().getGameMode();
        if (!gamemode.equals(GameMode.SURVIVAL)) return;
        
        World eventWorld = event.getBlock().getWorld();
        Location eventLocation = event.getBlock().getLocation();
        double entranceProtectionRadius = (double) this.config.getInt("entrance-protection-radius");

        boolean isWithinRange = GriefProtection.isWithinRange(plugin, eventWorld, eventLocation, entranceProtectionRadius);
        if (isWithinRange) event.setCancelled(true);
    }
}
