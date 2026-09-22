package me.royvortex.starlight.module.warmups;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.config.FileConfig;
import me.royvortex.starlight.config.PermissionTree;
import me.royvortex.starlight.hook.placeholder.PlaceholderRegistry;
import me.royvortex.starlight.module.Module;
import me.royvortex.starlight.module.ModuleContext;
import me.royvortex.starlight.module.warmups.config.WarmupsConfig;
import me.royvortex.starlight.module.warmups.config.WarmupsLang;
import me.royvortex.starlight.module.warmups.config.WarmupsPerms;
import me.royvortex.starlight.module.warmups.impl.TeleportWarmup;
import me.royvortex.starlight.module.warmups.impl.Warmup;
import me.royvortex.starlight.module.warmups.listener.WarmupsListener;
import me.royvortex.starlight.teleport.TeleportContext;
import me.royvortex.starlight.teleport.TeleportManager;
import me.royvortex.starlight.teleport.TeleportType;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WarmupsModule extends Module {

    private final TeleportManager teleportManager;

    private final Map<UUID, Warmup> warmupByIdMap;

    public WarmupsModule(@NotNull ModuleContext context, @NonNull TeleportManager teleportManager) {
        super(context);
        this.teleportManager = teleportManager;
        this.warmupByIdMap = new ConcurrentHashMap<>();
    }

    @Override
    protected void loadModule(@NotNull FileConfig config) {
        config.initializeOptions(WarmupsConfig.class);
        this.plugin.injectLang(WarmupsLang.class);

        this.addListener(new WarmupsListener(this.plugin, this));
        this.addAsyncTask(this::tickWarmups, WarmupsConfig.WARMUP_TICK_INTERVAL.get());
    }

    @Override
    protected void unloadModule() {
        this.getWarmups().forEach(warmup -> warmup.cancel(true));
        this.warmupByIdMap.clear();
    }

    @Override
    protected void registerPermissions(@NotNull PermissionTree root) {
        root.merge(WarmupsPerms.MODULE);
    }

    @Override
    protected void registerCommands() {

    }

    @Override
    public void registerPlaceholders(@NotNull PlaceholderRegistry registry) {

    }

    private void tickWarmups() {
        this.getWarmups().removeIf(warmup -> {
            this.tickWarmup(warmup);
            return warmup.isCompleted();
        });
    }

    private void tickWarmup(@NotNull Warmup warmup) {
        if (WarmupsConfig.WARMUP_CANCEL_ON_MOVE.get() && warmup.isMoved()) {
            this.cancelWarmup(warmup.getPlayer());
            return;
        }

        warmup.onTick();

        if (warmup.isCompleted()) {
            this.plugin.runTask(task -> warmup.complete()); // Back to the main thread.
            this.warmupByIdMap.remove(warmup.getPlayer().getUniqueId());
        }
    }

    @Nullable
    public Warmup getWarmup(@NotNull Player player) {
        return this.warmupByIdMap.get(player.getUniqueId());
    }

    public boolean hasWarmup(@NotNull Player player) {
        return this.getWarmup(player) != null;
    }

    @NotNull
    public Set<Warmup> getWarmups() {
        return new HashSet<>(this.warmupByIdMap.values());
    }

    public boolean canHandleTeleport(@NotNull TeleportType type) {
        return WarmupsConfig.TELEPORT_HANDLED_TYPES.get().contains(type);
    }

    public boolean canHandleTeleport(@NotNull Player player, @NotNull TeleportType type) {
        return !player.hasPermission(WarmupsPerms.BYPASS_TELEPORT) && this.canHandleTeleport(type);
    }

    public void cancelWarmup(@NotNull Player player) {
        this.cancelWarmup(player, false);
    }

    public void cancelWarmup(@NotNull Player player, boolean silent) {
        Warmup warmup = this.warmupByIdMap.remove(player.getUniqueId());
        if (warmup == null) return;

        warmup.cancel(silent);
    }

    public void addWarmup(@NotNull Player player, @NotNull Warmup warmup) {
        if (warmup.getValue() <= 0) {
            warmup.complete();
            return;
        }

        this.cancelWarmup(player, true);

        warmup.init();

        this.warmupByIdMap.put(player.getUniqueId(), warmup);
    }

    public void handleTeleport(@NotNull TeleportContext context, @NotNull TeleportType type) {
        Player player = context.getTarget();
        Location location = context.getDestination();
        int value = WarmupsConfig.TELEPORT_WARMUPS_BY_RANK.get().getSmallest(player);
        Runnable callback = () -> this.teleportManager.move(context); // Simply pass the same context into next teleportation "stage" after a delay is passed.

        Warmup warmup = new TeleportWarmup(this, player, value, location, callback);

        this.addWarmup(player, warmup);
    }
}
