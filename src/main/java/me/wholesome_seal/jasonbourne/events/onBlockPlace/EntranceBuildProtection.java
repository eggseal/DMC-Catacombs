package me.wholesome_seal.jasonbourne.events.onBlockPlace;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.function.DataSetup;

public class EntranceBuildProtection implements Listener {
    private JasonBourne plugin;
    private FileConfiguration config;

    public EntranceBuildProtection(JasonBourne plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        World eventWorld = event.getBlock().getWorld();
        boolean onCorrectWorld = eventWorld.equals(this.plugin.defaultWorld);
        if (!onCorrectWorld) return;

        ArrayList<String> entranceCoords = DataSetup.getEntranceCoordinated(this.plugin);
        double entranceProtectionRadius = (double) this.config.getInt("catacomb-entrance-protection-radius");
        for (String coords : entranceCoords) {
            String[] coordsArray = coords.split("\\s+");
            if (coordsArray.length < 3) continue;

            double X = Double.parseDouble(coordsArray[0]);
            double Y = Double.parseDouble(coordsArray[1]);
            double Z = Double.parseDouble(coordsArray[2]);

            Location location = new Location(eventWorld, X, Y, Z);
            double blockDistance = event.getBlock().getLocation().distance(location);
            if (blockDistance > entranceProtectionRadius) continue;

            event.setCancelled(true);
        }
    }
}
