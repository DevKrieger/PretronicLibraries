package net.prematic.libraries.language;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 25.01.19 20:48
 *
 */

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LanguageManager {

    private final List<Language> languages;
    private String defaultLanguage;

    public LanguageManager(String defaultLanguage) {
        this.languages = new CopyOnWriteArrayList<>();
        this.defaultLanguage = defaultLanguage;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public Language getDefaultLanguage() {
        return getLanguage(defaultLanguage);
    }

    public Language getLanguage(String name) {
        for(Language language : getLanguages()) if(language.getName().equalsIgnoreCase(name)) return language;
        return null;
    }

    public String getMessage(Language language, String key) {
        if(language == null || !language.containsMessage(key)) return getDefaultLanguage().getMessage(key);
        return language.getMessage(key);
    }

    public boolean containsLanguage(String name) {
        return getLanguage(name) != null;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public void addLanguage(Language language) {
        getLanguages().add(language);
    }

    public void removeLanguage(Language language) {
        getLanguages().remove(language);
    }
}