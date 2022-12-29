package me.wholesome_seal.jasonbourne;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.wholesome_seal.jasonbourne.command.CatacombManager;
import me.wholesome_seal.jasonbourne.events.onPlayerDeath.LootManagerDeath;
import me.wholesome_seal.jasonbourne.events.onPlayerQuit.LootManagerQuit;
import me.wholesome_seal.jasonbourne.function.PrizeDisplay;

public final class JasonBourne extends JavaPlugin {
    @SuppressWarnings("unused")
    private PluginManager manager;
    private FileConfiguration config;

    public Player currentPlayer = null;
    public World catacombWorld = null;

    @Override
    public void onEnable() {
        this.manager = getServer().getPluginManager();
        this.config = getConfig();

        this.config.options().copyDefaults();
        saveConfig();

        this.setCatacombWorld();

        //  UILITIES SETUP
        PrizeDisplay.setup(this);
        
        //  COMMAND REGISTRY
        new CatacombManager(this);

        //  EVENT REGISTRY
        new LootManagerDeath(this);
        new LootManagerQuit(this);
    }

    private void setCatacombWorld() {
        String multiverseWorldUID = this.config.getString("multiverse-world-UID");
        this.catacombWorld = Bukkit.getWorld(UUID.fromString(multiverseWorldUID));
    }

    public ArrayList<ItemStack> getCatacombPrizePool() {
        ArrayList<ItemStack> prizePool;
        try {
            @SuppressWarnings("unchecked")
            ArrayList<ItemStack> rawPrizePool = (ArrayList<ItemStack>) this.config.get("catacomb-prize-pool");
            prizePool = rawPrizePool == null ? new ArrayList<ItemStack>() : rawPrizePool;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
        return prizePool;
    }

    public ArrayList<ItemStack> getCatacombDefaultPrize() {
        ArrayList<ItemStack> prizePool;
        try {
            @SuppressWarnings("unchecked")
            ArrayList<ItemStack> rawPrizePool = (ArrayList<ItemStack>) this.config.get("catacomb-default-prize");
            prizePool = rawPrizePool == null ? new ArrayList<ItemStack>() : rawPrizePool;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
        return prizePool;
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

        String multiverseWorldUID = this.catacombWorld.getUID().toString();
        String senderWorldUID = senderWorld.getUID().toString();

        if (senderWorldUID == null) return false;
        return multiverseWorldUID.equals(senderWorldUID);
    }
}
