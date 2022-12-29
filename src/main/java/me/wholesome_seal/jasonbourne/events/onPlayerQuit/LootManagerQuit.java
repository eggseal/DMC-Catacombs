package me.wholesome_seal.jasonbourne.events.onPlayerQuit;

import java.util.ArrayList;
import java.util.List;

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

        boolean onCorrectWorld = this.plugin.isExecutedOnCorrectWorld(player);
        boolean isRunner = this.plugin.currentPlayer.equals(player);
        if (!(onCorrectWorld && isRunner)) return;

        List<String> filteredItems = this.config.getStringList("catacomb-item-filter");
        ArrayList<ItemStack> prizePoolAditions = new ArrayList<ItemStack>();

        player.getInventory().forEach((ItemStack item) -> {
            if (item == null) return;
            
            String itemName = item.getType().getKey().getKey().toUpperCase();
            if (filteredItems.contains(itemName)) return;

            prizePoolAditions.add(item);
        });
        player.getInventory().clear();

        if (prizePoolAditions.isEmpty()) return;

        String prizePoolPath = "catacomb-prize-pool";
        ArrayList<ItemStack> prizePool = this.plugin.getCatacombPrizePool();

        prizePool.addAll(prizePoolAditions);
        this.config.set(prizePoolPath, prizePool);
        this.plugin.saveConfig();
    }
}
