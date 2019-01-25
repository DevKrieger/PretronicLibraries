package net.prematic.libraries.language;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 25.01.19 20:48
 *
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LanguageManager {

    private final List<Language> languages;
    private Language defaultLanguage;

    public LanguageManager(Language defaultLanguage) {
        this.languages = new LinkedList<>();
        this.defaultLanguage = defaultLanguage;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public Language getDefaultLanguage() {
        return defaultLanguage;
    }

    public String getMessage(Language language, String key) {
        if(!language.containsMessage(key)) return getDefaultLanguage().getMessage(key);
        return language.getMessage(key);
    }

    public boolean containsLanguage(String name) {
        Iterator<Language> iterator = getLanguages().iterator();
        while (iterator.hasNext())
            if(iterator.next().getName().equalsIgnoreCase(name)) return true;
        return false;
    }

    public void setDefaultLanguage(Language defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public void addLanguage(Language language) {
        getLanguages().add(language);
    }

    public void removeLanguage(Language language) {
        getLanguages().remove(language);
    }
}