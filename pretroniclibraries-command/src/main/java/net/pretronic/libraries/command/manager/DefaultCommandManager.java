/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:40
 *
 * The PretronicLibraries Project is under the Apache License, version 2.0 (the "License");
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

package net.pretronic.libraries.command.manager;

import net.pretronic.libraries.command.NotFoundHandler;
import net.pretronic.libraries.command.command.Command;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.utility.Iterators;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.util.ArrayList;
import java.util.List;

public class DefaultCommandManager implements CommandManager {

    private final List<Command> commands;
    private NotFoundHandler notFoundHandler;

    public DefaultCommandManager() {
        this.commands = new ArrayList<>();
    }

    @Override
    public Command getCommand(String name) {
        return Iterators.findOne(this.commands, command -> command.getConfiguration().getName().equalsIgnoreCase(name));
    }

    @Override
    public List<Command> getCommands() {
        return commands;
    }

    @Override
    public void setNotFoundHandler(NotFoundHandler handler) {
        this.notFoundHandler = handler;
    }

    @Override
    public void dispatchCommand(CommandSender sender, String name0) {
        String name = name0.trim();
        int index = name.indexOf(" ");
        String command;
        if(index == -1) command = name;
        else command = name.substring(0,index);
        Command cmd = getCommand(command);
        String[] args = index==-1?new String[0]:name.substring(index+1).split(" ");
        if(cmd != null) cmd.execute(sender,args);
        else if(notFoundHandler != null) notFoundHandler.handle(sender,command,args);
    }

    @Override
    public void registerCommand(Command command) {
        if(!command.getConfiguration().isEnabled()) return;
        if(getCommand(command.getConfiguration().getName()) != null){
            throw new IllegalArgumentException("There is already a commend with the name "+command.getConfiguration().getName()+" registered.");
        }
        this.commands.add(command);
    }

    @Override
    public void unregisterCommand(String name) {
        Iterators.removeOne(this.commands, entry -> entry.getConfiguration().getName().equalsIgnoreCase(name));
    }

    @Override
    public void unregisterCommand(Command command) {
        Iterators.removeOne(this.commands, entry -> entry.equals(command));
    }

    @Override
    public void unregisterCommand(ObjectOwner owner) {
        Iterators.removeSilent(this.commands, entry -> entry.getOwner().equals(owner));
    }

    @Override
    public void unregisterCommands() {
        this.commands.clear();
    }
}
