package me.royvortex.starlight.module.playerwarps.category;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import me.royvortex.starlight.module.playerwarps.PlayerWarp;
import me.royvortex.starlight.module.playerwarps.core.PlayerWarpsLang;

import java.util.UUID;

public class OwnCategory implements WarpCategory {

    private final UUID playerId;

    public OwnCategory(@NonNull Player player) {
        this.playerId = player.getUniqueId();
    }

    @Override
    @NonNull
    public String name() {
        return PlayerWarpsLang.CATEGORY_OWN_NAME.text();
    }

    @Override
    public boolean isWarpOfThis(@NonNull PlayerWarp warp) {
        return warp.getOwnerId().equals(this.playerId);
    }
}
