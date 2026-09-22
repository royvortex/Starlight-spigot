package me.royvortex.starlight.module.chat.processor.global;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.text.night.NightMessage;
import me.royvortex.starlight.module.chat.ChatModule;
import me.royvortex.starlight.module.chat.context.ChatContext;
import me.royvortex.starlight.module.chat.processor.ChatProcessor;

public class ColorProcessor implements ChatProcessor<ChatContext> {

    @Override
    public void preProcess(@NonNull ChatModule module, @NonNull ChatContext context) {
        context.setMessage(NightMessage.stripTags(context.getMessage())); // Strip all legacy colors (+ all possible tags)
    }

    @Override
    public void postProcess(@NonNull ChatModule module, @NonNull ChatContext context) {

    }
}
