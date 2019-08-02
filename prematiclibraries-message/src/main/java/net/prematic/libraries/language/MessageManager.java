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

package net.prematic.libraries.language;

import net.prematic.libraries.utility.Iterators;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MessageManager {

    private final Collection<MessagePack> packs;
    private Language defaultLanguage;
    private Collection<MessageCacheEntry> cache;

    public MessageManager(Language defaultLanguage) {
        this.packs = new HashSet<>();
        this.defaultLanguage = defaultLanguage;
    }

    public Collection<MessagePack> getPacks() {
        return packs;
    }

    public Collection<MessagePack> getPacksByLanguage(Language language) {
        return Iterators.filter(this.packs, messagePack -> messagePack.getLanguage().equals(language));
    }

    public String getMessage(String key) {
        return getMessage(this.defaultLanguage, key);
    }

    public String getMessage(LanguageAble object, String key) {
        Language language = object!=null?object.getLanguage():defaultLanguage;
        if(language == null) language = defaultLanguage;
        return getMessage(language,key);
    }

    /*
    DKBANS
        english
        german
    dkcoins
        english
        german
     */

    public String getMessage(Language language, String key) {
        MessageCacheEntry cacheEntry = Iterators.findOne(this.cache, entry -> entry.key.toLowerCase().equals(key));
        if(cacheEntry != null && cacheEntry.messages.containsKey(language)) {
            return cacheEntry.messages.get(language);
        }

        MessagePack messagePack = Iterators.findOne(this.packs, pack -> pack.getLanguage().equals(language));
        if(messagePack != null && messagePack.getMessages().containsKey(key)) {
            return getPackMessageAndCache(language, key, messagePack);
        }

        MessagePack defaultPack = Iterators.findOne(this.packs, pack -> pack.getLanguage().equals(this.defaultLanguage));
        if(defaultPack != null && defaultPack.getMessages().containsKey(key)) {
            getPackMessageAndCache(language, key, defaultPack);
        }
        return "Unknown message:" + key;
    }

    private String getPackMessageAndCache(Language language, String key, MessagePack defaultPack) {
        String message = defaultPack.getMessage(key);
        Map<Language, String> messages = new HashMap<>();
        messages.put(language, message);
        this.cache.add(new MessageCacheEntry(key, messages));return message;

    }


    public void addPack(MessagePack pack) {
        this.packs.add(pack);
    }

    public void importPack(File source) {
        addPack(MessagePack.load(source));
    }

    public void importPack(String type, InputStream inputStream) {
        addPack(MessagePack.load(type, inputStream));
    }
    /*

    @for
    @if
    @else
    @else-if


     */

    private class MessageCacheEntry {

        private final String key;
        private final Map<Language,String> messages;

        public MessageCacheEntry(String key, Map<Language, String> messages) {
            this.key = key;
            this.messages = messages;
        }
    }

    private class MessageCacheLanguageEntry {

        private Language language;
        private String message;

    }
}