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

package net.prematic.libraries.command.command;

import net.prematic.libraries.command.manager.CommandManager;
import net.prematic.libraries.utility.interfaces.ObjectOwner;

import java.util.Collection;

public interface Command extends CommandExecutor{

    String getName();

    String getDescription();

    String getPermission();

    String getPrefix();

    ObjectOwner getOwner();

    Collection<String> getAliases();

    boolean hasAlias(String command);

    void setPrefix(String prefix);

    void init(CommandManager manager,ObjectOwner owner);

}