package org.lotzapp.component;


import org.lotzapp.adminapi.model.Permission;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PermissionComponent {
    private static final List<Permission> PERMISSIONS = new ArrayList<>();

    static {
        PERMISSIONS.add(new Permission(
                UUID.randomUUID(),
                "Sale",
                "regiolog",
                OffsetDateTime.now()
        ));
    }

    public static List<Permission> getPermissions() {
        return PERMISSIONS;
    }
}
