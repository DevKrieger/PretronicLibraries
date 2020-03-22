/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 22.03.20, 12:25
 * @web %web%
 *
 * The PretronicLibraries Project is under the Apache License, version 2.0 (the "License");
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

package net.pretronic.libraries.message.bml.builder;

import net.pretronic.libraries.message.MessageProvider;
import net.pretronic.libraries.message.bml.Message;

public class IncludeMessageBuilder implements MessageBuilder {

    private final MessageProvider provider;
    private Message message;

    public IncludeMessageBuilder(MessageProvider provider) {
        this.provider = provider;
    }

    @Override
    public Object build(BuildContext context, String name, Object[] parameters, String extension) {
        if(message == null){
            message = provider.getMessage((String)parameters[0],context.getLanguage());
            if(message == null) message = Message.ofStaticText("{MESSAGE NOT FOUND}");
        }
        return message.build(context);
    }
}
