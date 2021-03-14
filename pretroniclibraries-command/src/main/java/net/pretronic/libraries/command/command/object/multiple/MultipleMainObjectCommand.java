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

package net.pretronic.libraries.command.command.object.multiple;

import net.pretronic.libraries.command.Completable;
import net.pretronic.libraries.command.NoPermissionHandler;
import net.pretronic.libraries.command.NotFindable;
import net.pretronic.libraries.command.NotFoundHandler;
import net.pretronic.libraries.command.command.Command;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.command.object.*;
import net.pretronic.libraries.command.manager.CommandManager;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.utility.Iterators;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.util.*;

public abstract class MultipleMainObjectCommand<T,B> extends MainObjectCommand<T> implements CommandManager, DefinedCompletable<T> {

    private final List<Command> commands;
    private final Collection<String> internalTabComplete;
    private NotFoundHandler notFoundHandler;
    private MultipleObjectNotFindable<T> objectNotFoundHandler;
    private MultipleObjectCompletable<T> objectCompletable;

    public MultipleMainObjectCommand(ObjectOwner owner, CommandConfiguration configuration) {
        super(owner, configuration);
        this.commands = new ArrayList<>();
        this.internalTabComplete = new ArrayList<>();

        if(this instanceof NotFindable) setNotFoundHandler((NotFoundHandler) this);
        if(this instanceof MultipleObjectNotFindable) setObjectNotFoundHandler((MultipleObjectNotFindable<T>) this);
        if(this instanceof MultipleObjectCompletable) setObjectCompletableHandler((MultipleObjectCompletable<T>) this);
        if(this instanceof ObjectCommandPrecondition) setObjectCommandPrecondition((ObjectCommandPrecondition) this);
    }

    @Override
    public List<Command> getCommands() {
        return this.commands;
    }

    @Override
    public Command getCommand(String name) {
        return Iterators.findOne(this.commands, command -> command.getConfiguration().getName().equalsIgnoreCase(name));
    }

    @Override
    public void setNotFoundHandler(NotFoundHandler handler) {
        this.notFoundHandler = handler;
    }

    public void setObjectNotFoundHandler(MultipleObjectNotFindable<T> handler){
        this.objectNotFoundHandler = handler;
    }

    public void setObjectCompletableHandler(MultipleObjectCompletable<T> completable){
        this.objectCompletable = completable;
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
    public void registerCommand(Command command) {
        if(getCommand(command.getConfiguration().getName()) != null){
            throw new IllegalArgumentException("A command with the name "+command.getConfiguration().getName()+" is already registered as sub command.");
        }
        this.commands.add(command);
        this.internalTabComplete.add(command.getConfiguration().getName());
    }

    @Override
    public void unregisterCommand(String command) {
        Command result = Iterators.removeOne(this.commands, entry -> entry.getConfiguration().getName().equalsIgnoreCase(command));
        if(result != null) this.internalTabComplete.remove(result.getConfiguration().getName());
    }

    @Override
    public void unregisterCommand(Command command) {
        Command result = Iterators.removeOne(this.commands, entry -> entry.equals(command));
        if(result != null) this.internalTabComplete.remove(result.getConfiguration().getName());
    }

    @Override
    public void unregisterCommand(ObjectOwner owner) {
        List<Command> result = Iterators.remove(this.commands, entry -> entry.getOwner().equals(owner));
        for (Command command : result) this.internalTabComplete.remove(command.getConfiguration().getName());
    }

    @Override
    public void unregisterCommands() {
        this.commands.clear();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        throw new UnsupportedOperationException("You have to register this command in a main object command.");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(CommandSender sender, Object object, String[] args) {
        if(args.length > 0){
            String name = args[0];
            B bridged = getObject(sender, (T)object,name);
            if(bridged == null) {
                if(objectNotFoundHandler != null) {
                    objectNotFoundHandler.objectNotFound(sender, (T) object, name, Arrays.copyOfRange(args, 1, args.length));
                }
            } else if(args.length > 1) {
                super.execute(sender, bridged,Arrays.copyOfRange(args,1,args.length));
            } else {
                if(notFoundHandler != null){
                    if(notFoundHandler instanceof DefinedNotFindable){
                        ((DefinedNotFindable) notFoundHandler).commandNotFound(sender,object, null, new String[0]);
                    }else{
                        notFoundHandler.handle(sender, null, new String[0]);
                    }
                }
            }
        } else {
            if(notFoundHandler != null){
                notFoundHandler.handle(sender, null, args);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<String> complete(CommandSender sender, T object, String[] args) {
        if(args.length <= 1){
            MultipleObjectCompletable<T> completable = objectCompletable;
            if(completable != null){
                return completable.complete(sender,object,args.length == 0 ? "" : args[0]);
            }else{
                return Collections.emptyList();
            }
        } else if(args.length == 2){
            return internalTabComplete;
        }else{
            String subCommand = args[1];
            Command command = getCommand(subCommand);
            if(command instanceof DefinedCompletable){
                B bridged = getObject(sender, object,args[0]);
                if(object == null) return Collections.emptyList();
                return ((DefinedCompletable<B>) command).complete(sender,bridged,Arrays.copyOfRange(args,1,args.length));
            }else if(command instanceof Completable){
                return ((Completable) command).complete(sender,Arrays.copyOfRange(args,1,args.length));
            }
            else return Collections.emptyList();
        }
    }

    @Override
    public T getObject(CommandSender sender, String name) {
        throw new UnsupportedOperationException("Can't be called for a MultipleMainObjectCommand. It is bridged from a MainObjectCommand.");
    }

    public abstract B getObject(CommandSender commandSender, T original, String name);
}
