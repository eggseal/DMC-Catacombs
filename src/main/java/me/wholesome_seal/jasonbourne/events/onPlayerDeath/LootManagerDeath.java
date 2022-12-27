package me.wholesome_seal.jasonbourne.events.onPlayerDeath;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.wholesome_seal.jasonbourne.JasonBourne;

public class LootManagerDeath implements Listener {
    JasonBourne plugin;
    FileConfiguration config;

    public LootManagerDeath(JasonBourne plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        boolean onCorrectWorld = this.plugin.isExecutedOnCorrectWorld(event.getEntity());
        if (!onCorrectWorld) return;

        List<String> filteredItems = this.config.getStringList("catacomb-item-filter");
        List<ItemStack> droppedItems = event.getDrops();

        ArrayList<String[]> prizePoolAditions = new ArrayList<String[]>();
        for (ItemStack item : droppedItems) {
            NamespacedKey itemKey = item.getType().getKey();
            String itemName = itemKey.getNamespace() + ":" + itemKey.getKey();
            String itemAmount = filteredItems.contains(itemName) ? "0" : Integer.toString(item.getAmount());

            if (itemAmount.equals("0")) continue;
            String[] newItem = {itemName, itemAmount};
            prizePoolAditions.add(newItem);
        }

        if (droppedItems.isEmpty()) {
            System.out.println("No items to add to the pool.");
            return;
        }

        event.getDrops().clear();

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
