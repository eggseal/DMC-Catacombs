package me.wholesome_seal.jasonbourne.events.onEntityExplode;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.function.DataSetup;

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
            double blockDistance = event.getEntity().getLocation().distance(location);
            if (blockDistance > entranceProtectionRadius + 10) continue;

            event.setCancelled(true);
        }
    }
}
