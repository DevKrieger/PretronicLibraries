package net.prematic.libraries.command;

import net.prematic.libraries.command.sender.CommandSender;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 01.09.18 18:12
 *
 */

public class HelpCommand extends Command{

    private CommandManager commandmanager;

    public HelpCommand(CommandManager commandManager) {
        super("help", "Shows information about all commands", "hilfe");
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
