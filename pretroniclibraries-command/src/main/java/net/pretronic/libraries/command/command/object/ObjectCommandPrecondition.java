package net.pretronic.libraries.command.command.object;

import net.pretronic.libraries.command.sender.CommandSender;

public interface ObjectCommandPrecondition<T> {

    boolean checkPrecondition(CommandSender sender, T object);
}
