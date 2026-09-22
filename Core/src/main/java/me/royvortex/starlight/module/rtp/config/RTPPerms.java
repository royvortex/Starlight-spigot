package me.royvortex.starlight.module.rtp.config;

import org.bukkit.permissions.Permission;
import me.royvortex.starlight.config.PermissionTree;
import me.royvortex.starlight.config.Perms;

public class RTPPerms {

    public static final PermissionTree MODULE  = Perms.detached("rtp");
    public static final PermissionTree COMMAND = MODULE.branch("command");
    public static final PermissionTree BYPASS  = MODULE.branch("bypass");

    public static final Permission COMMAND_RTP = COMMAND.permission("rtp");
}
