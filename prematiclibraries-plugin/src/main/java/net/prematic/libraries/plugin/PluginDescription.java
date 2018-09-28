package net.prematic.libraries.plugin;

import java.io.File;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 01.09.18 18:10
 *
 */

public class PluginDescription {

    private File file;
    private String main, name, description, version, author;

    public PluginDescription(File file, String main, String name) {
        this.file = file;
        this.main = main;
        this.name = name;
        this.description = "";
        this.version = "";
        this.author = "";
    }
    public PluginDescription(File file, String main, String name, String description, String version, String author) {
        this.file = file;
        this.main = main;
        this.name = name;
        this.description = description;
        this.version = version;
        this.author = author;
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
        if(this.version == null) return "1.0";
        return this.version;
    }
    public String getAuthor() {
        if(this.author == null) return "";
        return this.author;
    }
    public void setMain(String main) {
        this.main = main;
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
