package me.royvortex.starlight.module.nerfphantoms.config;

import org.bukkit.permissions.Permission;
import me.royvortex.starlight.config.PermissionTree;
import me.royvortex.starlight.config.Perms;

public class PhantomsPerms {

    public static final PermissionTree ROOT    = Perms.detached("nerfphantoms");
    public static final PermissionTree COMMAND = ROOT.branch("command");

    public static final Permission COMMAND_PHANTOMS_ROOT          = COMMAND.permission("phantoms.root");
    public static final Permission COMMAND_PHANTOMS_TOGGLE        = COMMAND.permission("phantoms.toggle");
    public static final Permission COMMAND_PHANTOMS_TOGGLE_OTHERS = COMMAND.permission("phantoms.toggle.others");

}
