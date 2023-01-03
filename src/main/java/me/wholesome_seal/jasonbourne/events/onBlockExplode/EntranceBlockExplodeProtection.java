package me.wholesome_seal.jasonbourne.events.onBlockExplode;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.function.DataSetup;

public class EntranceBlockExplodeProtection implements Listener {
    private JasonBourne plugin;
    private FileConfiguration config;

    public EntranceBlockExplodeProtection(JasonBourne plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onEntityExplode(BlockExplodeEvent event) {
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
            if (blockDistance > entranceProtectionRadius + 10) continue;

            event.setCancelled(true);
        }
    }
}
