package me.wholesome_seal.jasonbourne.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;

import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.SubCommand;

public class CatacombCompleter implements TabCompleter {
    private JasonBourne plugin;
    @SuppressWarnings("unused")
    private FileConfiguration config;
    private CatacombManager manager;

    public CatacombCompleter(JasonBourne plugin, String name, CatacombManager manager) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.manager = manager;

        this.plugin.getCommand(name).setTabCompleter(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        HashMap<String, SubCommand> subCommands = manager.subCommands;
        int argIndex = args.length - 2;
        argIndex = argIndex >= 0 ? argIndex : 0;

        switch (args.length) {
            case 0: {
                return new ArrayList<String>();
            }
            case 1: {
                return subCommands.keySet().parallelStream().toList();
            }
            default: {
                return subCommands.get(args[0]).args.get(argIndex);
            }
        }
    }
}
