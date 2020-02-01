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

import net.prematic.libraries.command.command.configuration.CommandConfiguration;
import net.prematic.libraries.command.manager.CommandManager;
import net.prematic.libraries.command.sender.CommandSender;
import net.prematic.libraries.utility.Iterators;
import net.prematic.libraries.utility.interfaces.ObjectOwner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainCommand extends BasicCommand implements CommandManager {

    private final List<Command> subCommands;
    private NotFoundHandler notFoundHandler;

    public MainCommand(ObjectOwner owner,CommandConfiguration configuration) {
        super(owner,configuration);
        subCommands = new ArrayList<>();
    }

    @Override
    public Command getCommand(String name) {
        return Iterators.findOne(this.subCommands, command -> command.getConfiguration().getName().equalsIgnoreCase(name));
    }

    @Override
    public List<Command> getCommands() {
        return subCommands;
    }

    @Override
    public void setNotFoundHandler(NotFoundHandler notFoundHandler) {
        this.notFoundHandler = notFoundHandler;
    }

    @Override
    public void dispatchCommand(CommandSender sender, String name) {
        name = name.trim();
        int index = name.indexOf(" ");
        String[] args = index==-1?new String[0]:name.substring(index+1).split(" ");
        execute(sender,args);
    }

    @Override
    public void registerCommand(Command command) {
        if(getCommand(command.getConfiguration().getName()) != null){
            throw new IllegalArgumentException("A command with the name "+command.getConfiguration().getName()+" is already registered as sub command.");
        }
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

    public void registerSubCommand(Command command) {
        registerCommand(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String name = null;
        if(args.length > 0) {
            name = args[0];
            Command command = getCommand(name);
            if(command != null){
                command.execute(sender,Arrays.copyOfRange(args, 1, args.length));
                return;
            }
        }
        if(notFoundHandler != null) notFoundHandler.handle(sender,name,Arrays.copyOfRange(args, 1, args.length));
    }
}
