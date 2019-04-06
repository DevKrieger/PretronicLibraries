package net.prematic.libraries.command.command;

import net.prematic.libraries.command.CommandEntry;
import net.prematic.libraries.command.sender.CommandSender;
import net.prematic.libraries.utility.owner.ObjectOwner;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

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

public abstract class Command {

    private final String name, description, permission;
    private final Collection<String> aliases;
    private String prefix;

    public Command(String name) {
        this(name,"none");
    }

    public Command(String name, String description) {
        this(name,description,null);
    }

    public Command(String name, String description, String permission) {
        this(name, description,permission,new HashSet<>());
    }

    public Command(String name, String description, String permission,String... aliases) {
        this(name, description,permission,Arrays.asList(aliases));
    }

    public Command(String name, String description, String permission, Collection<String> aliases) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.aliases = aliases;
        this.prefix =  "[PrematicLibraries] ";
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPermission() {
        return this.permission;
    }

    public String getPrefix() {
        return prefix;
    }

    public Collection<String> getAliases() {
        return this.aliases;
    }

    public boolean hasAlias(String command) {
        return name.equalsIgnoreCase(command) || aliases.contains(command);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public CommandEntry toEntry(ObjectOwner owner) {
        return new CommandEntry<>(owner,this);
    }

    public abstract void execute(CommandSender sender, String[] args);

}