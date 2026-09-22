package me.royvortex.starlight.module.chat.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.manager.AbstractListener;
import me.royvortex.starlight.StarlightPlugin;
import me.royvortex.starlight.module.chat.ChatModule;

public class ChatListener extends AbstractListener<StarlightPlugin> {

    private final ChatModule module;

    public ChatListener(@NotNull StarlightPlugin plugin, @NotNull ChatModule module) {
        super(plugin);
        this.module = module;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.module.autoJoinChannels(player);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.module.removeFromAllChannels(player);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onChatProcessCommand(PlayerCommandPreprocessEvent event) {
        this.module.handleCommandEvent(event);
    }
}

