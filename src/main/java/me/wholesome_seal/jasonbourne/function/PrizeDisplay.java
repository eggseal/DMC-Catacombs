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

import me.wholesome_seal.jasonbourne.JasonBourne;

public class PrizeDisplay {
    public static String inventoryName = "Catacomb Rewards";

    private static String nameDecor = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "" + ChatColor.ITALIC;
    public static String prevPageName = PrizeDisplay.nameDecor + "Previous Page";
    public static String nextPageName = PrizeDisplay.nameDecor + "Next Page";

    private static ItemStack prevPage;
    private static ItemStack nextPage;

    public static void setup(JasonBourne plugin) {
        String customKeyName = "prize-page-handler";
        NamespacedKey customKey = new NamespacedKey(plugin, customKeyName);

        PrizeDisplay.prevPage = new ItemStack(Material.GRAY_DYE, 1);
        ItemMeta prevPageMeta = PrizeDisplay.prevPage.getItemMeta();
        prevPageMeta.setDisplayName(PrizeDisplay.prevPageName);
        prevPageMeta.getPersistentDataContainer().set(customKey, PersistentDataType.BYTE, (byte) 1);
        PrizeDisplay.prevPage.setItemMeta(prevPageMeta);

        PrizeDisplay.nextPage = new ItemStack(Material.GRAY_DYE, 1);
        ItemMeta nextPageMeta = nextPage.getItemMeta();
        nextPageMeta.setDisplayName(PrizeDisplay.nextPageName);
        nextPageMeta.getPersistentDataContainer().set(customKey, PersistentDataType.BYTE, (byte) 1);
        nextPage.setItemMeta(nextPageMeta);
    }

    public static void displayInventory(JasonBourne plugin, int page) {
        ArrayList<ItemStack> defaultItemList = DataSetup.getDefaultPrizePool(plugin);
        ArrayList<ItemStack> itemList = DataSetup.getCatacombPrizePool(plugin);

        ArrayList<ItemStack> fullItemList = new ArrayList<ItemStack>();
        fullItemList.addAll(defaultItemList);
        fullItemList.addAll(itemList);

        Player player = plugin.currentPlayer;
        if (player == null) return;

        Inventory GUI = Bukkit.createInventory(player, 54, PrizeDisplay.inventoryName);

        if (!itemList.isEmpty()) {
            int startIndex = 45 * page;
            int maxIndex = fullItemList.size() < 45 + startIndex ? fullItemList.size() : 44 + startIndex;
    
            try {
                GUI.addItem(fullItemList.subList(startIndex, maxIndex).toArray(new ItemStack[0]));
            } catch (IndexOutOfBoundsException exception) {
                System.out.println(exception);
                return;
            }
        }

        GUI.setItem(45, PrizeDisplay.prevPage);
        GUI.setItem(53, PrizeDisplay.nextPage);

        player.openInventory(GUI);
    }
}
