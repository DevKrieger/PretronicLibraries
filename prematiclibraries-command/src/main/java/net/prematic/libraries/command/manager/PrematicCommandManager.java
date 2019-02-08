package net.prematic.libraries.command.manager;

import net.prematic.libraries.command.defaults.HelpCommand;
import net.prematic.libraries.command.command.Command;
import net.prematic.libraries.command.owner.CommandOwner;
import net.prematic.libraries.command.owner.SystemCommandOwner;
import net.prematic.libraries.command.sender.CommandSender;
import net.prematic.libraries.command.sender.ConsoleCommandSender;
import net.prematic.libraries.language.LanguageManager;

import java.io.Console;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 08.02.19 16:17
 *
 * The PrematicLibraries Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

public class PrematicCommandManager implements CommandManager {

    private final Collection<Command> commands;
    private LanguageManager languageManager;
    protected boolean running;

    public PrematicCommandManager(){
        this.commands = new LinkedList<>();
    }

    public Collection<Command> getCommands() {
        return this.commands;
    }

    public Command getCommand(String name) {
        for(Command commands : this.commands) if(commands.hasAliases(name)) return commands;
        return null;
    }

    @Override
    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public void setLanguageManager(LanguageManager languageManager) {
        this.languageManager = languageManager;
    }

    public void registerCommand(CommandOwner owner, Command command) {
        command.init(owner, this);
        this.commands.add(command);
    }

    public void registerHelpCommand() {
        Command command = new HelpCommand();
        command.init(new SystemCommandOwner(), this);
        this.commands.add(command);
    }

    public void unregisterCommand(String name){
        Command command = getCommand(name);
        if(command != null) unregisterCommand(command);
    }

    public void unregisterCommand(Command command){
        this.commands.remove(command);
    }

    public void unregisterCommand(CommandOwner owner) {
        for(Command command : new LinkedList<>(this.commands)) if(command.getOwner().equals(owner)) this.commands.remove(command);
    }

    public void unregisterAll() {
        this.commands.clear();
    }


    public void dispatchCommand(CommandSender sender, String message){
        final String[] args = message.trim().split(" ");
        String name = args[0];
        Command command = getCommand(name);
        if(command != null) command.execute(sender,Arrays.copyOfRange(args,1,args.length));
        else
            sender.sendMessage((getLanguageManager() == null ? getMessages().commandNotFound :
                    getLanguageManager().getMessage(sender.getLanguage(), getMessages().commandNotFound))
                    .replace("[command]",name));
    }

    public void shutdown(){
        this.running = false;
    }

    public void startConsoleInputReader(){
        startConsoleInputReader(new ConsoleCommandSender());
    }

    public void startConsoleInputReader(final CommandSender sender){
        this.running = true;
        new Thread(()->{
            Console console = System.console();
            while(this.running){
                try{
                    String command = console.readLine();
                    if(command != null && (!command.equalsIgnoreCase(""))) dispatchCommand(sender,command);
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        }).start();
    }
}