package me.wholesome_seal.jasonbourne;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class CustomStorage {
    public static File file;
    public static FileConfiguration config;

    public static void setup(JasonBourne plugin) {
        CustomStorage.file = new File(plugin.getDataFolder(), "session_data.yml");

        if (!CustomStorage.file.exists()) {
            try {
                CustomStorage.file.createNewFile();
            } catch (IOException exception) {
                System.out.println("[Catacombs] Error: Failed to generate session file.");
                System.out.println(exception);
                return;
            }
        }

        CustomStorage.config = YamlConfiguration.loadConfiguration(CustomStorage.file);

        CustomStorage.setDefaults();
        CustomStorage.config.options().copyDefaults();
        CustomStorage.save();
        CustomStorage.reload();
    }

    private static void setDefaults() {
        config.addDefault("on-cooldown", new ArrayList<ArrayList<String>>());
        config.addDefault("default-prize", new ArrayList<ItemStack>());
        config.addDefault("prize-pool", new ArrayList<ItemStack>());
    }

    public static void save() {
        try {
            CustomStorage.config.save(CustomStorage.file);
        } catch (IOException exception) {
            System.out.println("[Catacombs] Error: Failed to save data to session file.");
        } 
        reload();
    }

    public static void reload() {
        CustomStorage.config = YamlConfiguration.loadConfiguration(CustomStorage.file);
    }
}
