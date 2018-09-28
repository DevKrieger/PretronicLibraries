package net.prematic.libraries.permission;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 03.09.18 20:57
 *
 */

import java.util.Map;

public abstract class PermissionManager {


    public boolean hasPermission(String permission, PermissionUser user) {
        boolean hasPermission = false;
        for (PermissionGroup group : user.getPermissionGroups()){
            for(Map.Entry<String, Boolean> entry : group.getPermissions().entrySet()){

            }
        }
        return true;
    }

    public abstract void onPermissionInternCheckBefore();

    public abstract void onPermissionInternCheckAfter();
}
