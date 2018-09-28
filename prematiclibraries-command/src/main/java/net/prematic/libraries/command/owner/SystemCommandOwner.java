package net.prematic.libraries.command.owner;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 02.09.18 14:13
 *
 */

public class SystemCommandOwner implements CommandOwner {

    public static final SystemCommandOwner DEFAULT = new SystemCommandOwner();
    private String name;

    public SystemCommandOwner(){
        this("System");
    }
    public SystemCommandOwner(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
}

