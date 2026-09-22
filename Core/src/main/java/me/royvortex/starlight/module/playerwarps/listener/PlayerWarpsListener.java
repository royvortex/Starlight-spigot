package me.royvortex.starlight.module.playerwarps.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.manager.AbstractListener;
import me.royvortex.starlight.StarlightPlugin;
import me.royvortex.starlight.module.playerwarps.PlayerWarpsModule;

public class PlayerWarpsListener extends AbstractListener<StarlightPlugin> {

    private final PlayerWarpsModule module;

    public PlayerWarpsListener(@NonNull StarlightPlugin plugin, @NonNull PlayerWarpsModule module) {
        super(plugin);
        this.module = module;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(WorldLoadEvent event) {
        this.module.handleWorldLoad(event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldUnload(WorldUnloadEvent event) {
        this.module.handleWorldUnload(event);
    }
}
