package net.prematic.libraries.permission;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 03.09.18 20:57
 *
 */

import java.util.Map;

public class PermissionGroup {

    private String name;
    private int priority;
    private Map<String, Boolean> permissions;


    public Map<String, Boolean> getPermissions() {
        return permissions;
    }
}