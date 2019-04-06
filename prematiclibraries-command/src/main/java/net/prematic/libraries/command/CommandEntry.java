/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 31.03.19 18:28
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

package net.prematic.libraries.command;

import net.prematic.libraries.command.command.Command;
import net.prematic.libraries.utility.owner.ObjectOwner;

public class CommandEntry<T extends Command> {

    private final ObjectOwner owner;
    private final T command;

    public CommandEntry(ObjectOwner owner, T command) {
        this.owner = owner;
        this.command = command;
    }

    public ObjectOwner getOwner() {
        return owner;
    }

    public T getCommand() {
        return command;
    }
}
