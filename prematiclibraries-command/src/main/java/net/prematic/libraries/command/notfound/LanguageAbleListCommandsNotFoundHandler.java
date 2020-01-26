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
import net.prematic.libraries.command.manager.CommandManager;
import net.prematic.libraries.command.sender.CommandSender;
import net.prematic.libraries.message.MessageProvider;
import net.prematic.libraries.message.language.LanguageAble;

public class LanguageAbleListCommandsNotFoundHandler extends ListCommandsNotFoundHandler{

    private MessageProvider messageManager;
    private String commandHelpKey, startMessageKey, pageNotFoundKey;

    public LanguageAbleListCommandsNotFoundHandler(CommandManager manager, int perPage, MessageProvider messageManager){
        this(manager,perPage,messageManager,"command.help","command.help.header","command.help.page.notfound");
    }

    public LanguageAbleListCommandsNotFoundHandler(CommandManager manager, int perPage, MessageProvider messageManager, String commandHelpKey, String startMessageKey, String pageNotFoundKey) {
        super(manager, perPage);
        this.messageManager = messageManager;
        this.commandHelpKey = commandHelpKey;
        this.startMessageKey = startMessageKey;
        this.pageNotFoundKey = pageNotFoundKey;
    }

    @Override
    public String getCommandHelpMessage(CommandSender sender, Command cmd) {
        return getMessage(sender,commandHelpKey).replace("name",cmd.getName()).replace("description",cmd.getDescription())
                .replace("prefix",cmd.getPrefix());
    }

    @Override
    public String getStartMessage(CommandSender sender, int page, int maxPage) {
        return getMessage(sender,startMessageKey).replace("%page%",String.valueOf(page)).replace("%maxPage%",String.valueOf(maxPage))
                .replace("%nextPage%",page>=maxPage?"1":String.valueOf(page+1));
    }

    @Override
    public String getPageNotFoundMessage(CommandSender sender) {
        return getMessage(sender,pageNotFoundKey);
    }

    private String getMessage(CommandSender sender, String key){
        if(sender instanceof LanguageAble && ((LanguageAble) sender).getLanguage() != null) messageManager.getMessage(key,(LanguageAble) sender);
        return messageManager.getMessage(key);
    }
}
