package me.royvortex.starlight.module;

import java.nio.file.Path;

import org.jetbrains.annotations.NotNull;

import su.nightexpress.nightcore.ui.dialog.wrap.DialogRegistry;
import me.royvortex.starlight.StarlightPlugin;
import me.royvortex.starlight.command.CommandRegistry;
import me.royvortex.starlight.data.DataHandler;
import me.royvortex.starlight.user.UserManager;

/**
 * A Parameter Object that bundles all common services and instance-specific
 * data required for loading a new Module.
 * <p>
 * This avoids "parameter proliferation" in constructors and loaders.
 *
 * @param plugin          The main Starlight instance.
 * @param dataHandler     The DataHandler instance.
 * @param userManager     The UserManager instance.
 * @param commandRegistry The Starlight's command registry.
 * @param id              The unique ID for this specific module instance.
 * @param path            The data folder path for this module.
 * @param definition      The configuration-defined definition for this module.
 */
public record ModuleContext(
                            @NotNull StarlightPlugin plugin,
                            @NotNull DataHandler dataHandler,
                            @NotNull UserManager userManager,
                            @NotNull CommandRegistry commandRegistry,
                            @NotNull DialogRegistry dialogRegistry,
                            @NotNull String id,
                            @NotNull Path path,
                            @NotNull ModuleDefinition definition
) {

}
