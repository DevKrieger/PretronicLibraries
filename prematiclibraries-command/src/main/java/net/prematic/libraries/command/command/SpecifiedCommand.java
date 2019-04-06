/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 31.03.19 17:51
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
import net.prematic.libraries.command.notfound.CommandNotFoundHandler;
import net.prematic.libraries.command.manager.CommandManager;
import net.prematic.libraries.command.sender.CommandSender;
import net.prematic.libraries.utility.Iterators;
import net.prematic.libraries.utility.owner.ObjectOwner;

import java.util.*;

public abstract class SpecifiedCommand<T> extends SpecifiedSubCommand implements CommandManager {

    public static String DEFAULT_OBJECT_NOT_FOUND = "The command %subCommand% in %command% was not found.";

    private final List<CommandEntry<SpecifiedSubCommand<T>>> subCommands;
    private CommandNotFoundHandler notFoundHandler, objectNotFoundHandler;

    public SpecifiedCommand(String name) {
        this(name,"none");
    }

    public SpecifiedCommand(String name, String description) {
        this(name, description,null);
    }

    public SpecifiedCommand(String name, String description, String permission) {
        this(name, description, permission,new HashSet<>());
    }

    public SpecifiedCommand(String name, String description, String permission, String... aliases) {
        this(name, description, permission,Arrays.asList(aliases));
    }

    public SpecifiedCommand(String name, String description, String permission, Collection<String> aliases) {
        super(name, description, permission, aliases);
        this.subCommands = new ArrayList<>();
    }

    @Override
    public void setNotFoundHandler(CommandNotFoundHandler notFoundCommand){
        this.notFoundHandler = notFoundCommand;
    }

    @Override
    public SpecifiedSubCommand<T> getCommand(String name) {
        return Iterators.iterateAndWrapOne(this.subCommands, entry -> entry.getCommand().hasAlias(name),CommandEntry::getCommand);
    }

    @Override
    public List<Command> getCommands() {
        return Iterators.iterateAndWrap(this.subCommands,CommandEntry::getCommand);
    }

    @Override
    public void dispatchCommand(CommandSender sender, String command) {}

    @Override
    public void registerCommand(ObjectOwner owner, Command command) {
        if(command instanceof SpecifiedSubCommand) registerSubCommand(owner, (SpecifiedSubCommand<T>) command);
        else throw new IllegalArgumentException("A Specified command requires a specified sub command.");
    }

    @Override
    public void unregisterCommand(String command) {
        Command cmd = getCommand(command);
        if(cmd != null) unregisterCommand(cmd);
    }

    @Override
    public void unregisterCommand(Command command) {
        Iterators.iterateAndRemove(this.subCommands, entry -> entry.getCommand().equals(command));
    }

    @Override
    public void unregisterCommand(ObjectOwner owner) {
        Iterators.iterateAndRemove(this.subCommands, entry -> entry.getOwner().equals(owner));
    }

    @Override
    public void unregisterAll() {
        this.subCommands.clear();
    }

    public void registerSubCommand(ObjectOwner owner, SpecifiedSubCommand<T> command) {
        if(getCommand(command.getName()) != null) throw new IllegalArgumentException("A command with the name "+command.getName()+" is already registered as sub command.");
        this.subCommands.add(command.toEntry(owner));
    }

    @Override
    public void execute(CommandSender sender, Object object, String[] args) {
        execute(sender, args);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length >= 2) {
            SpecifiedSubCommand<T> command = getCommand(args[1]);
            if(command != null) {
                T object = getSpecifiedData(sender, args[0]);
                if(object != null) command.execute(sender, object, Arrays.copyOfRange(args, 2, args.length));
                else{
                    if(objectNotFoundHandler != null) objectNotFoundHandler.execute(sender, args[0], Arrays.copyOfRange(args, 1, args.length));
                    else sender.sendMessage(DEFAULT_OBJECT_NOT_FOUND.replace("%object%",args[0]));
                }
                return;
            }
        }
        if(notFoundHandler != null) notFoundHandler.execute(sender,args.length>=1?args[0]:"Unknown",Arrays.copyOfRange(args, 1, args.length));
        else sender.sendMessage(MainCommand.DEFAULT_NOT_FOUND_MESSAGE.replace("%command%",getName()).replace("%subCommand%",args.length>=1?args[0]:"Unknown"));
    }

    public abstract T getSpecifiedData(CommandSender sender, String data);
}
