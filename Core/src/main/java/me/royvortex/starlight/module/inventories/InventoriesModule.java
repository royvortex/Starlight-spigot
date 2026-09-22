package me.royvortex.starlight.module.inventories;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;
import me.royvortex.starlight.config.PermissionTree;
import me.royvortex.starlight.exception.ModuleLoadException;
import me.royvortex.starlight.hook.placeholder.PlaceholderRegistry;
import me.royvortex.starlight.module.Module;
import me.royvortex.starlight.module.ModuleContext;
import me.royvortex.starlight.module.inventories.command.ContainerCommandProvider;
import me.royvortex.starlight.module.inventories.command.EnderchestCommandsProvider;
import me.royvortex.starlight.module.inventories.command.InventoryCommandProvider;
import me.royvortex.starlight.nms.SunNMS;

public class InventoriesModule extends Module {

    private final SunNMS internals;

    public InventoriesModule(@NotNull ModuleContext context, @Nullable SunNMS internals) {
        super(context);
        this.internals = internals;
    }

    @Override
    protected void loadModule(@NotNull FileConfig config) throws ModuleLoadException {

    }

    @Override
    protected void unloadModule() {

    }

    @Override
    protected void registerPermissions(@NotNull PermissionTree root) {
        root.merge(InventoriesPerms.MODULE);
    }

    @Override
    protected void registerCommands() {
        if (this.internals != null) {
            this.commandRegistry.addProvider("container", new ContainerCommandProvider(this.plugin, this, this.internals));
        }

        this.commandRegistry.addProvider("enderchest", new EnderchestCommandsProvider(this.plugin, this, this.userManager, this.internals));
        this.commandRegistry.addProvider("inventory", new InventoryCommandProvider(this.plugin, this, this.userManager, this.internals));
    }

    @Override
    public void registerPlaceholders(@NotNull PlaceholderRegistry registry) {

    }
}
