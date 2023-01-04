package me.wholesome_seal.jasonbourne.function;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.wholesome_seal.jasonbourne.CustomStorage;
import me.wholesome_seal.jasonbourne.JasonBourne;

public class PrizeDisplay {
    public static String inventoryName = "Catacomb Rewards";
    private static JasonBourne plugin;

    private static String nameDecor = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "" + ChatColor.ITALIC;
    public static String prevPageName = PrizeDisplay.nameDecor + "Previous Page";
    public static String nextPageName = PrizeDisplay.nameDecor + "Next Page";

    private static ItemStack prevPage;
    private static ItemStack nextPage;
    private static ItemStack empty;

    private static int currentPage = 0;
    private static ArrayList<ItemStack> fullItemList = new ArrayList<ItemStack>();
    private static ArrayList<ItemStack> defaultItemList = new ArrayList<ItemStack>();
    private static ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();

    public static void setup(JasonBourne mainPlugin) {
        String customKeyName = "prize-page-handler";
        NamespacedKey customKey = new NamespacedKey(mainPlugin, customKeyName);

        prevPage = new ItemStack(Material.GRAY_DYE, 1);
        ItemMeta prevPageMeta = PrizeDisplay.prevPage.getItemMeta();
        prevPageMeta.setDisplayName(PrizeDisplay.prevPageName);
        prevPageMeta.getPersistentDataContainer().set(customKey, PersistentDataType.BYTE, (byte) 1);
        PrizeDisplay.prevPage.setItemMeta(prevPageMeta);

        nextPage = new ItemStack(Material.GRAY_DYE, 1);
        ItemMeta nextPageMeta = nextPage.getItemMeta();
        nextPageMeta.setDisplayName(PrizeDisplay.nextPageName);
        nextPageMeta.getPersistentDataContainer().set(customKey, PersistentDataType.BYTE, (byte) 1);
        PrizeDisplay.nextPage.setItemMeta(nextPageMeta);

        empty = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.setDisplayName(" ");
        emptyMeta.getPersistentDataContainer().set(customKey, PersistentDataType.BYTE, (byte) 1);
        PrizeDisplay.empty.setItemMeta(emptyMeta);

        plugin = mainPlugin;
        PrizeDisplay.updateRewards();
    }

    public static void updateRewards() {
        defaultItemList = DataSetup.getDefaultPrizePool(PrizeDisplay.plugin);
        itemList = DataSetup.getCatacombPrizePool(PrizeDisplay.plugin);
    
        fullItemList = new ArrayList<ItemStack>();
        fullItemList.addAll(defaultItemList);
        fullItemList.addAll(itemList);
    }

    public static void displayInventory(int page) {
        Player player = PrizeDisplay.plugin.currentPlayer;
        if (player == null) return;

        Inventory GUI = Bukkit.createInventory(player, 54, PrizeDisplay.inventoryName);
        for (int i = 0; i <= 53; GUI.setItem(i++, empty));

        int startIndex = 45 * page;
        int maxIndex = fullItemList.size() < 45 + startIndex ? fullItemList.size() : 45 + startIndex;

        for (int i = startIndex; i < maxIndex; i++) {
            int inventoryIndex = i - startIndex;
            if (inventoryIndex < 0 || inventoryIndex > 44) break;

            ItemStack item = fullItemList.get(i);
            if (item == null) continue;
            GUI.setItem(inventoryIndex, item);
        }

        GUI.setItem(45, PrizeDisplay.prevPage);
        GUI.setItem(53, PrizeDisplay.nextPage);

        currentPage = page;
        player.openInventory(GUI);
    }

    public static void deleteOnFullItemList(int index) {
        fullItemList.set(getAbsoluteIndex(index), null);
        displayInventory(currentPage);
    }

    public static int getAbsoluteIndex(int inventoryIndex) {
        return inventoryIndex + (currentPage * 45);
    }

    public static void updateConfigPrize() {
        ArrayList<ItemStack> newList = new ArrayList<ItemStack>(fullItemList.subList(defaultItemList.size(), fullItemList.size()));
        newList.removeIf((ItemStack item) -> item == null);
        CustomStorage.config.set("prize-pool", newList);
        CustomStorage.save();
    }

    public static void nextPage() {
        int newPage = currentPage + 1;
        int maxPages = (int) Math.ceil(fullItemList.size() / 45f);
        
        if (newPage >= maxPages) newPage = maxPages - 1;
        if (newPage < 0) newPage = 0;

        PrizeDisplay.displayInventory(newPage);
    }

    public static void previousPage() {
        int newPage = currentPage - 1;
        if (newPage < 0) newPage = 0;
        
        PrizeDisplay.displayInventory(newPage);
    }
}
