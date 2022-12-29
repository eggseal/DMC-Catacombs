package me.wholesome_seal.jasonbourne.command.catacomb;

import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.SubCommand;
import me.wholesome_seal.jasonbourne.command.CatacombManager;
import me.wholesome_seal.jasonbourne.function.PrizeDisplay;

public class Winner implements SubCommand {
    public String name = "winner";
    public String description = "Display the prize pool GUI to the current Catacombs player";
    public String syntax = "/catacomb winner";

    private JasonBourne plugin;
    @SuppressWarnings("unused")
    private FileConfiguration config;

    public Winner(JasonBourne plugin, CatacombManager manager) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        manager.subCommands.put(this.name, this);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        boolean onCorrectWorld = this.plugin.isExecutedOnCorrectWorld(sender);
        if (!(onCorrectWorld && sender instanceof BlockCommandSender)) {
            sender.sendMessage(ChatColor.GREEN + "[Catacomb] " + ChatColor.RED + "This command must be executed by a command block.");
            return false;
        }
        
        BlockCommandSender commandBlock = (BlockCommandSender) sender;
        Player player = this.plugin.currentPlayer;

        if (player == null) {
            String errorMessage = "Could not find a valid Player";            
            commandBlock.sendMessage(ChatColor.GREEN + "[Catacomb] " + ChatColor.RED + errorMessage);
            return false;
        }

        PrizeDisplay.displayInventory(this.plugin, 0);
        return true;
    }
}
