package net.prematic.libraries.command;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 29.09.18 12:31
 *
 */

import net.prematic.libraries.command.sender.CommandSender;
import net.prematic.libraries.command.sender.MainCommand;

public abstract class SubCommand extends Command {

    private final MainCommand mainCommand;

    public SubCommand(String name, MainCommand mainCommand) {
        super(name);
        this.mainCommand = mainCommand;
    }

    public SubCommand(String name, String description, MainCommand mainCommand) {
        super(name, description);
        this.mainCommand = mainCommand;
    }

    public SubCommand(String name, String description, String permission, MainCommand mainCommand) {
        super(name, description, permission);
        this.mainCommand = mainCommand;
    }

    public SubCommand(String name, String description, String permission, String usage, MainCommand mainCommand, String... aliases) {
        super(name, description, permission, usage, aliases);
        this.mainCommand = mainCommand;
    }

    public MainCommand getMainCommand() {
        return mainCommand;
    }


    public abstract void execute(CommandSender sender, String[] args);
}
