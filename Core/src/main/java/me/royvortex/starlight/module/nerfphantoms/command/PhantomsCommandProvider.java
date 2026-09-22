package me.royvortex.starlight.module.nerfphantoms.command;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import me.royvortex.starlight.STPlaceholders;
import me.royvortex.starlight.StarlightPlugin;
import me.royvortex.starlight.command.CommandArguments;
import me.royvortex.starlight.command.mode.ToggleMode;
import me.royvortex.starlight.command.provider.type.AbstractCommandProvider;
import me.royvortex.starlight.module.nerfphantoms.PhantomsModule;
import me.royvortex.starlight.module.nerfphantoms.PhantomsProperties;
import me.royvortex.starlight.module.nerfphantoms.config.PhantomsLang;
import me.royvortex.starlight.module.nerfphantoms.config.PhantomsPerms;
import me.royvortex.starlight.user.UserManager;

public class PhantomsCommandProvider extends AbstractCommandProvider {

    //private static final String COMMAND_OFF    = "off";
    //private static final String COMMAND_ON     = "on";
    private static final String COMMAND_TOGGLE = "toggle";

    private final PhantomsModule module;
    private final UserManager    userManager;

    public PhantomsCommandProvider(@NotNull StarlightPlugin plugin, @NotNull PhantomsModule module, @NotNull UserManager userManager) {
        super(plugin);
        this.module = module;
        this.userManager = userManager;
    }

    @Override
    public void registerDefaults() {
        this.registerLiteral(COMMAND_TOGGLE, true, new String[]{"phantoms"}, builder -> builder
            .description(PhantomsLang.COMMAND_PHANTOMS_TOGGLE_DESC)
            .permission(PhantomsPerms.COMMAND_PHANTOMS_TOGGLE)
            .withArguments(Arguments.playerName(CommandArguments.PLAYER).optional().permission(PhantomsPerms.COMMAND_PHANTOMS_TOGGLE_OTHERS))
            .withFlags(CommandArguments.FLAG_SILENT)
            .executes((context, arguments) -> this.togglePhantoms(context, arguments, ToggleMode.TOGGLE))
        );

        /*this.registerRoot("mode", true, new String[]{"phantommode"},
            Map.of(
                COMMAND_OFF, "off",
                COMMAND_ON, "on",
                COMMAND_TOGGLE, "toggle"
            ),
            builder -> builder.description(PhantomsLang.COMMAND_PHANTOMS_ROOT_DESC).permission(PhantomsPerms.COMMAND_PHANTOMS_ROOT)
        );*/
    }

    private boolean togglePhantoms(@NotNull CommandContext context, @NotNull ParsedArguments arguments, @NotNull ToggleMode mode) {
        return this.loadPlayerOrSenderWithDataAndRunInMainThread(context, arguments, this.module, this.userManager, (user, target) -> {
            boolean state = mode.apply(user.getPropertyOrDefault(PhantomsProperties.ANTI_PHANTOM));
            user.setProperty(PhantomsProperties.ANTI_PHANTOM, state);
            user.markDirty();

            if (context.getSender() != target) {
                this.module.sendPrefixed(PhantomsLang.COMMAND_NO_PHANTOM_TOGGLE_OTHERS, context.getSender(), builder -> builder
                    .with(STPlaceholders.GENERIC_STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(state))
                    .with(CommonPlaceholders.PLAYER.resolver(target))
                );
            }

            if (!context.hasFlag(CommandArguments.FLAG_SILENT)) {
                this.module.sendPrefixed(PhantomsLang.COMMAND_NO_PHANTOM_TOGGLE_NOTIFY, target, replacer -> replacer
                    .with(STPlaceholders.GENERIC_STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(state))
                );
            }
        });
    }
}
