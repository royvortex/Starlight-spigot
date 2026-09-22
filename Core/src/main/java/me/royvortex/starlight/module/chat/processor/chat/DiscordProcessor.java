package me.royvortex.starlight.module.chat.processor.chat;

import org.jspecify.annotations.NonNull;

import me.royvortex.starlight.module.chat.ChatModule;
import me.royvortex.starlight.module.chat.context.MessageContext;
import me.royvortex.starlight.module.chat.discord.DiscordHandler;
import me.royvortex.starlight.module.chat.processor.MessageProcessor;

public class DiscordProcessor implements MessageProcessor {

    private final DiscordHandler discordHandler;

    public DiscordProcessor(@NonNull DiscordHandler discordHandler) {
        this.discordHandler = discordHandler;
    }

    @Override
    public void preProcess(@NonNull ChatModule module, @NonNull MessageContext context) {

    }

    @Override
    public void postProcess(@NonNull ChatModule module, @NonNull MessageContext context) {
        this.discordHandler.sendToChannel(context.getChannel(), context.getMessage());
    }
}
