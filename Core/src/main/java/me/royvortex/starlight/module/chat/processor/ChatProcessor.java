package me.royvortex.starlight.module.chat.processor;

import org.jspecify.annotations.NonNull;

import me.royvortex.starlight.module.chat.ChatModule;
import me.royvortex.starlight.module.chat.context.ChatContext;

public interface ChatProcessor<T extends ChatContext> {

    void preProcess(@NonNull ChatModule module, @NonNull T context);

    void postProcess(@NonNull ChatModule module, @NonNull T context);
}
