package me.wholesome_seal.jasonbourne.command.catacomb;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.wholesome_seal.jasonbourne.CustomStorage;
import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.SubCommand;
import me.wholesome_seal.jasonbourne.command.CatacombManager;
import me.wholesome_seal.jasonbourne.function.DataSetup;
import me.wholesome_seal.jasonbourne.function.SenderMessage;

public class DefaultReward extends SubCommand {
    private JasonBourne plugin;
    @SuppressWarnings("unused")
    private FileConfiguration config;

    public DefaultReward(JasonBourne plugin, CatacombManager manager) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.name = "default_reward";
        this.description = "Add an item to the default reward pool";
        this.args = new ArrayList<>();
        this.args.add(Arrays.asList("add", "remove"));

        manager.subCommands.put(this.name, this);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            SenderMessage.sendError(sender, "This command can only be executed by players");
            return false;
        }

        if (args.length < 2) {
            SenderMessage.sendError(sender, "Invalid arguments");
            return false;
        }

        Player player = (Player) sender;
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        String itemName = heldItem.getType().getKey().getKey().toUpperCase();
        
        if (heldItem.getType().isAir()) {
            SenderMessage.sendError(sender, "Air is not a valid reward item");
            return false;
        }
        
        ArrayList<ItemStack> defaultItemList = DataSetup.getDefaultPrizePool(this.plugin);
        ArrayList<String> filteredItems = DataSetup.getItemFilter(this.plugin);
        switch(args[1]) {
            case "add": {
                if (filteredItems.contains(itemName)) {
                    SenderMessage.sendError(player, "This item is not allowed on the reward pool");
                    return false;
                }
                defaultItemList.add(heldItem);
                SenderMessage.sendSuccess(player, "Added your " + itemName + " with all its data to the reward pool");
                break;
            }
            case "remove": {
                if (!defaultItemList.contains(heldItem)) {
                    SenderMessage.sendError(sender, "There is not such an item in the reward pool");
                    return false;
                }
                defaultItemList.remove(heldItem);
                SenderMessage.sendSuccess(player, "Removed " + itemName + " from the default reward pool");
                break;
            }
            default: {
                SenderMessage.sendError(sender, "Invalid arguments");
                return false;
            }
        }
        
        CustomStorage.config.set("catacomb-default-prize", defaultItemList);
        CustomStorage.save();
        return true;
    }
    
}
