package net.prematic.libraries.plugin;

import java.io.File;
import java.util.Properties;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
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

public class PluginDescription {

    private File file;
    private final String main;
    private String name, description, version, author;
    private Properties properties;

    public PluginDescription(File file, String main, String name) {
        this.file = file;
        this.main = main;
        this.name = name;
    }
    public PluginDescription(File file, String main, String name, String description, String version, String author, Properties properties) {
        this.file = file;
        this.main = main;
        this.name = name;
        this.description = description;
        this.version = version;
        this.author = author;
        this.properties = properties;
    }
    public File getFile() {
        return this.file;
    }
    public String getMain() {
        return this.main;
    }
    public String getName() {
        return this.name;
    }
    public String getDescription() {
        if(this.description == null) return "";
        return this.description;
    }
    public String getVersion() {
        if(this.version == null) return "Unknown";
        return this.version;
    }
    public String getAuthor() {
        if(this.author == null) return "Unknown";
        return this.author;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
}
