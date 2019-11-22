/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 03.04.19 11:33
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

package net.prematic.libraries.command.notfound;

import net.prematic.libraries.command.command.Command;
import net.prematic.libraries.command.command.CommandExecutor;
import net.prematic.libraries.command.manager.CommandManager;
import net.prematic.libraries.command.sender.CommandSender;
import net.prematic.libraries.utility.GeneralUtil;

import java.util.List;

public abstract class ListCommandsNotFoundHandler implements CommandExecutor {

    private final CommandManager manager;
    private final int perPage;

    public ListCommandsNotFoundHandler(CommandManager manager, int perPage) {
        this.manager = manager;
        this.perPage = perPage;
    }

    @Override
    public void execute(CommandSender sender, String cmdName, String[] args) {
        List<Command> commands = manager.getCommands();
        int page = 0;
        int maxPage = GeneralUtil.getMaxPages(page,commands);

        if(GeneralUtil.isNumber(cmdName)) page = Integer.parseInt(cmdName);
        else if(cmdName.equalsIgnoreCase("help") && args.length > 0 && GeneralUtil.isNumber(args[0]))
            page = Integer.parseInt(args[0]);

        int from = (perPage*(page - 1))+1;
        int to = page*perPage;

        if(page > maxPage) sender.sendMessage(getPageNotFoundMessage(sender));
        else{
            sender.sendMessage(getStartMessage(sender,page,maxPage));
            for(int i = from;i<=to;i++){
                if(i > commands.size())  break;
                Command command = commands.get(i);
                if(command.getPermission() == null || sender.hasPermission(command.getPermission())) sender.sendMessage(getCommandHelpMessage(sender,command));
            }
        }
    }

    public abstract String getCommandHelpMessage(CommandSender sender, Command cmd);

    public abstract String getStartMessage(CommandSender sender,int page,int maxPage);

    public abstract String getPageNotFoundMessage(CommandSender sender);
}
