package me.royvortex.starlight.module.vanish.command;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.core.CoreLang;
import me.royvortex.starlight.STPlaceholders;
import me.royvortex.starlight.StarlightPlugin;
import me.royvortex.starlight.command.CommandArguments;
import me.royvortex.starlight.command.mode.ToggleMode;
import me.royvortex.starlight.command.provider.type.AbstractCommandProvider;
import me.royvortex.starlight.module.vanish.VanishModule;
import me.royvortex.starlight.module.vanish.config.VanishLang;
import me.royvortex.starlight.module.vanish.config.VanishPerms;
import me.royvortex.starlight.user.UserManager;
import me.royvortex.starlight.user.property.UserProperty;

public class VanishCommand extends AbstractCommandProvider {

    private static final String COMMAND_OFF    = "off";
    private static final String COMMAND_ON     = "on";
    private static final String COMMAND_TOGGLE = "toggle";

    private final VanishModule module;
    private final UserManager userManager;

    public VanishCommand(@NotNull StarlightPlugin plugin, @NotNull VanishModule module, @NotNull UserManager userManager) {
        super(plugin);
        this.module = module;
        this.userManager = userManager;
    }

    @Override
    public void registerDefaults() {
        this.registerLiteral(COMMAND_TOGGLE, true, new String[]{"vanish"}, builder -> builder
            .description(VanishLang.COMMAND_VANISH_DESC)
            .permission(VanishPerms.COMMAND_VANISH)
            .withArguments(Arguments.playerName(CommandArguments.PLAYER).permission(VanishPerms.COMMAND_VANISH_OTHERS).optional())
            .withFlags(CommandArguments.FLAG_SILENT)
            .executes((context, arguments) -> this.toggleVanish(context, arguments, ToggleMode.TOGGLE))
        );
    }

    private boolean toggleVanish(@NotNull CommandContext context, @NotNull ParsedArguments arguments, @NotNull ToggleMode mode) {
        this.loadPlayerOrSenderWithDataAndRunInMainThread(context, arguments, this.module, this.userManager, (user, target) -> {
            UserProperty<Boolean> setting = VanishModule.VANISH;

            boolean state = mode.apply(user.getPropertyOrDefault(setting));
            user.setProperty(setting, state);
            user.markDirty();

            module.vanish(target, state);

            if (context.getSender() != target) {
                VanishLang.COMMAND_VANISH_TARGET.message().send(context.getSender(), replacer -> replacer
                    .replace(STPlaceholders.GENERIC_STATE, CoreLang.getEnabledOrDisabled(state))
                    .replace(STPlaceholders.forPlayer(target))
                );
            }

            if (!context.hasFlag(CommandArguments.FLAG_SILENT)) {
                VanishLang.COMMAND_VANISH_NOTIFY.message().send(target, replacer -> replacer
                    .replace(STPlaceholders.GENERIC_STATE, CoreLang.getEnabledOrDisabled(state))
                );
            }
        });

        return true;
    }
}
