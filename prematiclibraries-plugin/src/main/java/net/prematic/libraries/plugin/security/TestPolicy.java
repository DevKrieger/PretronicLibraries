package net.prematic.libraries.plugin.security;

import java.security.*;

public class TestPolicy extends Policy {

    @Override
    public PermissionCollection getPermissions(ProtectionDomain domain) {
        System.out.println("get permission for "+domain.getClass());
        Permissions permissions = new Permissions();
        permissions.add(new AllPermission());
        return permissions;
    }
}
