package me.royvortex.starlight.module.chat.channel;

import org.jetbrains.annotations.NotNull;

public record ChannelPrefix(boolean enabled, @NotNull String value) {

}
