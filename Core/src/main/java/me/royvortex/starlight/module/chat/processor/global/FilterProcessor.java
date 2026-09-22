package me.royvortex.starlight.module.chat.processor.global;

import org.jspecify.annotations.NonNull;

import me.royvortex.starlight.module.chat.ChatModule;
import me.royvortex.starlight.module.chat.context.ChatContext;
import me.royvortex.starlight.module.chat.processor.ChatProcessor;
import me.royvortex.starlight.module.chat.rule.WordFilter;

public class FilterProcessor implements ChatProcessor<ChatContext> {

    private final WordFilter filter;

    public FilterProcessor(@NonNull WordFilter filter) {
        this.filter = filter;
    }

    @Override
    public void preProcess(@NonNull ChatModule module, @NonNull ChatContext context) {
        String message = context.getMessage();

        context.setMessage(this.filter.censor(message, '*'));
    }

    @Override
    public void postProcess(@NonNull ChatModule module, @NonNull ChatContext context) {

    }
}
