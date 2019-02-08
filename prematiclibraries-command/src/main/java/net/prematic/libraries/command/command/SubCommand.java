package net.prematic.libraries.command.command;

import net.prematic.libraries.command.sender.CommandSender;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Philipp Elvin Friedhoff
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

public abstract class SubCommand extends MainCommand {

    private final MainCommand mainCommand;

    public SubCommand(String name, MainCommand mainCommand) {
        super(name);
        this.mainCommand = mainCommand;
    }

    public SubCommand(String name, String description, MainCommand mainCommand) {
        super(name, description);
        this.mainCommand = mainCommand;
    }

    public SubCommand(String name, String description, String permission, MainCommand mainCommand) {
        super(name, description, permission);
        this.mainCommand = mainCommand;
    }

    public SubCommand(String name, String description, String permission, String usage, MainCommand mainCommand, String... aliases) {
        super(name, description, permission, usage, aliases);
        this.mainCommand = mainCommand;
    }

    public MainCommand getMainCommand() {
        return mainCommand;
    }

    public abstract void executeSubCommand(CommandSender sender, String[] args);
}