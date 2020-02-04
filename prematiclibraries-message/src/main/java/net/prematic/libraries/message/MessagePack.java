/*
 * (C) Copyright 2020 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 21.01.20, 20:08
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

import java.util.HashMap;
import java.util.Map;

public class MessagePack {

    private final MessagePackMeta meta;
    private final Map<String,String> messages;

    public MessagePack(MessagePackMeta meta){
        this(meta,new HashMap<>());
    }

    public MessagePack(MessagePackMeta meta, Map<String, String> messages) {
        this.meta = meta;
        this.messages = messages;
    }

    public MessagePackMeta getMeta() {
        return meta;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public String getMessage(String key){
        return messages.get(key);
    }

    public Document toDocument(){
        return Document.newDocument(this);
    }


    public static MessagePack fromDocument(Document document){
        return document.getAsObject(MessagePack.class);
    }
}
