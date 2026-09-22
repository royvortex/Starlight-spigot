package me.royvortex.starlight.module.playerwarps.menu;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import me.royvortex.starlight.module.playerwarps.category.WarpCategory;

public record WarpsListData(@NonNull WarpCategory category, @NonNull PlayerWarpSortType sortType, @Nullable String searchText) {

}
