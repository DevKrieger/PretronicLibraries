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
import net.pretronic.libraries.command.command.Command;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.command.object.DefinedCompletable;
import net.pretronic.libraries.command.command.object.DefinedNotFindable;
import net.pretronic.libraries.command.command.object.MainObjectCommand;
import net.pretronic.libraries.command.command.object.ObjectCompletable;
import net.pretronic.libraries.command.manager.CommandManager;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.utility.Iterators;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public abstract class MultipleMainObjectCommand<T,B> extends MainObjectCommand<T> implements CommandManager, DefinedCompletable<T> {

    private MultipleObjectNotFindable<T> objectNotFoundHandler;
    private MultipleObjectCompletable<T> objectCompletable;

    public MultipleMainObjectCommand(ObjectOwner owner, CommandConfiguration configuration) {
        super(owner, configuration);

        if(this instanceof MultipleObjectNotFindable) setObjectNotFoundHandler((MultipleObjectNotFindable<T>) this);
        if(this instanceof MultipleObjectCompletable) setObjectCompletableHandler((MultipleObjectCompletable<T>) this);
    }

    public void setObjectNotFoundHandler(MultipleObjectNotFindable<T> handler){
        this.objectNotFoundHandler = handler;
    }

    public void setObjectCompletableHandler(MultipleObjectCompletable<T> completable){
        this.objectCompletable = completable;
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
                        ((DefinedNotFindable) notFoundHandler).commandNotFound(sender,bridged, null, new String[0]);
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
        }else if(args.length == 2) {
            return Iterators.map(getCommands(), command -> command.getConfiguration().getName()
                    ,command -> command.getConfiguration().getName().toLowerCase().startsWith(args[1].toLowerCase()));
        }else{
            String subCommand = args[1];
            Command command = getCommand(subCommand);

            String[] subArg;
            if(command instanceof MainObjectCommand) subArg = Arrays.copyOfRange(args,1,args.length);
            else subArg = Arrays.copyOfRange(args,2,args.length);

            if(command instanceof DefinedCompletable){
                B bridged = getObject(sender, object,args[0]);
                if(object == null) return Collections.emptyList();
                return ((DefinedCompletable<B>) command).complete(sender,bridged,Arrays.copyOfRange(args,1,args.length));
            }else if(command instanceof Completable){
                return ((Completable) command).complete(sender,subArg);
            }else return Collections.emptyList();
        }
    }

    @Override
    public T getObject(CommandSender sender, String name) {
        throw new UnsupportedOperationException("Can't be called for a MultipleMainObjectCommand. It is bridged from a MainObjectCommand.");
    }

    public abstract B getObject(CommandSender commandSender, T original, String name);
}
