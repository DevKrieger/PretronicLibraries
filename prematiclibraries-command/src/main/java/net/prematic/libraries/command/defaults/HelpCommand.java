package net.prematic.libraries.command.defaults;

import net.prematic.libraries.command.command.Command;
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

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Shows information about all command");
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("Available Commands:");
        StringBuilder helpMessage = new StringBuilder();
        for(Command command : getCommandManager().getCommands()) {
            if(command != this) helpMessage.append(command.getName()).append(" | ").append(command.getDescription())
                    .append(command.hasUsage() ? " | " + command.getUsage() : "").append("\n");
        }
        sender.sendMessage(helpMessage.toString());
    }
}
