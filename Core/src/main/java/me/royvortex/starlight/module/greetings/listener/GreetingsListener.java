package me.royvortex.starlight.module.greetings.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.manager.AbstractListener;
import me.royvortex.starlight.StarlightPlugin;
import me.royvortex.starlight.module.greetings.GreetingsModule;

public class GreetingsListener extends AbstractListener<StarlightPlugin> {

    private final GreetingsModule module;

    public GreetingsListener(@NotNull StarlightPlugin plugin, @NotNull GreetingsModule module) {
        super(plugin);
        this.module = module;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        this.module.handleJoinEvent(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        this.module.handleQuitEvent(event);
    }
}
