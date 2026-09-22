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

public class BackCommandProvider extends AbstractCommandProvider {

    private final BackLocationModule module;
    private final UserManager userManager;

    public BackCommandProvider(@NotNull StarlightPlugin plugin, @NotNull BackLocationModule module, @NotNull UserManager userManager) {
        super(plugin);
        this.module = module;
        this.userManager = userManager;
    }

    @Override
    public void registerDefaults() {
        this.registerLiteral("back", true, new String[]{"back"}, builder -> builder
            .description(BackLocationLang.COMMAND_BACK_DESC)
            .permission(BackLocationPerms.COMMAND_BACK)
            .withArguments(Arguments.playerName(CommandArguments.PLAYER).optional().permission(BackLocationPerms.COMMAND_BACK_OTHERS))
            .withFlags(CommandArguments.FLAG_SILENT)
            .executes(this::moveToPreviousLocation)
        );
    }

    private boolean moveToPreviousLocation(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        return this.loadPlayerOrSenderAndRunInMainThread(context, arguments, this.module, this.userManager, target -> {

            boolean silent = context.hasFlag(CommandArguments.FLAG_SILENT);
            if (!this.module.teleportToLocation(target, LocationType.PREVIOUS, silent)) {
                if (context.getSender() != target) {
                    this.module.sendPrefixed(BackLocationLang.PREVIOUS_ERROR_NOTHING_FEEDBACK, context.getSender(), builder -> builder.andThen(STPlaceholders.forPlayerWithPAPI(target)));
                }
                return;
            }

            if (context.getSender() != target) {
                this.module.sendPrefixed(BackLocationLang.PREVIOUS_TELEPORT_FEEDBACK, context.getSender(), builder -> builder.andThen(STPlaceholders.forPlayerWithPAPI(target)));
            }
        });
    }
}
