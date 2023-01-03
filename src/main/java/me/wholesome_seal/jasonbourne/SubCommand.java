package me.wholesome_seal.jasonbourne;

import java.util.List;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {
    public String name;
    public String description;
    public List<List<String>> args;

    public abstract boolean execute(CommandSender sender, String[] args);
}
