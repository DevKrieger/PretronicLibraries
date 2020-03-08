/*
 * (C) Copyright 2020 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 21.01.20, 20:11
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

package net.prematic.libraries.message;

import net.prematic.libraries.document.Document;
import net.prematic.libraries.message.bml.variable.VariableSet;
import net.prematic.libraries.message.language.Language;
import net.prematic.libraries.message.language.LanguageAble;
import net.prematic.libraries.message.repository.MessageRepository;

import java.util.Collection;
import java.util.List;

public interface MessageProvider {

    Language getDefaultLanguage();

    void setDefaultLanguage(Language language);

    Collection<MessageRepository> getRepositories();

    MessageRepository getRepository(String name);

    MessageRepository addRepository(String name, String baseUrl);


    Collection<MessagePack> getPacks();

    Collection<MessagePack> getPacks(String module);

    Collection<MessagePack> getPacks(Language language);


    MessagePack importPack(Document pack);

    MessagePack getPack(String name);

    void addPack(MessagePack pack);

    MessagePack addPack(Document pack);

    List<MessagePack> loadPacks(String namespace);


    String getMessage(String key);

    String getMessage(String key, Language language);

    String getMessage(String key, LanguageAble object);


    String buildMessage(String key, VariableSet variables);

    String buildMessage(String key, Language language);

    String buildMessage(String key, VariableSet variables, Language language);


    void calculateMessages();

}
