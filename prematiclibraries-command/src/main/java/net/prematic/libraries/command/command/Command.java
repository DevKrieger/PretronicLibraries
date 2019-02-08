package net.prematic.libraries.command.command;

import net.prematic.libraries.command.manager.CommandManager;
import net.prematic.libraries.command.owner.CommandOwner;
import net.prematic.libraries.command.sender.CommandSender;

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

    private String name, description, permission, usage;
    private CommandOwner owner;
    private CommandManager commandManager;
    private Collection<String> aliases;

    public Command(String name) {
        this.name = name;
        this.description = "none";
        this.aliases = new HashSet<>();
    }

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
        this.aliases = new HashSet<>();
    }

    public Command(String name, String description, String permission) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.aliases = new HashSet<>();
    }

    public Command(String name, String description, String permission, String usage, String... aliases) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.usage = usage;
        this.aliases = new HashSet<>(Arrays.asList(aliases));
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

    public String getUsage() {
        return usage;
    }

    public boolean hasUsage() {
        return usage != null;
    }

    public CommandOwner getOwner() {
        return this.owner;
    }

    public Collection<String> getAliases() {
        return this.aliases;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public boolean hasAliases(String command) {
        if(this.name.equalsIgnoreCase(command)) return true;
        return aliases.contains(command);
    }

    public Command setUsage(String usage) {
        this.usage = usage;
        return this;
    }

    public Command setDescription(String description) {
        this.description = description;
        return this;
    }

    public Command setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public Command addAlias(String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    public void init(CommandOwner owner, CommandManager commandManager) {
        this.owner = owner;
        this.commandManager = commandManager;
    }

    public abstract void execute(CommandSender sender, String[] args);
}