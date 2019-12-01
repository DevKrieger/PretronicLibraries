/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Philipp Elvin Friedhoff
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

package net.prematic.libraries.command.command;

import net.prematic.libraries.command.manager.CommandManager;
import net.prematic.libraries.command.sender.CommandSender;
import net.prematic.libraries.utility.Iterators;
import net.prematic.libraries.utility.interfaces.ObjectOwner;

import java.util.*;

public class MainCommand extends SimpleCommand implements CommandManager {

    private final List<Command> subCommands;
    private CommandExecutor notFoundHandler;

    public MainCommand(String name) {
        this(name,"none");
    }

    public MainCommand(String name, String description) {
        this(name, description,null);
    }

    public MainCommand(String name, String description, String permission) {
        this(name, description, permission,new HashSet<>());
    }

    public MainCommand(String name, String description, String permission, String... aliases) {
        this(name, description, permission,Arrays.asList(aliases));
    }

    public MainCommand(String name, String description, String permission, Collection<String> aliases) {
        super(name, description, permission, aliases);
        this.subCommands = new ArrayList<>();
    }

    @Override
    public Command getCommand(String name) {
        return Iterators.findOne(this.subCommands, command -> command.getName().equalsIgnoreCase(name));
    }

    @Override
    public List<Command> getCommands() {
        return subCommands;
    }

    @Override
    public void setNotFoundHandler(CommandExecutor notFoundHandler) {
        this.notFoundHandler = notFoundHandler;
    }

    @Override
    public void dispatchCommand(CommandSender sender, String name) {
        name = name.trim();
        int index = name.indexOf(" ");
        String command;
        if(index == -1) command = name;
        else command = name.substring(0,index);
        String[] args = index==-1?new String[0]:name.substring(index+1).split(" ");
        execute(sender,command,args);
    }

    @Override
    public void registerCommand(ObjectOwner owner, Command command) {
        if(getCommand(command.getName()) != null) throw new IllegalArgumentException("A command with the name "+command.getName()+" is already registered as sub command.");
        command.init(this,owner);
        this.subCommands.add(command);
    }

    @Override
    public void unregisterCommand(String command) {
        Command cmd = getCommand(command);
        if(cmd != null) unregisterCommand(cmd);
    }

    @Override
    public void unregisterCommand(Command command) {
        Iterators.removeOne(this.subCommands, entry -> entry.equals(command));
    }

    @Override
    public void unregisterCommand(ObjectOwner owner) {
        Iterators.removeSilent(this.subCommands, entry -> entry.getOwner().equals(owner));
    }

    @Override
    public void unregisterCommands() {
        this.subCommands.clear();
    }

    public void registerSubCommand(ObjectOwner owner, Command command) {
        registerCommand(owner, command);
    }

    public void setNotFoundCommand(CommandExecutor handler){
        this.notFoundHandler = handler;
    }

    @Override
    public void execute(CommandSender sender,String cmd, String[] args) {
        String name = null;
        if(args.length > 0) {
            name = args[0];
            Command command = getCommand(name);
            if(command != null){
                command.execute(sender,name,Arrays.copyOfRange(args, 1, args.length));
                return;
            }
        }
        if(notFoundHandler != null) notFoundHandler.execute(sender,name,Arrays.copyOfRange(args, 1, args.length));
    }
}