package me.wholesome_seal.jasonbourne.command.catacomb;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.SubCommand;
import me.wholesome_seal.jasonbourne.command.CatacombManager;
import me.wholesome_seal.jasonbourne.function.DataSetup;
import me.wholesome_seal.jasonbourne.function.SenderMessage;

public class Filter extends SubCommand {
    private JasonBourne plugin;
    private FileConfiguration config;

    public Filter(JasonBourne plugin, CatacombManager manager) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.name = "filter";
        this.description = "Add an item to the catacomb reward filter";
        this.args = new ArrayList<>();
        this.args.add(Arrays.asList("add", "remove"));

        manager.subCommands.put(this.name, this);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            SenderMessage.sendError(sender, "This command must be executed by a Player");
            return false;
        }

        if (args.length < 2) {
            SenderMessage.sendError(sender, "Invalid arguments");
            return false;
        }

        Player player = (Player) sender;
        ItemStack itemHeld = player.getInventory().getItemInMainHand();
        String heldItemName = itemHeld.getType().getKey().getKey().toUpperCase();
        if (itemHeld.getType().isAir()) {
            SenderMessage.sendError(player, "Air is not a valid item");
            return false;
        }

        ArrayList<String> itemFilter = DataSetup.getItemFilter(this.plugin);
        boolean isItemOnFilter = itemFilter.contains(heldItemName);
        switch(args[1]) {
            case "add": {
                if (isItemOnFilter) {
                    SenderMessage.sendError(player, "This item is already being filtered");
                    return false;
                }
                itemFilter.add(heldItemName);
                SenderMessage.sendSuccess(player, "Added " + heldItemName + " to the item filter");
                break;
            }
            case "remove": {
                if (!isItemOnFilter) {
                    SenderMessage.sendError(player, "This item is not on the item filter");
                    return false;
                }
                itemFilter.remove(heldItemName);
                SenderMessage.sendSuccess(player, "Removed " + heldItemName + " from the item filter");
                break;
            }
            default: {
                SenderMessage.sendError(player, "Invalid arguments");
                return false;
            }
        }
        this.config.set("item-filter", itemFilter);
        this.plugin.saveConfig();
        return true;
    }
}
