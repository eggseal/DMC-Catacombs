package me.wholesome_seal.jasonbourne.function;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SenderMessage {
    public static void sendError(CommandSender sender, String message) {
        if (sender == null || message == null) return;
        
        String errorMessage = ChatColor.GREEN + "[Catacomb] " + ChatColor.RED + message;
        sender.sendMessage(errorMessage);
    }

    public static void sendPrivate(CommandSender sender, String message) {
        if (sender == null || message == null) return;

        String privateMessage = ChatColor.GREEN + "[Catacombs] " + ChatColor.GRAY + message;
        sender.sendMessage(privateMessage);
    }

    public static void sendSuccess(CommandSender sender, String message) {
        if (sender == null || message == null) return;

        String succMessage = ChatColor.GREEN + "[Catacombs] " + ChatColor.AQUA + message;
        sender.sendMessage(succMessage);
    }
}
