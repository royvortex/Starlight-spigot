package me.royvortex.starlight.module.bans.punishment;

import org.jetbrains.annotations.NotNull;
import me.royvortex.starlight.module.bans.time.BanTime;

public record PunishmentContext(@NotNull PunishmentType type,
                                @NotNull PunishmentReason reason,
                                @NotNull BanTime time,
                                boolean silent) {

}
