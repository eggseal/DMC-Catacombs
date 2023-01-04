package me.wholesome_seal.jasonbourne.command;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.wholesome_seal.jasonbourne.JasonBourne;
import me.wholesome_seal.jasonbourne.SubCommand;
import me.wholesome_seal.jasonbourne.command.catacomb.DefaultReward;
import me.wholesome_seal.jasonbourne.command.catacomb.DisplayReward;
import me.wholesome_seal.jasonbourne.command.catacomb.End;
import me.wholesome_seal.jasonbourne.command.catacomb.Filter;
import me.wholesome_seal.jasonbourne.command.catacomb.Start;
import me.wholesome_seal.jasonbourne.command.catacomb.Winner;
import me.wholesome_seal.jasonbourne.function.SenderMessage;

public class CatacombManager implements CommandExecutor {

    private JasonBourne plugin;
    public HashMap<String, SubCommand> subCommands = new HashMap<String, SubCommand>();

    private String name = "catacomb";

    public CatacombManager(JasonBourne plugin) {
        this.plugin = plugin;
        this.plugin.getCommand(this.name).setExecutor(this);

        new CatacombCompleter(this.plugin, this.name, this);

        //  SUBCOMMAND REGISTRY
        new Start(this.plugin, this);
        new Winner(this.plugin, this);
        new End(this.plugin, this);
        new Filter(this.plugin, this);
        new DefaultReward(this.plugin, this);
        new DisplayReward(this.plugin, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            SenderMessage.sendError(sender, "Do you are have stupid?");
            return false;
        }

        String inputSubCommand = args[0];
        SubCommand subCommand = this.subCommands.get(inputSubCommand);
        if (subCommand == null) {
            SenderMessage.sendError(sender, "Such subcommand does not exist");
            return false;
        }

        if (!sender.hasPermission("catacomb.manager")) {
            SenderMessage.sendError(sender, "Missing permissions: catacombs.manager");
            return false;
        }

        return subCommand.execute(sender, args);
    }
}
