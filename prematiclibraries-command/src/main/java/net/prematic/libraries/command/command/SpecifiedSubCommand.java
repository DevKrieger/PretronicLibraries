/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 31.03.19 17:56
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
import net.prematic.libraries.command.sender.CommandSender;
import net.prematic.libraries.utility.owner.ObjectOwner;

import java.util.Collection;

public abstract class SpecifiedSubCommand<T> extends Command{

    public SpecifiedSubCommand(String name) {
        super(name);
    }

    public SpecifiedSubCommand(String name, String description) {
        super(name, description);
    }

    public SpecifiedSubCommand(String name, String description, String permission) {
        super(name, description, permission);
    }

    public SpecifiedSubCommand(String name, String description, String permission, String... aliases) {
        super(name, description, permission, aliases);
    }

    public SpecifiedSubCommand(String name, String description, String permission, Collection<String> aliases) {
        super(name, description, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        throw new UnsupportedOperationException("You have to register this command in a specified command.");
    }

    @Override
    public CommandEntry<SpecifiedSubCommand<T>> toEntry(ObjectOwner owner) {
        return new CommandEntry<>(owner, this);
    }

    public abstract void execute(CommandSender sender, T object, String[] args);
}
