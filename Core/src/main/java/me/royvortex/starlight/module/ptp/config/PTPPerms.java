package me.royvortex.starlight.module.ptp.config;

import org.bukkit.permissions.Permission;
import me.royvortex.starlight.config.PermissionTree;
import me.royvortex.starlight.config.Perms;

public class PTPPerms {

    public static final PermissionTree MODULE  = Perms.detached("ptp");
    public static final PermissionTree COMMAND = MODULE.branch("command");
    public static final PermissionTree BYPASS  = MODULE.branch("bypass");

    public static final Permission COMMAND_ROOT            = COMMAND.permission("root");
    public static final Permission COMMAND_ACCEPT          = COMMAND.permission("accept");
    public static final Permission COMMAND_DECLINE         = COMMAND.permission("decline");
    public static final Permission COMMAND_INVITE          = COMMAND.permission("invite");
    public static final Permission COMMAND_REQUEST         = COMMAND.permission("request");
    public static final Permission COMMAND_REQUESTS        = COMMAND.permission("toggle");
    public static final Permission COMMAND_REQUESTS_OTHERS = COMMAND.permission("toggle.others");

    public static final Permission BYPASS_REQUESTS_DISABLED = BYPASS.permission("requests.disabled");
}
