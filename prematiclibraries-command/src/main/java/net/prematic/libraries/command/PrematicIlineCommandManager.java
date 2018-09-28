package net.prematic.libraries.command;

import net.prematic.libraries.command.sender.CommandSender;
import net.prematic.libraries.command.sender.ConsoleCommandSender;
/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 03.09.18 19:42
 *
 */

public class PrematicIlineCommandManager extends PrematicCommandManager{

    public void startConsoleInputReader(final jline.console.ConsoleReader reader){
        startConsoleInputReader(reader,new ConsoleCommandSender());
    }
    public void startConsoleInputReader(final jline.console.ConsoleReader reader, final CommandSender sender){
        this.running = true;
        new Thread(()->{
            while(this.running){
                try{
                    String command = reader.readLine();
                    if(command != null && (!command.equalsIgnoreCase(""))) dispatchCommand(sender,command);
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        }).start();
    }
}
