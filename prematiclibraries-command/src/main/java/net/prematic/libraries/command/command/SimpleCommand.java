/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 01.12.19, 15:30
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

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

public abstract class SimpleCommand implements Command{

    private final String name, description, permission;
    private final Collection<String> aliases;

    private String prefix;
    private ObjectOwner owner;

    public SimpleCommand(String name) {
        this(name,"none");
    }

    public SimpleCommand(String name, String description) {
        this(name,description,null);
    }

    public SimpleCommand(String name, String description, String permission) {
        this(name, description,permission,new LinkedHashSet<>());
    }

    public SimpleCommand(String name, String description, String permission,String... aliases) {
        this(name, description,permission, Arrays.asList(aliases));
    }

    public SimpleCommand(String name, String description, String permission, Collection<String> aliases) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.aliases = aliases;
        this.prefix =  "[UNKNOWN]";
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getPermission() {
        return this.permission;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public ObjectOwner getOwner() {
        return owner;
    }

    @Override
    public Collection<String> getAliases() {
        return this.aliases;
    }

    @Override
    public boolean hasAlias(String command) {
        return name.equalsIgnoreCase(command) || aliases.contains(command);
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void init(CommandManager manager,ObjectOwner owner){
        if(owner == null) throw new IllegalArgumentException("This command is already initialised");
        this.owner = owner;
    }
}
