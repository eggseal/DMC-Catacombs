package me.wholesome_seal.jasonbourne.events.onPlayerQuit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import me.wholesome_seal.jasonbourne.CustomStorage;
import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.function.DataSetup;
import me.wholesome_seal.jasonbourne.tasks.EndGame;

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

        boolean onCorrectWorld = this.plugin.isExecutedOnCorrectWorld(player);
        boolean isRunner = player.equals(this.plugin.currentPlayer);
        if (!(onCorrectWorld && isRunner && EndGame.task != null)) return;

        List<String> filteredItems = this.config.getStringList("item-filter");
        ArrayList<ItemStack> prizePoolAditions = new ArrayList<ItemStack>();

        player.getInventory().forEach((ItemStack item) -> {
            if (item == null) return;
            
            String itemName = item.getType().getKey().getKey().toUpperCase();
            if (filteredItems.contains(itemName)) return;

            prizePoolAditions.add(item);
        });
        player.getInventory().clear();

        this.plugin.currentPlayer = null;
        if (prizePoolAditions.isEmpty()) return;

        ArrayList<ItemStack> prizePool = DataSetup.getCatacombPrizePool(this.plugin);

        prizePool.addAll(prizePoolAditions);
        CustomStorage.config.set("prize-pool", prizePool);
        CustomStorage.save();
    }
}
