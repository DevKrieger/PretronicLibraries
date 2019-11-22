/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 17.11.19, 17:46
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

import net.prematic.libraries.command.CommandEntry;
import net.prematic.libraries.command.manager.CommandManager;
import net.prematic.libraries.command.sender.CommandSender;
import net.prematic.libraries.utility.Iterators;
import net.prematic.libraries.utility.interfaces.ObjectOwner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class MainObjectCommand<T> extends ObjectCommand<T> implements CommandManager {

    private final List<CommandEntry> commands;
    private CommandExecutor notFoundHandler, objectNotFoundHandler;

    public MainObjectCommand(String name) {
        super(name);
        commands = new ArrayList<>();
    }

    public MainObjectCommand(String name, String description) {
        super(name, description);
        commands = new ArrayList<>();
    }

    public MainObjectCommand(String name, String description, String permission) {
        super(name, description, permission);
        commands = new ArrayList<>();
    }

    public MainObjectCommand(String name, String description, String permission, String... aliases) {
        super(name, description, permission, aliases);
        commands = new ArrayList<>();
    }

    public MainObjectCommand(String name, String description, String permission, Collection aliases) {
        super(name, description, permission, aliases);
        commands = new ArrayList<>();
    }

    @Override
    public Command getCommand(String name) {
        CommandEntry result = Iterators.findOne(commands, entry -> entry.getCommand().getName().equalsIgnoreCase(name));
        return result != null? result.getCommand() : null;
    }

    @Override
    public List<Command> getCommands() {
        return Iterators.map(this.commands, CommandEntry::getCommand);
    }

    @Override
    public void setNotFoundHandler(CommandExecutor executor) {
        this.notFoundHandler = executor;
    }

    public void setObjectNotFoundHandler(CommandExecutor executor){
        this.objectNotFoundHandler = executor;
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
        this.commands.add(new CommandEntry(owner,command));
    }

    @Override
    public void unregisterCommand(String command) {
        Iterators.removeOne(this.commands, entry -> entry.getCommand().getName().equalsIgnoreCase(command));
    }

    @Override
    public void unregisterCommand(Command command) {
        Iterators.removeOne(this.commands, entry -> entry.getCommand().equals(command));
    }

    @Override
    public void unregisterCommand(ObjectOwner owner) {
        Iterators.removeOne(this.commands, entry -> entry.getOwner().equals(owner));
    }

    @Override
    public void unregisterCommands() {
        this.commands.clear();
    }

    @Override
    public void execute(CommandSender sender, String command, String[] args) {
        String name = null;//perms object <xy> delete
        if(args.length > 0){
            T object = getObject(name);
            if(object != null) execute(sender,command, object,Arrays.copyOfRange(args,1,args.length));
            return;
        }
        objectNotFoundHandler.execute(sender, name, Arrays.copyOfRange(args,1,args.length));
    }

    @Override
    public void execute(CommandSender sender,String command, Object object, String[] args) {
       if(args.length > 0){
           for (CommandEntry entry : commands) {
               if(entry.getCommand().hasAlias(args[0])){
                   if(entry.getCommand() instanceof ObjectCommand){
                       ((ObjectCommand) entry.getCommand()).execute(sender,args[0],object,Arrays.copyOfRange(args,1,args.length));
                   }else entry.getCommand().execute(sender, command, args);
                   return;
               }
           }
       }
       notFoundHandler.execute(sender,args[0],Arrays.copyOfRange(args,1,args.length));
    }

    public abstract T getObject(String name);
}
