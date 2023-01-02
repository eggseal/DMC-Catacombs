package me.wholesome_seal.jasonbourne;

import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.wholesome_seal.jasonbourne.command.CatacombManager;
import me.wholesome_seal.jasonbourne.events.onPlayerDeath.LootManagerDeath;
import me.wholesome_seal.jasonbourne.events.onPlayerJoin.QuitCatacombsJoin;
import me.wholesome_seal.jasonbourne.events.onPlayerQuit.LootManagerQuit;
import me.wholesome_seal.jasonbourne.events.onPlayerRespawn.QuitCatacombsRespawn;
import me.wholesome_seal.jasonbourne.function.DataSetup;
import me.wholesome_seal.jasonbourne.tasks.CooldownManager;

public final class JasonBourne extends JavaPlugin {
    @SuppressWarnings("unused")
    private PluginManager manager;
    private FileConfiguration config;
    
    public Player currentPlayer = null;
    public World catacombWorld = null;
    public World defaultWorld = null;
    
    public ConsoleCommandSender console;

    @Override
    public void onEnable() {
        this.manager = getServer().getPluginManager();
        this.config = getConfig();
        this.console = getServer().getConsoleSender();

        this.config.options().copyDefaults();
        this.saveDefaultConfig();
        this.reloadConfig();

        //  SETUP
        CustomStorage.setup(this);
        DataSetup.setCatacombWorld(this);
        DataSetup.setDefaultWorld(this);
        new CooldownManager(this).runTaskTimer(this, 0, 600);
        
        //  COMMAND REGISTRY
        new CatacombManager(this);

        //  EVENT REGISTRY
        new LootManagerDeath(this);
        new LootManagerQuit(this);
        new QuitCatacombsJoin(this);
        new QuitCatacombsRespawn(this);
    }

    public void sendPlayerToDefault(Player player, boolean playerEnded) {
        String playerName = player.getName();
        String worldName = this.defaultWorld == null ? "world" : this.defaultWorld.getName();
        String command = "mvtp " + playerName + " " + worldName;
        getServer().dispatchCommand(this.console, command);

        if (playerEnded) this.currentPlayer = null;
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

        if (this.catacombWorld == null) return false;
        String multiverseWorldUID = this.catacombWorld.getUID().toString();
        String senderWorldUID = senderWorld.getUID().toString();

        if (senderWorldUID == null) return false;
        return multiverseWorldUID.equals(senderWorldUID);
    }
}
