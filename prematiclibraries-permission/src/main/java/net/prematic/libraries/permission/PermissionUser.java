package net.prematic.libraries.permission;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 03.09.18 21:15
 *
 */

import java.util.List;
import java.util.Map;

public class PermissionUser {

    private Map<String, Boolean> permissions;
    private List<PermissionGroup> permissionGroups;


    public List<PermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }
}
