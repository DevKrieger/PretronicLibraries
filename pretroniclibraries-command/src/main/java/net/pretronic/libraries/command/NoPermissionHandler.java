package net.pretronic.libraries.command;

import net.pretronic.libraries.command.sender.CommandSender;

public interface NoPermissionHandler {

    void handle(CommandSender sender, String permission, String command, String[] args);
}
