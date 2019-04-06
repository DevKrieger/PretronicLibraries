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

import net.prematic.libraries.command.notfound.CommandNotFoundHandler;
import net.prematic.libraries.command.command.Command;
import net.prematic.libraries.command.sender.CommandSender;
import net.prematic.libraries.utility.interfaces.ObjectOwner;

import java.util.List;

public interface CommandManager {

    String getName();

    Command getCommand(String name);

    List<Command> getCommands();

    void setNotFoundHandler(CommandNotFoundHandler notFoundHandler);

    void dispatchCommand(CommandSender sender, String command);

    void registerCommand(ObjectOwner owner, Command command);

    void unregisterCommand(String command);

    void unregisterCommand(Command command);

    void unregisterCommand(ObjectOwner owner);

    void unregisterAll();

}