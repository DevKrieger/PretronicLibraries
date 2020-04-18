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

package net.pretronic.libraries.command.command;

import net.pretronic.libraries.command.*;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.command.object.ObjectNoPermissionAble;
import net.pretronic.libraries.command.manager.CommandManager;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.utility.Iterators;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.util.*;

public class MainCommand extends BasicCommand implements CommandManager, Completable {

    private final List<Command> subCommands;
    private final Collection<String> internalTabComplete;
    private NotFoundHandler notFoundHandler;
    private NoPermissionHandler noPermissionHandler;

    public MainCommand(ObjectOwner owner, CommandConfiguration configuration) {
        super(owner,configuration);
        this.subCommands = new ArrayList<>();
        this.internalTabComplete = new ArrayList<>();

        if(this instanceof NotFindable) setNotFoundHandler((NotFoundHandler) this);

        if(this instanceof ObjectNoPermissionAble) setNoPermissionHandler((ObjectNoPermissionAble) this);
        else if(this instanceof NoPermissionAble) setNoPermissionHandler((NoPermissionAble) this);
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
    public NoPermissionHandler getNoPermissionHandler() {
        return this.noPermissionHandler;
    }

    @Override
    public void setNoPermissionHandler(NoPermissionHandler noPermissionHandler) {
        this.noPermissionHandler = noPermissionHandler;
    }

    @Override
    public void dispatchCommand(CommandSender sender, String name0) {
        String name = name0.trim();
        int index = name.indexOf(" ");
        String[] args = index==-1?new String[0]:name.substring(index+1).split(" ");
        execute(sender,args);
    }

    @Override
    public void registerCommand(Command command) {
        if(!command.getConfiguration().isEnabled()) return;
        if(getCommand(command.getConfiguration().getName()) != null){
            throw new IllegalArgumentException("A command with the name "+command.getConfiguration().getName()+" is already registered as sub command.");
        }
        this.subCommands.add(command);
        this.internalTabComplete.add(command.getConfiguration().getName());
    }

    @Override
    public void unregisterCommand(String command) {
        Command result = Iterators.removeOne(this.subCommands, entry -> entry.getConfiguration().getName().equalsIgnoreCase(command));
        if(result != null) this.internalTabComplete.remove(result.getConfiguration().getName());
    }

    @Override
    public void unregisterCommand(Command command) {
        Command result = Iterators.removeOne(this.subCommands, entry -> entry.equals(command));
        if(result != null) this.internalTabComplete.remove(result.getConfiguration().getName());
    }

    @Override
    public void unregisterCommand(ObjectOwner owner) {
        Collection<Command> result = Iterators.remove(this.subCommands, entry -> entry.getOwner().equals(owner));
        for (Command command : result) this.internalTabComplete.remove(command.getConfiguration().getName());
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
        if(args.length > 0) {
            for (Command command : subCommands) {
                if(CommandManager.hasPermission(sender, noPermissionHandler, null, command.getConfiguration().getPermission(), args[0], args)) {
                    if (command.getConfiguration().hasAlias(args[0])) {
                        command.execute(sender,Arrays.copyOfRange(args, 1, args.length));
                        return;
                    }
                }
            }
        }
        if(notFoundHandler != null){
            notFoundHandler.handle(sender, args.length == 0 ? "" : args[0],
                    args.length == 0 ? args : Arrays.copyOfRange(args,1,args.length));
        }
    }

    @Override
    public Collection<String> complete(CommandSender sender, String[] args) {
        if(args.length <= 0) return internalTabComplete;
        else{
            String subCommand = args[0];
            Command command = getCommand(subCommand);
            if(command instanceof Completable) return ((Completable) command).complete(sender,Arrays.copyOfRange(args,1,args.length));
            else return Collections.emptyList();
        }
    }
}
