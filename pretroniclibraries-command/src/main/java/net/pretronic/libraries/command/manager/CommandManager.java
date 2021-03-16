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

import net.pretronic.libraries.command.NoPermissionHandler;
import net.pretronic.libraries.command.NotFoundHandler;
import net.pretronic.libraries.command.TextableNoPermissionHandler;
import net.pretronic.libraries.command.command.Command;
import net.pretronic.libraries.command.command.object.ObjectNoPermissionHandler;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.message.StringTextable;
import net.pretronic.libraries.message.Textable;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.util.List;

public interface CommandManager {

    Command getCommand(String name);

    List<Command> getCommands();

    void setNotFoundHandler(NotFoundHandler handler);

    NoPermissionHandler getNoPermissionHandler();

    void setNoPermissionHandler(NoPermissionHandler noPermissionHandler);

    NoPermissionHandler getNoPermissionHandler(ObjectOwner owner);

    void setNoPermissionHandler(ObjectOwner owner,NoPermissionHandler noPermissionHandler);

    default void setNoPermissionMessage(Textable textable) {
        setNoPermissionHandler(new TextableNoPermissionHandler(textable));
    }


    void dispatchCommand(CommandSender sender, String commandLine);

    void registerCommand(Command command);

    void unregisterCommand(String command);

    void unregisterCommand(Command command);

    void unregisterCommand(ObjectOwner owner);

    void unregisterCommands();

    static boolean hasPermission(CommandSender sender, NoPermissionHandler noPermissionHandler, Object object, String permission, String command, String[] args) {
        if(permission == null) return true;
        if(!sender.hasPermission(permission)) {
            if(noPermissionHandler != null) {
                if(noPermissionHandler instanceof ObjectNoPermissionHandler && object != null) {
                    ((ObjectNoPermissionHandler) noPermissionHandler).handle(sender, permission, object, args);
                } else {
                    noPermissionHandler.handle(sender, permission, command, args);
                }
            } else {
                sender.sendMessage(new StringTextable("You don't have enough permission to execute this command"));
            }
            return false;
        }
        return true;
    }
}
