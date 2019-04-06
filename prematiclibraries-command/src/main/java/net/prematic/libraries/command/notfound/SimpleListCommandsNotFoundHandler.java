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

import net.prematic.libraries.command.SimpleHelpCommand;
import net.prematic.libraries.command.command.Command;
import net.prematic.libraries.command.manager.CommandManager;
import net.prematic.libraries.command.sender.CommandSender;
import net.prematic.libraries.utility.GeneralUtil;

import java.util.List;
import java.util.function.Function;

public class SimpleListCommandsNotFoundHandler extends ListCommandsNotFoundHandler {

    public static String DEFAULT_START_MESSAGE = "";
    public static String DEFAULT_PAGE_NOTFOUND_MESSAGE= "";

    private final Function<Command,String> formatter;
    private final String startMessage, pageNotFound;

    public SimpleListCommandsNotFoundHandler(CommandManager manager){
        this(manager, SimpleHelpCommand.DEFAULT_FORMATTER,DEFAULT_START_MESSAGE,DEFAULT_PAGE_NOTFOUND_MESSAGE);
    }

    public SimpleListCommandsNotFoundHandler(CommandManager manager, Function<Command, String> formatter, String startMessage, String pageNotFound){
        this(manager,formatter,startMessage,pageNotFound,10);
    }

    public SimpleListCommandsNotFoundHandler(CommandManager manager, Function<Command, String> formatter, String startMessage, String pageNotFound, int perPage) {
        super(manager, perPage);
        this.formatter = formatter;
        this.startMessage = startMessage;
        this.pageNotFound = pageNotFound;
    }

    public String getCommandHelpMessage(CommandSender sender, Command cmd){
        return formatter.apply(cmd);
    }

    public String getStartMessage(CommandSender sender,int page,int maxPage){
        return startMessage.replace("%page%",String.valueOf(page)).replace("%maxPage%",String.valueOf(maxPage))
                .replace("%nextPage%",page>=maxPage?"1":String.valueOf(page+1));
    }

    public String getPageNotFoundMessage(CommandSender sender){
        return pageNotFound;
    }
}
