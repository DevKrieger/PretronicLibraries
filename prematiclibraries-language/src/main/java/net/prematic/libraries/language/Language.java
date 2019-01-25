package net.prematic.libraries.language;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 25.01.19 20:46
 *
 */

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Language {

    private final String name, localName;
    private final Map<String, String> messages;

    public Language(String name, String localName) {
        this.name = name;
        this.localName = localName;
        this.messages = new ConcurrentHashMap<>();
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

    public boolean containsMessage(String key) {
        return getMessages().containsKey(key);
    }

    public void addMessage(String key, String message) {
        getMessages().put(key, message);
    }

    public void removeMessage(String key) {
        getMessages().remove(key);
    }
}