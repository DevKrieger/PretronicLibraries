package net.prematic.libraries.command.command;

import net.prematic.libraries.command.sender.CommandSender;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 04.10.18 14:31
 *
 */

public abstract class SubCommand extends MainCommand {

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

    public abstract void onSubCommandExecute(CommandSender sender, String[] args);
}