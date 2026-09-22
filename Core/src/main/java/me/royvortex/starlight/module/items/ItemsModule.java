package me.royvortex.starlight.module.items;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import me.royvortex.starlight.config.PermissionTree;
import me.royvortex.starlight.hook.placeholder.PlaceholderRegistry;
import me.royvortex.starlight.module.Module;
import me.royvortex.starlight.module.ModuleContext;
import me.royvortex.starlight.module.items.command.ItemCommandProvider;
import me.royvortex.starlight.module.items.command.LoreCommandsProvider;

public class ItemsModule extends Module {

    private final ItemsSettings settings;

    public ItemsModule(@NotNull ModuleContext context) {
        super(context);
        this.settings = new ItemsSettings();
    }

    @Override
    protected void loadModule(@NotNull FileConfig config) {
        this.settings.load(config);
        this.plugin.injectLang(ItemsLang.class);
    }

    @Override
    protected void unloadModule() {

    }

    @Override
    protected void registerPermissions(@NotNull PermissionTree root) {
        root.merge(ItemsPerms.MODULE);
    }

    @Override
    protected void registerCommands() {
        this.commandRegistry.addProvider("item", new ItemCommandProvider(this.plugin, this, this.settings, this.userManager));
        this.commandRegistry.addProvider("lore", new LoreCommandsProvider(this.plugin, this));
    }

    @Override
    public void registerPlaceholders(@NotNull PlaceholderRegistry registry) {

    }
}
