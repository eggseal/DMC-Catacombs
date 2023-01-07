package me.wholesome_seal.jasonbourne.events.onEntityExplode;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.function.GriefProtection;

public class EntranceExplodeProtection implements Listener {
    private JasonBourne plugin;
    private FileConfiguration config;

    public EntranceExplodeProtection(JasonBourne plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        World eventWorld = event.getEntity().getWorld();
        Location eventLocation = event.getEntity().getLocation();
        double entranceProtectionRadius = (double) this.config.getInt("entrance-protection-radius");

        boolean isWithinRange = GriefProtection.isWithinRange(plugin, eventWorld, eventLocation, entranceProtectionRadius + 10);
        if (isWithinRange) event.setCancelled(true);
    }
}
