package me.royvortex.starlight.user;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.manager.AbstractListener;
import me.royvortex.starlight.StarlightPlugin;
import me.royvortex.starlight.STUtils;

public class UserListener extends AbstractListener<StarlightPlugin> {

    private final UserManager manager;

    public UserListener(@NotNull StarlightPlugin plugin, @NotNull UserManager manager) {
        super(plugin);
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        SunUser user = this.manager.getOrFetch(player);

        user.setFirstTimeJoined(false);
        user.setLastOnline(System.currentTimeMillis());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SunUser user = this.manager.getOrFetch(player);
        STUtils.getInetAddress(player).ifPresent(address -> {
            user.setLatestAddress(address);
            user.markDirty();
        });
    }
}
