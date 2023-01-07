package me.wholesome_seal.jasonbourne.command.catacomb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.wholesome_seal.jasonbourne.CustomStorage;
import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.SubCommand;
import me.wholesome_seal.jasonbourne.command.CatacombManager;
import me.wholesome_seal.jasonbourne.function.DataSetup;
import me.wholesome_seal.jasonbourne.function.SenderMessage;
import me.wholesome_seal.jasonbourne.tasks.EndGame;

public class Start extends SubCommand {
    private JasonBourne plugin;
    private FileConfiguration config;

    public Start(JasonBourne plugin, CatacombManager manager) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.name = "start";
        this.description = "Send the nearest survival player to the Catacombs and setup all the necessary config";
        this.args = null;

        manager.subCommands.put(this.name, this);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof BlockCommandSender || sender instanceof ConsoleCommandSender)) {
            SenderMessage.sendError(sender, "This command must be executed by a command block.");
            return false;
        }
        if (this.plugin.catacombWorld == null) {
            SenderMessage.sendError(sender, "Failed to resolve the given world in catacomb-world-UID");
            return false;
        }

        Player resultPlayer = null;
        if (sender instanceof BlockCommandSender) {
            BlockCommandSender commandBlock = (BlockCommandSender) sender;
            World currentWorld = commandBlock.getBlock().getWorld();
            Location commandBlockLocation = commandBlock.getBlock().getLocation();
            double lastPlayerDistance = Double.MAX_VALUE;
    
            for (Player player : currentWorld.getPlayers()) {
                Location playerLocation = player.getLocation();
                double playerDistance = playerLocation.distance(commandBlockLocation);
    
                if (playerDistance < lastPlayerDistance) {
                    lastPlayerDistance = playerDistance;
                    resultPlayer = player;
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length < 2) {
                SenderMessage.sendError(sender, "Missing arguments. Usage: /catacomb start <player>");
                return false;
            }
            resultPlayer = Bukkit.getPlayer(args[1]);
        }

        if (resultPlayer == null) {
            SenderMessage.sendError(sender, "No avilable player was found.");;
            return false;
        }

        GameMode playerMode = resultPlayer.getGameMode();
        if (playerMode.equals(GameMode.CREATIVE) || playerMode.equals(GameMode.SPECTATOR)) {
            String commandLine = "mvtp " + resultPlayer.getName() + " " + plugin.catacombWorld.getName();
            Bukkit.dispatchCommand(this.plugin.console, commandLine);
            return true;
        }

        if (this.plugin.currentPlayer != null) {
            String message = "The catacombs are currently occupied by " + this.plugin.currentPlayer.getName() + ", come back later.";
            SenderMessage.sendPrivate(resultPlayer, message);
            return false;
        }

        ArrayList<ArrayList<String>> cooldowns = DataSetup.getPlayersOnCooldown(this.plugin);
        String playerUID = resultPlayer.getUniqueId().toString();
        boolean isOnCooldown = cooldowns.stream().anyMatch((ArrayList<String> playerCooldowns) -> {
            if (playerCooldowns.size() == 0) return false;
            return playerCooldowns.get(0).equals(playerUID);
        });
        if (isOnCooldown) {
            SenderMessage.sendPrivate(resultPlayer, "You have already wasted your attempt, come back later.");
            SenderMessage.sendError(sender, "The selected player is on cooldown");
            return false;
        }

        String commandLine = "mvtp " + resultPlayer.getName() + " " + plugin.catacombWorld.getName();
        Bukkit.dispatchCommand(this.plugin.console, commandLine);

        this.plugin.currentPlayer = resultPlayer;
        int runLength = this.config.getInt("run-length");
        runLength = runLength == 0 ? 3600 : runLength;
        
        new EndGame(this.plugin).runTaskLater(this.plugin, (long) runLength * 20);

        String currentTime = String.valueOf(new Date().getTime());
        List<String> newCooldown = Arrays.asList(playerUID, currentTime);
        ArrayList<String> newerCooldown = new ArrayList<String>(newCooldown);
        cooldowns.add(newerCooldown);
        CustomStorage.config.set("on-cooldown", cooldowns);
        CustomStorage.save();

        String broadcast = ChatColor.GREEN + resultPlayer.getName() + " has entered the Catacombs";
        Bukkit.broadcastMessage(broadcast);
        SenderMessage.sendPrivate(resultPlayer, "You now have " + runLength + " seconds to finish the run");
        SenderMessage.sendSuccess(sender, "Sent " + resultPlayer.getName() + " to the catacombs");
        return true;
    }
}
