package me.royvortex.starlight.module.warps.core;

import org.bukkit.permissions.Permission;
import me.royvortex.starlight.config.PermissionTree;
import me.royvortex.starlight.config.Perms;

public class WarpsPerms {

    public static final PermissionTree MODULE  = Perms.detached("warps");
    public static final PermissionTree COMMAND = MODULE.branch("command");
    public static final PermissionTree WARP    = MODULE.branch("warp");

    public static final Permission EDITOR = MODULE.permission("editor");

    public static final Permission COMMAND_WARPS_ROOT        = COMMAND.permission("warps.root");
    public static final Permission COMMAND_WARPS_CREATE      = COMMAND.permission("warps.create");
    public static final Permission COMMAND_WARPS_UPDATE      = COMMAND.permission("warps.update");
    public static final Permission COMMAND_WARPS_DELETE      = COMMAND.permission("warps.delete");
    public static final Permission COMMAND_WARPS_JUMP        = COMMAND.permission("warps.teleport");
    public static final Permission COMMAND_WARPS_JUMP_OTHERS = COMMAND.permission("warps.teleport.others");
    public static final Permission COMMAND_WARPS_LIST        = COMMAND.permission("warps.list");
    public static final Permission COMMAND_WARPS_LIST_OTHERS = COMMAND.permission("warps.list.others");
    public static final Permission COMMAND_WARPS_EDIT        = COMMAND.permission("warps.edit");
}
