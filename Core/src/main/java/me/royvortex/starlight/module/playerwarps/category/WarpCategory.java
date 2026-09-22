package me.royvortex.starlight.module.playerwarps.category;

import org.jspecify.annotations.NonNull;
import me.royvortex.starlight.module.playerwarps.PlayerWarp;

public interface WarpCategory {

    @NonNull String name();

    boolean isWarpOfThis(@NonNull PlayerWarp warp);
}
