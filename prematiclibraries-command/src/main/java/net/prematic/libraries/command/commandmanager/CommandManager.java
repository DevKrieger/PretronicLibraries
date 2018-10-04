package net.prematic.libraries.command.commandmanager;

import net.prematic.libraries.command.command.Command;
import net.prematic.libraries.command.owner.CommandOwner;
import net.prematic.libraries.command.sender.CommandSender;

import java.util.Collection;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 04.10.18 14:32
 *
 */

public interface CommandManager {

    public Command getCommand(String name);

    public Collection<Command> getCommands();

    public void dispatchCommand(CommandSender sender, String command);

    public void registerCommand(CommandOwner owner, Command command);

    public void unregisterCommand(String command);

    public void unregisterCommand(Command command);

    public void unregisterCommand(CommandOwner owner);

    public void unregisterAll();
}