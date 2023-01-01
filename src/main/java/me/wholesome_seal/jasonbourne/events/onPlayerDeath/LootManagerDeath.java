package me.wholesome_seal.jasonbourne.events.onPlayerDeath;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.function.DataSetup;

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
        boolean isRunner = event.getEntity().equals(this.plugin.currentPlayer);
        if (!(onCorrectWorld && isRunner)) return;

        List<String> filteredItems = this.config.getStringList("catacomb-item-filter");
        ArrayList<ItemStack> droppedItems = (ArrayList<ItemStack>) event.getDrops();

        droppedItems.removeIf((ItemStack item) -> {
            String itemKey = item.getType().getKey().getKey();
            return filteredItems.contains(itemKey.toUpperCase());
        });

        if (droppedItems.isEmpty()) return;

        String prizePoolPath = "catacomb-prize-pool";
        ArrayList<ItemStack> prizePool = DataSetup.getCatacombPrizePool(this.plugin);

        prizePool.addAll(droppedItems);
        this.config.set(prizePoolPath, prizePool);
        this.plugin.saveConfig();

        event.getDrops().clear();
    }
}
