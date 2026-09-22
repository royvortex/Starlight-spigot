package me.royvortex.starlight.module.essential;

import org.bukkit.permissions.Permission;
import me.royvortex.starlight.config.PermissionTree;
import me.royvortex.starlight.config.Perms;

public class EssentialPerms {

    public static final PermissionTree MODULE  = Perms.detached("essential");
    public static final PermissionTree COMMAND = MODULE.branch("command");
    public static final PermissionTree BYPASS  = MODULE.branch("bypass");

    public static final Permission COMMAND_INVULNERABILITY        = COMMAND.permission("invulnerability");
    public static final Permission COMMAND_INVULNERABILITY_OTHERS = COMMAND.permission("invulnerability.others");

    public static final Permission BYPASS_INVULNERABILITY_WORLD  = BYPASS.permission("invulnerability.world");
    public static final Permission BYPASS_INVULNERABILITY_DAMAGE = BYPASS.permission("invulnerability.damage");
}
