/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 12.07.19 11:38
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

import java.io.File;
import java.io.InputStream;
import java.util.Map;

public class MessagePack {

    private final int id;
    private final String name, author;
    private final boolean enabled;
    private final MessageModule module;
    private final Language language;
    private final Map<String,String> messages;

    public MessagePack(int id, String name, String author, boolean enabled, MessageModule module, Language language, Map<String, String> messages) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.enabled = enabled;
        this.module = module;
        this.language = language;
        this.messages = messages;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public MessageModule getModule() {
        return module;
    }

    public Language getLanguage() {
        return language;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public String getMessage(String key) {
        return getMessages().get(key);
    }

    public static MessagePack load(File source) {
        Document document = Document.read(source);
        return document.getAsObject(MessagePack.class);
    }

    public static MessagePack load(String type, InputStream inputStream) {
        Document document = Document.read(type, inputStream);
        return document.getAsObject(MessagePack.class);
    }
}
