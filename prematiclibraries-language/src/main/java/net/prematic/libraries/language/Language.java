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

import net.prematic.libraries.utility.Document;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Language {

    private boolean enabled, hidden;
    private final String name, localName, tag;//code
    private final Map<String, String> messages;
    private final Document properties;

    public Language(String name, String localName, String tag) {
        this.name = name;
        this.localName = localName;
        this.tag = tag;
        this.enabled = true;
        this.hidden = false;
        this.messages = new ConcurrentHashMap<>();
        this.properties = new Document();
    }

    public Language(boolean enabled, boolean hidden, String name, String localName, String tag) {
        this.enabled = enabled;
        this.hidden = hidden;
        this.name = name;
        this.localName = localName;
        this.tag = tag;
        this.messages = new ConcurrentHashMap<>();
        this.properties = new Document();
    }

    public String getName() {
        return name;
    }

    public String getLocalName() {
        return localName;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public String getMessage(String key) {
        return getMessages().get(key);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isHidden() {
        return hidden;
    }

    public String getTag() {
        return tag;
    }

    public Document getProperties() {
        return properties;
    }

    public boolean containsMessage(String key) {
        return getMessages().containsKey(key);
    }

    public Language addMessage(String key, String message) {
        getMessages().put(key, message);
        return this;
    }

    public Language removeMessage(String key) {
        getMessages().remove(key);
        return this;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}