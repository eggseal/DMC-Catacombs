package me.wholesome_seal.jasonbourne.events.onInventoryClick;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.function.PrizeDisplay;

public class LootMenuHandler implements Listener {
    private JasonBourne plugin;
    @SuppressWarnings("unused")
    private FileConfiguration config;

    private NamespacedKey customKey;

    public LootMenuHandler(JasonBourne plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);

        String customKeyName = "prize-page-handler";
        this.customKey = new NamespacedKey(this.plugin, customKeyName);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(PrizeDisplay.inventoryName)) return;
        if (event.getClickedInventory() != event.getView().getTopInventory()) return;

        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        
        event.setCancelled(true);

        ItemMeta itemData = item.getItemMeta();
        PersistentDataContainer dataContainer = item.getItemMeta().getPersistentDataContainer();
        Byte customKeyData = dataContainer.get(customKey, PersistentDataType.BYTE);

        if (customKeyData != null) {
            if (itemData.getDisplayName().equals(PrizeDisplay.prevPageName)) {
                PrizeDisplay.previousPage();
            }
            if (itemData.getDisplayName().equals(PrizeDisplay.nextPageName)) {
                PrizeDisplay.nextPage();
            }
            return;
        }

        int emptySlot = event.getView().getBottomInventory().firstEmpty();
        if (emptySlot == -1) return;
        
        event.getView().getBottomInventory().setItem(emptySlot, item);
        PrizeDisplay.deleteOnFullItemList(event.getSlot());
    }
}
