package me.royvortex.starlight.module.nametags.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.manager.AbstractListener;
import me.royvortex.starlight.StarlightPlugin;
import me.royvortex.starlight.module.nametags.NametagsModule;

public class NametagsListener extends AbstractListener<StarlightPlugin> {

    private final NametagsModule module;

    public NametagsListener(@NotNull StarlightPlugin plugin, @NotNull NametagsModule module) {
        super(plugin);
        this.module = module;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.module.handleJoin(event);
    }
}
