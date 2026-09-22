package me.royvortex.starlight.module.chat.context;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import me.royvortex.starlight.module.chat.cache.UserChatCache;

public abstract class FormattedContext extends ChatContext {

    protected String format;

    public FormattedContext(@NotNull Player player, @NotNull UserChatCache cache, @NotNull String originalMessage) {
        super(player, cache, originalMessage);
    }

    @NotNull
    public String getFormat() {
        return this.format;
    }

    public void setFormat(@NotNull String format) {
        this.format = format;
    }
}
