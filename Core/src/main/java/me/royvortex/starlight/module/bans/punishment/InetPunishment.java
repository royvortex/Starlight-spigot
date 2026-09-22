package me.royvortex.starlight.module.bans.punishment;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolver;
import me.royvortex.starlight.module.bans.BansPlaceholders;
import me.royvortex.starlight.STUtils;

import java.net.InetAddress;

public class InetPunishment extends AbstractPunishment {

    private final InetAddress address;

    public InetPunishment(@NotNull InetAddress address, @NotNull PunishmentData data, boolean active) {
        super(data, active);
        this.address = address;
    }

    @Override
    @NotNull
    public PlaceholderResolver placeholders() {
        return BansPlaceholders.INET_PUNISHMENT.resolver(this);
    }

    @Override
    public boolean isApplicable(@NotNull Player player) {
        return STUtils.getInetAddress(player).map(address -> address.equals(this.address)).orElse(false);
    }

    @Override
    @NotNull
    public String getName() {
        return this.getRawAddress();
    }

    @NotNull
    public InetAddress getAddress() {
        return this.address;
    }

    @NotNull
    public String getRawAddress() {
        return this.address.getHostAddress();
    }
}
