package me.royvortex.starlight.api;

import org.jspecify.annotations.NonNull;
import me.royvortex.starlight.api.provider.AfkProvider;
import me.royvortex.starlight.api.provider.VanishProvider;

import java.util.Optional;

public interface StarlightAPI {

    @NonNull Optional<? extends AfkProvider> afkProvider();

    @NonNull Optional<? extends VanishProvider> vanishProvider();
}
