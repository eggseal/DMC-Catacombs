package me.wholesome_seal.jasonbourne.command.catacomb;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.SubCommand;
import me.wholesome_seal.jasonbourne.command.CatacombManager;
import me.wholesome_seal.jasonbourne.function.PrizeDisplay;
import me.wholesome_seal.jasonbourne.function.SenderMessage;
import me.wholesome_seal.jasonbourne.tasks.LootinTime;

public class DisplayReward extends SubCommand {
    private JasonBourne plugin;
    @SuppressWarnings("unused")
    private FileConfiguration config;

    public DisplayReward(JasonBourne plugin, CatacombManager manager) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.name = "display_reward";
        this.description = "Display the accumulated rewards as a chest GUI to the catacomb runner";
        this.args = null;

        manager.subCommands.put(this.name, this);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof BlockCommandSender)) {
            SenderMessage.sendError(sender, "This command must be executed by a command block.");
            return false;
        }
        if (!this.plugin.isExecutedOnCorrectWorld(sender)) {
            SenderMessage.sendError(sender, "This command must be executed on the catacombs world");
            return false;
        }
        if (this.plugin.currentPlayer == null) {
            SenderMessage.sendError(sender, "There is not a player currently recognized as a runner");
            return false;
        }
        if (LootinTime.task == null) {
            SenderMessage.sendError(sender, "The runner cannot access the rewards before '/catacombs winner' has been run");
            return false;
        }

        PrizeDisplay.displayInventory(0);
        return true;
    }
    
}
