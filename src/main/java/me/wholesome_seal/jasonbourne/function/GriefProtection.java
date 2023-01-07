package me.wholesome_seal.jasonbourne.function;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;

import me.wholesome_seal.jasonbourne.JasonBourne;

public class GriefProtection {
    public static boolean isWithinRange(JasonBourne plugin, World world, Location location, double radius) {
        boolean onCorrectWorld = world.equals(plugin.defaultWorld);
        if (!onCorrectWorld) return false;

        ArrayList<String> entranceCoords = DataSetup.getEntranceCoordinated(plugin);
        for (String coords : entranceCoords) {
            String[] coordsArray = coords.split("\\s+");
            if (coordsArray.length < 3) continue;

            double X = Double.parseDouble(coordsArray[0]);
            double Y = Double.parseDouble(coordsArray[1]);
            double Z = Double.parseDouble(coordsArray[2]);

            Location entranceLocation = new Location(world, X, Y, Z);
            double blockDistance = location.distance(entranceLocation);
            if (blockDistance > radius) continue;

            return true;
        }
        return false;
    }
}
