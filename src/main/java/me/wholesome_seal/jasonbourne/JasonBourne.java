package me.wholesome_seal.jasonbourne;

import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.wholesome_seal.jasonbourne.events.onPlayerDeath.LootManagerDeath;
import me.wholesome_seal.jasonbourne.events.onPlayerQuit.LootManagerQuit;

public final class JasonBourne extends JavaPlugin {
    @SuppressWarnings("unused")
    private PluginManager manager;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        this.manager = getServer().getPluginManager();
        this.config = getConfig();

        this.config.options().copyDefaults();
        saveConfig();
        
        //  COMMAND REGISTRY

        //  EVENT REGISTRY
        new LootManagerDeath(this);
        new LootManagerQuit(this);
    }

    public boolean isExecutedOnCorrectWorld(CommandSender sender) {
        BlockCommandSender commandBlock;
        Player player;
        World senderWorld;

        if (sender instanceof BlockCommandSender) {
            commandBlock = (BlockCommandSender) sender;
            senderWorld = commandBlock.getBlock().getWorld();
        } else if (sender instanceof Player) {
            player = (Player) sender;
            senderWorld = player.getWorld();
        } else {
            return false;
        }

        String multiverseWorldUID = this.config.getString("multiverse-world-UID");
        String senderWorldUID = senderWorld.getUID().toString();

        if (senderWorldUID == null) return false;
        return multiverseWorldUID.equals(senderWorldUID);
    }
}
