package me.royvortex.starlight.module.inventories;

import me.royvortex.starlight.config.PermissionTree;
import me.royvortex.starlight.config.Perms;

public class InventoriesPerms {

    public static final PermissionTree MODULE = Perms.detached("inventories");
    public static final PermissionTree COMMAND = MODULE.branch("command");
}
