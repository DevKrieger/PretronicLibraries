package net.prematic.libraries.plugin.security;

import java.security.Permission;

public class TestSecurity extends SecurityManager{

    @Override
    public void checkPermission(Permission perm) {
        System.out.println("Checking permission "+perm.getName()+" | "+perm.getActions());
        //super.checkPermission(perm);
    }



}
