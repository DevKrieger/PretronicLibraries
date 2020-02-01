/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 31.03.19 17:54
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

import net.prematic.libraries.command.command.BasicCommand;
import net.prematic.libraries.command.command.Command;
import net.prematic.libraries.command.command.configuration.CommandConfiguration;
import net.prematic.libraries.command.manager.CommandManager;
import net.prematic.libraries.command.sender.CommandSender;
import net.prematic.libraries.utility.interfaces.ObjectOwner;

public class SimpleHelpCommand extends BasicCommand {

    private CommandManager manager;

    public SimpleHelpCommand(ObjectOwner owner) {
        super(owner,CommandConfiguration.newBuilder().name("help").description("Shows all commands of this system.").create());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("Available Commands:");
        for(Command command : this.manager.getCommands()){
            if(command != this) sender.sendMessage(command.getConfiguration().getName()+" | "+command.getConfiguration().getDescription());
        }
    }
}
