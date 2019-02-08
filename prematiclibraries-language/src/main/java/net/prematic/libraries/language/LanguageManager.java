package net.prematic.libraries.language;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
        if(language == null || !language.containsMessage(key)) return !getDefaultLanguage().containsMessage(key) ? key : getDefaultLanguage().getMessage(key);
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