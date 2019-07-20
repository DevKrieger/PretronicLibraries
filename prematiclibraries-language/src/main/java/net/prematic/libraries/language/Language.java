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

import net.prematic.libraries.utility.annonations.Nullable;

public class Language {

    public static final Language ENGLISH = new Language("English","English","en");

    public static final Language GERMAN = new Language("German","Deutsch","de");

    public static final Language FRENCH = new Language("French","Français","fr");

    public static final Language SPANISH = new Language("Spanish","Español","es");

    public static final Language ITALIAN = new Language("Italian","Italiano","it");


    private final String name, localizedName, code, country;

    public Language(String name, String localizedName, String code) {
        this(name,localizedName,code,null);
    }

    public Language(String name, String localizedName, String code, String country) {
        this.name = name;
        this.localizedName = localizedName;
        this.code = code;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public String getCode() {
        return code;
    }

    @Nullable
    public String getCountry() {
        return country;
    }

    public static Language getLanguage(String code){

    }

    public static Language getLanguage(String name, String localizedName, String code){

    }

    public static Language getLanguage(String name, String localizedName, String code, String region){

    }

}