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

import net.prematic.libraries.command.command.configuration.CommandConfiguration;
import net.prematic.libraries.command.manager.CommandManager;
import net.prematic.libraries.command.sender.CommandSender;
import net.prematic.libraries.utility.Iterators;
import net.prematic.libraries.utility.interfaces.ObjectOwner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class MainObjectCommand<T> extends ObjectCommand<T> implements CommandManager {

    private final List<Command> commands;
    private NotFoundHandler notFoundHandler, objectNotFoundHandler;

    public MainObjectCommand(ObjectOwner owner, CommandConfiguration configuration) {
        super(owner, configuration);
        commands = new ArrayList<>();
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

    public void setObjectNotFoundHandler(NotFoundHandler executor){
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
    public void registerCommand(Command command) {
        this.commands.add(command);
    }

    @Override
    public void unregisterCommand(String command) {
        Iterators.removeOne(this.commands, entry -> entry.getConfiguration().getName().equalsIgnoreCase(command));
    }

    @Override
    public void unregisterCommand(Command command) {
        Iterators.removeOne(this.commands, entry -> entry.equals(command));
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
    public void execute(CommandSender sender,  String[] args) {
        String name = null;//perms object <xy> delete
        if(args.length > 1){
            name = args[0];
            T object = getObject(name);
            if(object != null) execute(sender, object,Arrays.copyOfRange(args,1,args.length));
            else{
                if(objectNotFoundHandler != null){
                    objectNotFoundHandler.handle(sender, name, Arrays.copyOfRange(args,1,args.length));
                }
            }
            return;
        }
        if(notFoundHandler != null){
            notFoundHandler.handle(sender,args[0],Arrays.copyOfRange(args,1,args.length));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(CommandSender sender, Object object, String[] args) {
       if(args.length > 0){
           for (Command command : commands) {
               if(command.getConfiguration().hasAlias(args[0])){
                   if(command instanceof ObjectCommand){
                       ((ObjectCommand<T>)command).execute(sender, (T) object,Arrays.copyOfRange(args,1,args.length));
                   }else command.execute(sender, args);
                   return;
               }
           }
       }
       if(notFoundHandler != null){
           notFoundHandler.handle(sender, args.length == 0 ? "" : args[0],
                   args.length == 0 ? args : Arrays.copyOfRange(args,1,args.length));
       }
    }

    public abstract T getObject(String name);
}
