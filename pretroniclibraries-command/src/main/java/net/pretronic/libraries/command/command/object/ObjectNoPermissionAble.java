package net.pretronic.libraries.command.command.object;

import net.pretronic.libraries.command.sender.CommandSender;

public interface ObjectNoPermissionAble<T> extends ObjectNoPermissionHandler<T> {

    void noObjectPermission(CommandSender sender, String permission, T object, String[] args);

    @Override
    default void handle(CommandSender sender, String permission, T object, String[] args) {
        noObjectPermission(sender, permission, object, args);
    }
}
