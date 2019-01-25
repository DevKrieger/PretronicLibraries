package net.prematic.libraries.language;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 25.01.19 20:46
 *
 */

import net.prematic.libraries.utility.Document;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Language {

    private boolean enabled, hidden;
    private final String name, localName, tag;
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

    public void addMessage(String key, String message) {
        getMessages().put(key, message);
    }

    public void removeMessage(String key) {
        getMessages().remove(key);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}