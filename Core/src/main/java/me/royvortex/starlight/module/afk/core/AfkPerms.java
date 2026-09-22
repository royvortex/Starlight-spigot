package me.royvortex.starlight.module.afk.core;

import org.bukkit.permissions.Permission;
import me.royvortex.starlight.config.PermissionTree;
import me.royvortex.starlight.config.Perms;

public class AfkPerms {

    public static final PermissionTree ROOT    = Perms.detached("afk");
    public static final PermissionTree COMMAND = ROOT.branch("command");

    public static final Permission COMMAND_AFK        = COMMAND.permission("afk");
    public static final Permission COMMAND_AFK_OTHERS = COMMAND.permission("afk.others");
}
