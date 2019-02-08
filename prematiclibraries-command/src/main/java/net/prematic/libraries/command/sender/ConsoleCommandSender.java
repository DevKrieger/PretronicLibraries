package net.prematic.libraries.command.sender;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 01.09.18 18:12
 *
 */

import net.prematic.libraries.language.Language;

public class ConsoleCommandSender implements CommandSender{

    public String getName() {
        return "Console";
    }

    public boolean hasPermission(String permission) {
        return true;
    }

    public void sendMessage(String message) {
        System.out.println(message);
    }

    @Override
    public Language getLanguage() {
        return null;
    }
}
