package net.prematic.libraries.command.manager;

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

    Command getCommand(String name);

    Collection<Command> getCommands();

    void dispatchCommand(CommandSender sender, String command);

    void registerCommand(CommandOwner owner, Command command);

    void unregisterCommand(String command);

    void unregisterCommand(Command command);

    void unregisterCommand(CommandOwner owner);

    void unregisterAll();

    Messages messages = new Messages();

    default Messages getMessages() {
        return messages;
    }

    class Messages {

        public String commandNotFound, commandHelpHeader, commandHelp;

        public Messages() {
            this.commandNotFound = "Command not found. Use the command \"help\" for more information!";
            this.commandHelpHeader = "Page %currentPage%/%maxPages%";
            this.commandHelp = "%command% %commandUsage% %commandDescription%";
        }
    }
}