package me.wholesome_seal.jasonbourne.events.onPlayerQuit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import me.wholesome_seal.jasonbourne.JasonBourne;

public class LootManagerQuit implements Listener {
    JasonBourne plugin;
    FileConfiguration config;

    public LootManagerQuit(JasonBourne plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        System.out.println("PLayer Quit");

        boolean onCorrectWorld = this.plugin.isExecutedOnCorrectWorld(player);
        if (!onCorrectWorld) return;

        List<String> filteredItems = this.config.getStringList("catacomb-item-filter");

        ArrayList<String[]> prizePoolAditions = new ArrayList<String[]>();
        player.getInventory().forEach((ItemStack item) -> {
            if (item == null) return;
            
            NamespacedKey itemKey = item.getType().getKey();
            String itemName = itemKey.getNamespace() + ":" + itemKey.getKey();
            String itemAmount = filteredItems.contains(itemName) ? "0" : Integer.toString(item.getAmount());

            if (itemAmount.equals("0")) return;
            String[] newItem = {itemName, itemAmount};
            prizePoolAditions.add(newItem);
        });
        player.getInventory().clear();

        String prizePoolPath = "catacomb-prize-pool";
        List<String[]> prizePool;

        try {
            @SuppressWarnings("unchecked")
            List<String[]> rawPrizePool = (List<String[]>) this.config.get(prizePoolPath);
            prizePool = rawPrizePool;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return;
        }

        prizePool.addAll(prizePoolAditions);
        this.config.set(prizePoolPath, prizePool);
        this.plugin.saveConfig();
    }
}
