package net.prematic.libraries.command.sender;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 01.09.18 18:12
 *
 */

public class ConsoleCommandSender implements CommandSender{

    public String getName() {
        return "Console";
    }
    public Boolean hasPermission(String permission) {
        return true;
    }
    public void sendMessage(String message) {
        System.out.println(message);
    }
}
