package me.royvortex.starlight.module.ptp.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.manager.AbstractListener;
import me.royvortex.starlight.StarlightPlugin;
import me.royvortex.starlight.module.ptp.PTPModule;

public class PTPListener extends AbstractListener<StarlightPlugin> {

    private final PTPModule module;

    public PTPListener(@NotNull StarlightPlugin plugin, @NotNull PTPModule module) {
        super(plugin);
        this.module = module;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(@NotNull PlayerQuitEvent event) {
        this.module.clearRequests(event.getPlayer());
    }
}
