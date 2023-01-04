package me.wholesome_seal.jasonbourne.command.catacomb;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.SubCommand;
import me.wholesome_seal.jasonbourne.command.CatacombManager;
import me.wholesome_seal.jasonbourne.function.SenderMessage;
import me.wholesome_seal.jasonbourne.tasks.LootinTime;

public class End extends SubCommand {
    private JasonBourne plugin;
    @SuppressWarnings("unused")
    private FileConfiguration config;

    public End(JasonBourne plugin, CatacombManager manager) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.name = "end";
        this.description = "End the current run if the runner is already on the reward room";
        this.args = null;

        manager.subCommands.put(this.name, this);
    }
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof BlockCommandSender)) {
            SenderMessage.sendError(sender, "This command must be executed by a command block");
            return false;
        }
        if (!this.plugin.isExecutedOnCorrectWorld(sender)) {
            SenderMessage.sendError(sender, "This command must be executed on the catacombs world");
            return false;
        }
        if (LootinTime.task == null) {
            SenderMessage.sendError(sender, "The runner is not currently on the reward room");
            return false;
        }
        LootinTime.task.cancel();
        LootinTime.runnableTask.run();
        return true;
    }
    
}
