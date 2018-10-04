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

    private CommandManager commandmanager;

    public HelpCommand(CommandManager commandManager) {
        super("help", "Shows information about all command", "hilfe");
        this.commandmanager = commandManager;
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("Available Commands:");
        for(Command command : this.commandmanager.getCommands()){
            if(command != this) sender.sendMessage(command.getName()+" | "+command.getDescription() + (command.hasUsage() ? " | " + command.getUsage() : ""));
        }
    }
}
