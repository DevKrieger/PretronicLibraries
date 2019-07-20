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

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageManager {

    private final Collection<MessagePack> packs;
    private Language defaultLanguage;

    private Collection<MessageCacheEntry> cache;


    public MessageManager(String defaultLanguage) {
        Exception exception;
        this.defaultLanguage = defaultLanguage;
    }

    public Collection<MessagePack> getPacks() {
        return packs;
    }

    public String getMessage(String key){

    }

    public String getMessage(LanguageAble object, String key){
        Language language = object!=null?object.getLanguage():defaultLanguage;
        if(language == null) language = defaultLanguage;
        return getMessage(language,key);
    }

    public String getMessage(Language language, String key){

    }



    public void addPack(MessagePack pack){
        this.packs.add(pack);
    }






    public void inportPacks(File source){

    }

    public void importPack(InputStream inputStream){

    }
    /*

    @for
    @if
    @else
    @else-if


     */

    private class MessageCacheEntry {

        private String key;
        private Language language;
        private String message;

    }

    private class MessageCacheLanguageEntry {

        private Language language;
        private String message;

    }
}