package me.wholesome_seal.jasonbourne;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    public boolean execute(CommandSender sender, String[] args);
}
