/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 21.03.20, 17:04
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

package net.pretronic.libraries.message;

import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.message.bml.Message;
import net.pretronic.libraries.message.bml.MessageProcessor;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.message.language.Language;
import net.pretronic.libraries.message.language.LanguageAble;
import net.pretronic.libraries.message.repository.MessageRepository;

import java.util.Collection;
import java.util.List;

public interface MessageProvider {

    MessageProcessor getProcessor();

    Language getDefaultLanguage();

    void setDefaultLanguage(Language language);

    Collection<MessageRepository> getRepositories();

    MessageRepository getRepository(String name);

    MessageRepository addRepository(String name, String baseUrl);


    Collection<MessagePack> getPacks();

    Collection<MessagePack> getPacks(String module);

    Collection<MessagePack> getPacks(Language language);


    MessagePack getPack(String name);

    void addPack(MessagePack pack);

    MessagePack addPack(Document pack);


    List<MessagePack> loadPacks(String module);

    void unloadPacks(String module);

    default MessagePack importPack(Document pack){
        return MessagePack.fromDocument(pack);
    }

    MessagePack importPack(MessagePack pack);

    default void updatePack(MessagePack pack){
        updatePack(pack,0);
    }

    void updatePack(MessagePack pack, int updateCound);


    Message getMessage(String key);

    Message getMessage(String key, Language language);

    Message getMessage(String key, LanguageAble object);


    Message getMessageOrNull(String key);

    Message getMessageOrNull(String key, Language language);

    Message getMessageOrNull(String key, LanguageAble object);


    String buildMessage(String key, VariableSet variables);

    String buildMessage(String key, Language language);

    String buildMessage(String key, VariableSet variables, Language language);


    void calculateMessages();

}
