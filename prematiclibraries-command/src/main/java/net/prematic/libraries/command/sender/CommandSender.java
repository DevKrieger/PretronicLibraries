package net.prematic.libraries.command.sender;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 01.09.18 18:12
 *
 */

public interface CommandSender {

    public String getName();

    public Boolean hasPermission(String permission);

    public void sendMessage(String message);

}
