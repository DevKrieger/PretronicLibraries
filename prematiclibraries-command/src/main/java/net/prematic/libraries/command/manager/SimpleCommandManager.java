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

package net.prematic.libraries.command.manager;

import net.prematic.libraries.command.CommandEntry;
import net.prematic.libraries.command.command.Command;
import net.prematic.libraries.command.notfound.CommandNotFoundHandler;
import net.prematic.libraries.command.sender.CommandSender;
import net.prematic.libraries.utility.Iterators;
import net.prematic.libraries.utility.interfaces.ObjectOwner;

import java.util.ArrayList;
import java.util.List;

public class SimpleCommandManager implements CommandManager {

    public static String DEFAULT_NOT_FOUND_MESSAGE = "The command %command% was not found.";

    private final List<CommandEntry> commands;
    private CommandNotFoundHandler notFoundHandler;

    public SimpleCommandManager() {
        this.commands = new ArrayList<>();
    }

    @Override
    public String getName() {
        return "SimpleCommandManager";
    }

    @Override
    public Command getCommand(String name) {
        return Iterators.mapOne(this.commands, entry -> entry.getCommand().hasAlias(name),CommandEntry::getCommand);
    }

    @Override
    public List<Command> getCommands() {
        return Iterators.iterateAndWrap(this.commands,CommandEntry::getCommand);
    }

    @Override
    public void setNotFoundHandler(CommandNotFoundHandler handler) {
        this.notFoundHandler = handler;
    }

    @Override
    public void dispatchCommand(CommandSender sender, String name) {
        name = name.trim();
        int index = name.indexOf(" ");
        String command;
        if(index == -1) command = name;
        else command = name.substring(0,index);
        Command cmd = getCommand(command);
        String[] args = index==-1?new String[0]:name.substring(index+1).split(" ");
        if(cmd != null) cmd.execute(sender,args);
        else if(notFoundHandler != null) notFoundHandler.execute(sender,command,args);
        else sender.sendMessage(DEFAULT_NOT_FOUND_MESSAGE.replace("%command%",command));
    }

    @Override
    public void registerCommand(ObjectOwner owner, Command command) {
        if(getCommand(command.getName()) != null) throw new IllegalArgumentException("There is already a commend with the name "+command.getName()+" registered.");
        this.commands.add(command.toEntry(owner));
    }

    @Override
    public void unregisterCommand(String command) {
        Command cmd = getCommand(command);
        if(cmd != null) unregisterCommand(cmd);
    }

    @Override
    public void unregisterCommand(Command command) {
        Iterators.iterateAndRemove(this.commands, entry -> entry.getCommand().equals(command));
    }

    @Override
    public void unregisterCommand(ObjectOwner owner) {
        Iterators.iterateAndRemove(this.commands, entry -> entry.getOwner().equals(owner));
    }

    @Override
    public void unregisterAll() {
        this.commands.clear();
    }
}