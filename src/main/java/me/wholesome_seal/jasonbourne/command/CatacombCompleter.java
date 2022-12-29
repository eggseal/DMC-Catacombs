package me.wholesome_seal.jasonbourne.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;

import me.wholesome_seal.jasonbourne.JasonBourne;

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
        ArrayList<String> completions = new ArrayList<String>();

        switch (args.length) {
            case 1: {
                completions.addAll(manager.subCommands.keySet().parallelStream().toList());
                break;
            }
        }


        return completions;
    }
    
}
