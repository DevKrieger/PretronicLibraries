/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 02.08.19 17:47
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

import net.prematic.libraries.utility.Iterators;

import java.io.File;
import java.io.InputStream;
import java.util.*;

public class DefaultMessageManager implements MessageManager{

    private Language defaultLanguage;
    private final Collection<MessagePack> packs;
    private Collection<MessageCacheEntry> cache;

    public DefaultMessageManager(Language defaultLanguage) {
        this.packs = new HashSet<>();
        this.cache = new ArrayList<>();
        this.defaultLanguage = defaultLanguage;
    }

    @Override
    public Collection<MessagePack> getPacks() {
        return packs;
    }

    @Override
    public Collection<MessagePack> getPacksByLanguage(Language language) {
        return Iterators.filter(this.packs, messagePack -> messagePack.getLanguage().equals(language));
    }

    @Override
    public Collection<String> getMessages(Language language) {
        return null;
    }

    @Override
    public String getMessage(String key) {
        return getMessage(this.defaultLanguage, key);
    }

    @Override
    public String getMessage(LanguageAble object, String key) {
        Language language = object!=null?object.getLanguage():defaultLanguage;
        if(language == null) language = defaultLanguage;
        return getMessage(language,key);
    }

    @Override
    public String getMessage(Language language, String key) {
        MessageCacheEntry cacheEntry = getFromCache(key);
        if(cacheEntry != null) {
            String message = cacheEntry.messages.get(language);
            if(message != null) return message;
            else{
                message = cacheEntry.messages.get(defaultLanguage);
                if(message != null) return message;
            }
        }

        String unknownMessage = getMessage(language,"unknown.message");
        if(unknownMessage != null) return unknownMessage;
        return "Unknown message for key " + key+" in language "+language.getName();
    }

    @Override
    public void addPack(MessagePack pack) {
        this.packs.add(pack);
    }

    @Override
    public void importPack(File source) {
        addPack(MessagePack.load(source));
    }

    @Override
    public void importPack(String type, InputStream inputStream) {
        addPack(MessagePack.load(type, inputStream));
    }

    @Override
    public void loadMessages(){
        this.cache.clear();
        packs.forEach(pack -> {
            String moduleKey = pack.getModule().getKey();

            pack.getMessages().forEach((key, message) -> {
                String finalKey = moduleKey+"."+key;

                MessageCacheEntry entry = getFromCache(finalKey);
                if(entry == null){
                    entry = new MessageCacheEntry(finalKey);
                    cache.add(entry);
                }

                entry.messages.put(pack.getLanguage(),message);
            });
        });
    }

    private MessageCacheEntry getFromCache(String key){
        return Iterators.findOne(this.cache, entry -> entry.key.toLowerCase().equals(key));
    }

    private class MessageCacheEntry {

        private final String key;
        private final Map<Language,String> messages;

        public MessageCacheEntry(String key) {
            this.key = key;
            this.messages = new HashMap<>();
        }
    }

}
