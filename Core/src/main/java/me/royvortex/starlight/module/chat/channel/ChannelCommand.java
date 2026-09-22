package me.royvortex.starlight.module.chat.channel;

import org.jetbrains.annotations.NotNull;

public record ChannelCommand(boolean enabled, @NotNull String alias) {

}
