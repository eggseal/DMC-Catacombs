package me.wholesome_seal.jasonbourne.command.catacomb;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.SubCommand;
import me.wholesome_seal.jasonbourne.command.CatacombManager;
import me.wholesome_seal.jasonbourne.function.SenderMessage;
import me.wholesome_seal.jasonbourne.tasks.EndGame;
import me.wholesome_seal.jasonbourne.tasks.LootinTime;
import net.md_5.bungee.api.ChatColor;

public class Winner implements SubCommand {
    public String name = "winner";
    public String description = "Display the prize pool GUI to the current Catacombs player";
    public String syntax = "/catacomb winner";

    private JasonBourne plugin;
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
            SenderMessage.sendError(sender, "This command must be executed by a command block.");
            return false;
        }

        if (EndGame.task != null) {
            EndGame.task.cancel();
        }
        
        Player player = this.plugin.currentPlayer;
        if (player == null) {
            SenderMessage.sendError(sender, "Could not find a valid Player");
            return false;
        }

        int lootDuration = this.config.getInt("catacomb-loot-length");
        lootDuration = lootDuration > 0 ? lootDuration : 900;
        new LootinTime(this.plugin).runTaskLater(this.plugin, (long) lootDuration * 20);
        
        Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " has defeated the catacombs");
        SenderMessage.sendPrivate(player, "You now have " + lootDuration + " seconds to collect your reward");
        return true;
    }
}
