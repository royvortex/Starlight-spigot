package me.royvortex.starlight.module.spawners;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import me.royvortex.starlight.config.PermissionTree;
import me.royvortex.starlight.exception.ModuleLoadException;
import me.royvortex.starlight.hook.placeholder.PlaceholderRegistry;
import me.royvortex.starlight.module.Module;
import me.royvortex.starlight.module.ModuleContext;

public class SpawnersModule extends Module {

    public SpawnersModule(@NotNull ModuleContext context) {
        super(context);
    }

    @Override
    protected void loadModule(@NotNull FileConfig config) throws ModuleLoadException {

    }

    @Override
    protected void unloadModule() {

    }

    @Override
    protected void registerPermissions(@NotNull PermissionTree root) {

    }

    @Override
    protected void registerCommands() {

    }

    @Override
    public void registerPlaceholders(@NotNull PlaceholderRegistry registry) {

    }
}
