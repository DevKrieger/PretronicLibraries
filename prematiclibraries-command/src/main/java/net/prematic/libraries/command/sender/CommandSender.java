package net.prematic.libraries.command.sender;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 01.09.18 18:12
 *
 */

import net.prematic.libraries.language.Language;

public interface CommandSender {

    String getName();

    boolean hasPermission(String permission);

    void sendMessage(String message);

    Language getLanguage();

}
