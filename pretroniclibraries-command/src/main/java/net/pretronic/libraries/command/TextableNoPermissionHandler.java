package net.pretronic.libraries.command;

import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.message.Textable;

public class TextableNoPermissionHandler implements NoPermissionHandler {

    private final Textable textable;

    public TextableNoPermissionHandler(Textable textable) {
        this.textable = textable;
    }

    @Override
    public void handle(CommandSender sender, String permission, String command, String[] args) {
        sender.sendMessage(textable);
    }
}
