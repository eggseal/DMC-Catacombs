package me.wholesome_seal.jasonbourne.function;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import me.wholesome_seal.jasonbourne.JasonBourne;

public class DataSetup {
    public static void setCatacombWorld(JasonBourne plugin) {
        String multiverseWorldUID = plugin.getConfig().getString("catacomb-world-UID");
        try {
            plugin.catacombWorld = Bukkit.getWorld(UUID.fromString(multiverseWorldUID));
        } catch (Exception exception) {
            plugin.catacombWorld = null;
        }
    }

    public static void setDefaultWorld(JasonBourne plugin) {
        String defaultWorldUID = plugin.getConfig().getString("default-world-UID");
        try {
            plugin.defaultWorld = Bukkit.getWorld(UUID.fromString(defaultWorldUID));
        } catch (Exception exception) {
            plugin.defaultWorld = null;
        }
    }

    public static long getCooldownTime(JasonBourne plugin) {
        long cooldownLength = plugin.getConfig().getLong("catacomb-cooldown-length");
        return cooldownLength == 0 ? 86400 : cooldownLength;
    }

    public static ArrayList<ArrayList<String>> getPlayersOnCooldown(JasonBourne plugin) {
        ArrayList<ArrayList<String>> cooldowns;
        try {
            @SuppressWarnings("unchecked")
            ArrayList<ArrayList<String>> cooldownsRaw = (ArrayList<ArrayList<String>>) plugin.getConfig().get("catacomb-on-cooldown");
            cooldowns = cooldownsRaw == null ? new ArrayList<ArrayList<String>>() : cooldownsRaw;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return new ArrayList<ArrayList<String>>();
        }
        return cooldowns;

    }

    public static ArrayList<ItemStack> getCatacombPrizePool(JasonBourne plugin) {
        ArrayList<ItemStack> prizePool;
        try {
            @SuppressWarnings("unchecked")
            ArrayList<ItemStack> rawPrizePool = (ArrayList<ItemStack>) plugin.getConfig().get("catacomb-default-prize");
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
            ArrayList<ItemStack> rawPrizePool = (ArrayList<ItemStack>) plugin.getConfig().get("catacomb-default-prize");
            prizePool = rawPrizePool == null ? new ArrayList<ItemStack>() : rawPrizePool;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
        return prizePool;
    }
}
