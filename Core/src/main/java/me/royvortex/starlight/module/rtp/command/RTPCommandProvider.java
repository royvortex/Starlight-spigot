package me.royvortex.starlight.module.rtp.command;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import me.royvortex.starlight.StarlightPlugin;
import me.royvortex.starlight.command.provider.type.AbstractCommandProvider;
import me.royvortex.starlight.module.rtp.RTPModule;
import me.royvortex.starlight.module.rtp.config.RTPLang;
import me.royvortex.starlight.module.rtp.config.RTPPerms;

public class RTPCommandProvider extends AbstractCommandProvider {

    private final RTPModule module;

    public RTPCommandProvider(@NotNull StarlightPlugin plugin, @NotNull RTPModule module) {
        super(plugin);
        this.module = module;
    }

    @Override
    public void registerDefaults() {
        this.registerLiteral("rtp", true, new String[]{"rtp", "wild"}, builder -> builder
            .playerOnly()
            .description(RTPLang.COMMAND_RTP_DESC)
            .permission(RTPPerms.COMMAND_RTP)
            .executes(this::execute)
        );
    }

    private boolean execute(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        this.module.teleportToRandomPlace(player);
        return true;
    }
}
