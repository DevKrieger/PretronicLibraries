package net.pretronic.libraries.command;

import net.pretronic.libraries.command.sender.CommandSender;

public interface NoPermissionAble extends NoPermissionHandler {

    void noPermission(CommandSender sender, String permission, String command, String[] args);

    @Override
    default void handle(CommandSender sender, String permission, String command, String[] args) {
        noPermission(sender, permission, command, args);
    }
}
