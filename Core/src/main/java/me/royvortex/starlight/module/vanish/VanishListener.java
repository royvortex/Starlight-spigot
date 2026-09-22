package me.royvortex.starlight.module.vanish;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.manager.AbstractListener;
import me.royvortex.starlight.StarlightPlugin;
import me.royvortex.starlight.module.vanish.config.VanishPerms;

public class VanishListener extends AbstractListener<StarlightPlugin> {

    private final VanishModule module;

    public VanishListener(@NotNull StarlightPlugin plugin, @NotNull VanishModule module) {
        super(plugin);
        this.module = module;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (this.module.isVanished(player)) {
            this.module.vanish(player, true);
        }

        if (player.hasPermission(VanishPerms.BYPASS_SEE)) return;

        for (Player vanished : plugin.getServer().getOnlinePlayers()) {
            if (vanished == player || !this.module.isVanished(vanished)) continue;

            player.hidePlayer(plugin, vanished);
        }
    }
}
