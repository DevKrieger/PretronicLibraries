package net.pretronic.libraries.command.command.object;

import net.pretronic.libraries.command.NoPermissionHandler;
import net.pretronic.libraries.command.sender.CommandSender;

public interface ObjectNoPermissionHandler<T> extends NoPermissionHandler {

    void handle(CommandSender sender, String permission, T object, String[] args);

    @Override
    default void handle(CommandSender sender, String permission, String command, String[] args) {
        throw new UnsupportedOperationException();
    }
}
