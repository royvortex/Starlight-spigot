package me.royvortex.starlight.module.warmups.config;

import org.bukkit.permissions.Permission;
import me.royvortex.starlight.config.PermissionTree;
import me.royvortex.starlight.config.Perms;

public class WarmupsPerms {

    public static final PermissionTree MODULE = Perms.detached("warmups");
    public static final PermissionTree BYPASS = MODULE.branch("bypass");

    public static final Permission BYPASS_TELEPORT = BYPASS.permission("teleport");
    public static final Permission BYPASS_COMMAND  = BYPASS.permission("command");
}
