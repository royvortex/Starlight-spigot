package me.royvortex.starlight.module.chat.processor.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.TimeUtil;
import me.royvortex.starlight.module.chat.ChatModule;
import me.royvortex.starlight.module.chat.cache.UserChatCache;
import me.royvortex.starlight.module.chat.context.CommandContext;
import me.royvortex.starlight.module.chat.core.ChatLang;
import me.royvortex.starlight.module.chat.processor.ChatProcessor;

public class CommandCooldownProcessor implements ChatProcessor<CommandContext> {

    @Override
    public void preProcess(@NonNull ChatModule module, @NonNull CommandContext context) {
        Player player = context.getPlayer();
        UserChatCache cache = context.getCache();

        long nextCommandTimestamp = cache.getNextCommandTimestamp();
        if (TimeUtil.isPassed(nextCommandTimestamp)) return;

        module.sendPrefixed(ChatLang.ANTI_FLOOD_COMMAND_COOLDOWN, player);
        context.cancel();
    }

    @Override
    public void postProcess(@NonNull ChatModule module, @NonNull CommandContext context) {
        context.getCache().setNextCommandTimestamp(module.getSettings().getAntiFloodCommandCooldown());
    }
}
