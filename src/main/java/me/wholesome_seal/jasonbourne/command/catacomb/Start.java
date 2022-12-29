package me.wholesome_seal.jasonbourne.command.catacomb;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.SubCommand;
import me.wholesome_seal.jasonbourne.command.CatacombManager;

public class Start implements SubCommand {
    public String name = "start";
    public String description = "Send the nearest survival player to the Catacombs and setup all the necessary config";
    public String syntax = "/catacomb start";

    private JasonBourne plugin;
    @SuppressWarnings("unused")
    private FileConfiguration config;

    public Start(JasonBourne plugin, CatacombManager manager) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        manager.subCommands.put(this.name, this);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof BlockCommandSender)) {
            sender.sendMessage(ChatColor.GREEN + "[Catacomb] " + ChatColor.RED + "This command must be executed by a command block.");
            return false;
        }

        BlockCommandSender commandBlock = (BlockCommandSender) sender;
        World currentWorld = commandBlock.getBlock().getWorld();
        Location commandBlockLocation = commandBlock.getBlock().getLocation();

        double lastPlayerDistance = Double.MAX_VALUE;
        Player resultPlayer = null;

        for (Player player : currentWorld.getPlayers()) {
            Location playerLocation = player.getLocation();
            double playerDistance = playerLocation.distance(commandBlockLocation);

            if (playerDistance < lastPlayerDistance) {
                lastPlayerDistance = playerDistance;
                resultPlayer = player;
            }
        }

        if (resultPlayer == null) {
            String errorMessage = ChatColor.GREEN + "[Catacomb] " + ChatColor.RED + "No avilable player was found.";
            commandBlock.sendMessage(errorMessage);
            return false;
        }

        plugin.currentPlayer = resultPlayer;
        String commandLine = "mvtp " + resultPlayer.getName() + " " + plugin.catacombWorld.getName();
        Bukkit.dispatchCommand(commandBlock, commandLine);

        String broadcast = resultPlayer.getName() + " has entered the Catacombs";
        Bukkit.broadcastMessage(broadcast);
        return true;
    }
    
}
