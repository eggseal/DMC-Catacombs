package me.wholesome_seal.jasonbourne.function;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import me.wholesome_seal.jasonbourne.CustomStorage;
import me.wholesome_seal.jasonbourne.JasonBourne;

public class DataSetup {
    public static void setCatacombWorld(JasonBourne plugin) {
        plugin.catacombWorld = DataSetup.getWorld(plugin, "catacomb-world-UID");
    }

    public static void setDefaultWorld(JasonBourne plugin) {
        plugin.defaultWorld = DataSetup.getWorld(plugin, "default-world-UID");
    }

    private static World getWorld(JasonBourne plugin, String path) {
        String worldConfig = plugin.getConfig().getString(path);
        try {
            UUID worldUUID = UUID.fromString(worldConfig);
            return Bukkit.getWorld(worldUUID);
        } catch (Exception exception) {
            World world = Bukkit.getWorld(worldConfig);
            if (world == null) System.out.println("[EarthCatacombs] Failed to resolve the world in " + path);
            return world;
        }
    }

    public static ArrayList<String> getItemFilter(JasonBourne plugin) {
        return new ArrayList<String>(plugin.getConfig().getStringList("item-filter"));
    }

    public static long getCooldownTime(JasonBourne plugin) {
        long cooldownLength = plugin.getConfig().getLong("cooldown-length");
        return cooldownLength == 0 ? 86400 : cooldownLength;
    }

    public static ArrayList<ArrayList<String>> getPlayersOnCooldown(JasonBourne plugin) {
        ArrayList<ArrayList<String>> cooldowns;
        try {
            @SuppressWarnings("unchecked")
            ArrayList<ArrayList<String>> cooldownsRaw = (ArrayList<ArrayList<String>>) CustomStorage.config.get("on-cooldown");
            cooldowns = cooldownsRaw == null ? new ArrayList<ArrayList<String>>() : cooldownsRaw;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return new ArrayList<ArrayList<String>>();
        }
        return cooldowns;

    }

    public static ArrayList<String> getEntranceCoordinated(JasonBourne plugin) {
        return new ArrayList<>(plugin.getConfig().getStringList("entrance-list"));
    }

    public static ArrayList<ItemStack> getCatacombPrizePool(JasonBourne plugin) {
        ArrayList<ItemStack> prizePool;
        try {
            @SuppressWarnings("unchecked")
            ArrayList<ItemStack> rawPrizePool = (ArrayList<ItemStack>) CustomStorage.config.get("prize-pool");
            prizePool = rawPrizePool == null ? new ArrayList<ItemStack>() : rawPrizePool;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
        return prizePool;
    }

    public static ArrayList<ItemStack> getDefaultPrizePool(JasonBourne plugin) {
        ArrayList<ItemStack> prizePool;
        try {
            @SuppressWarnings("unchecked")
            ArrayList<ItemStack> rawPrizePool = (ArrayList<ItemStack>) CustomStorage.config.get("default-prize");
            prizePool = rawPrizePool == null ? new ArrayList<ItemStack>() : rawPrizePool;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
        return prizePool;
    }
}
