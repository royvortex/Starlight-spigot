package me.royvortex.starlight.module.backlocation.command;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import me.royvortex.starlight.STPlaceholders;
import me.royvortex.starlight.StarlightPlugin;
import me.royvortex.starlight.command.CommandArguments;
import me.royvortex.starlight.command.provider.type.AbstractCommandProvider;
import me.royvortex.starlight.module.backlocation.BackLocationModule;
import me.royvortex.starlight.module.backlocation.config.BackLocationLang;
import me.royvortex.starlight.module.backlocation.config.BackLocationPerms;
import me.royvortex.starlight.module.backlocation.data.LocationType;
import me.royvortex.starlight.user.UserManager;

public class DeathBackCommandProvider extends AbstractCommandProvider {

    private final BackLocationModule module;
    private final UserManager userManager;

    public DeathBackCommandProvider(@NotNull StarlightPlugin plugin, @NotNull BackLocationModule module, @NotNull UserManager userManager) {
        super(plugin);
        this.module = module;
        this.userManager = userManager;
    }

    @Override
    public void registerDefaults() {
        this.registerLiteral("deathback", true, new String[]{"deathback", "dback"}, builder -> builder
            .description(BackLocationLang.COMMAND_DEATH_BACK_DESC)
            .permission(BackLocationPerms.COMMAND_DEATHBACK)
            .withArguments(Arguments.playerName(CommandArguments.PLAYER).optional().permission(BackLocationPerms.COMMAND_DEATHBACK_OTHERS))
            .withFlags(CommandArguments.FLAG_SILENT)
            .executes(this::moveToDeathLocation)
        );
    }

    private boolean moveToDeathLocation(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        return this.loadPlayerOrSenderAndRunInMainThread(context, arguments, this.module, this.userManager, target -> {

            boolean silent = context.hasFlag(CommandArguments.FLAG_SILENT);
            if (!this.module.teleportToLocation(target, LocationType.DEATH, silent)) {
                if (context.getSender() != target) {
                    this.module.sendPrefixed(BackLocationLang.DEATH_ERROR_NOTHING_FEEDBACK, context.getSender(), builder -> builder.andThen(STPlaceholders.forPlayerWithPAPI(target)));
                }
                return;
            }

            if (context.getSender() != target) {
                this.module.sendPrefixed(BackLocationLang.DEATH_TELEPORT_FEEDBACK, context.getSender(), builder -> builder.andThen(STPlaceholders.forPlayerWithPAPI(target)));
            }
        });
    }
}
