/*
 * (C) Copyright 2020 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 19.01.20, 16:33
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

package net.prematic.libraries.message.repository;

import net.prematic.libraries.message.MessagePack;
import net.prematic.libraries.message.MessagePackMeta;
import net.prematic.libraries.message.language.Language;

import java.util.Collection;

public interface MessageRepository {

    String getName();

    String getBaseUrl();

    Collection<String> getAvailableModules();


    Collection<MessagePackMeta> getAvailablePacks();

    Collection<MessagePackMeta> getAvailablePacks(String module);


    Collection<Language> getAvailableLanguages();

    Collection<Language> getAvailableLanguages(String namespace);

    Collection<MessagePackMeta> search(String query);


    MessagePack download(MessagePackMeta meta);

    void update(MessagePack update);

}
