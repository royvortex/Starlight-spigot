package me.royvortex.starlight.module.chat.processor.chat;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.time.TimeFormatType;
import su.nightexpress.nightcore.util.time.TimeFormats;
import me.royvortex.starlight.STPlaceholders;
import me.royvortex.starlight.StarlightPlugin;
import me.royvortex.starlight.module.chat.ChatModule;
import me.royvortex.starlight.module.chat.cache.UserChatCache;
import me.royvortex.starlight.module.chat.channel.ChatChannel;
import me.royvortex.starlight.module.chat.context.MessageContext;
import me.royvortex.starlight.module.chat.core.ChatLang;
import me.royvortex.starlight.module.chat.core.ChatPerms;
import me.royvortex.starlight.module.chat.processor.MessageProcessor;

public class ChannelProcessor implements MessageProcessor {

    private final StarlightPlugin plugin;

    public ChannelProcessor(@NonNull StarlightPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void preProcess(@NonNull ChatModule module, @NonNull MessageContext context) {
        Player player = context.getPlayer();
        ChatChannel channel = context.getChannel();
        UserChatCache cache = context.getCache();

        if (!channel.canSpeakHere(player)) {
            module.sendPrefixed(ChatLang.CHANNEL_SPEAK_NO_PERMISSION, player, builder -> builder.with(channel
                .placeholders()));
            context.cancel();
            return;
        }

        // Check channel cooldown.
        if (cache.hasChannelCooldown(channel.getId())) {
            context.cancel();
            module.sendPrefixed(ChatLang.CHANNEL_MESSAGE_COOLDOWN, player, builder -> builder
                .with(STPlaceholders.GENERIC_TIME, () -> TimeFormats.formatDuration(cache.getChannelCooldownTimestamp(
                    channel.getId()), TimeFormatType.LITERAL))
            );
            return;
        }

        // Remove channel prefix from the message.
        if (channel.hasPrefix() && context.getMessage().charAt(0) == channel.getPrefixChar()) {
            context.setMessage(context.getMessage().substring(1).trim());
        }

        // Do not send empty messages, mimic default chat behavior.
        if (context.getMessage().isBlank()) {
            context.cancel();
            return;
        }

        // Add player to the channel, so they can listen for new messages.
        if (!channel.contains(player)) {
            module.joinChannel(player, channel, true);
        }

        context.getViewers().removeIf(sender -> !channel.isInChannelRadius(sender, player));
    }

    @Override
    public void postProcess(@NonNull ChatModule module, @NonNull MessageContext context) {
        Player player = context.getPlayer();

        if (this.isAlone(player, context)) {
            // While messages can be set silent in the lang config, it won't prevent this useless scheduler task, so use explicit config option to disable it.
            if (module.getSettings().isChannelNoHeardMessageEnabled()) {
                // One tick delay to send after player's message.
                this.plugin.runTask(() -> module.sendPrefixed(ChatLang.CHANNEL_NOBODY_HERE, player));
            }
        }

        if (!player.hasPermission(ChatPerms.BYPASS_CHANNEL_COOLDOWN)) {
            UserChatCache cache = context.getCache();
            ChatChannel channel = context.getChannel();
            int cooldown = channel.getAccessibility().messageCooldown();
            if (cooldown <= 0) return;

            cache.setChannelCooldown(channel.getId(), cooldown);
        }
    }

    private boolean isAlone(@NonNull Player player, @NonNull MessageContext context) {
        return context.getViewers().stream().noneMatch(
            sender -> sender != player && !(sender instanceof ConsoleCommandSender));
    }
}
