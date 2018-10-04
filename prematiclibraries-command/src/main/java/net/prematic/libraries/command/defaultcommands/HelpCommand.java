package net.prematic.libraries.command.defaultcommands;

import net.prematic.libraries.command.command.Command;
import net.prematic.libraries.command.commandmanager.CommandManager;
import net.prematic.libraries.command.sender.CommandSender;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 04.10.18 14:32
 *
 */

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Shows information about all command");
        addAlias("hilfe");
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("Available Commands:");
        for(Command command : getCommandManager().getCommands()) {
            if(command != this) sender.sendMessage(command.getName()+" | "+command.getDescription() + (command.hasUsage() ? " | " + command.getUsage() : ""));
        }
    }
}
