package me.royvortex.starlight.module.nametags;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import me.royvortex.starlight.config.PermissionTree;
import me.royvortex.starlight.exception.ModuleLoadException;
import me.royvortex.starlight.hook.HookId;
import me.royvortex.starlight.hook.placeholder.PlaceholderRegistry;
import me.royvortex.starlight.module.Module;
import me.royvortex.starlight.module.ModuleContext;
import me.royvortex.starlight.module.nametags.handler.NametagHandler;
import me.royvortex.starlight.module.nametags.handler.PacketsTagHandler;
import me.royvortex.starlight.module.nametags.handler.ProtocolTagHandler;
import me.royvortex.starlight.module.nametags.listener.NametagsListener;

import java.util.Comparator;

public class NametagsModule extends Module {

    private final NametagsSettings settings;

    private NametagHandler tagHandler;

    public NametagsModule(@NotNull ModuleContext context) {
        super(context);
        this.settings = new NametagsSettings();
    }

    @Override
    protected void loadModule(@NotNull FileConfig config) throws ModuleLoadException {
        this.settings.load(config);

        this.loadTagHandler();

        this.addListener(new NametagsListener(this.plugin, this));
    }

    private void loadTagHandler() {
        if (Plugins.isInstalled(HookId.PACKET_EVENTS)) {
            this.tagHandler = new PacketsTagHandler(this.plugin);
        }
        else if (Plugins.isInstalled(HookId.PROTOCOL_LIB)) {
            this.tagHandler = new ProtocolTagHandler(this.plugin);
        }

        if (this.tagHandler != null) {
            this.tagHandler.setup();
            this.addAsyncTask(this::updatePlayerNameTags, this.settings.getNameTagUpdateInterval());
        }
    }

    @Override
    protected void unloadModule() {

    }

    @Override
    protected void registerPermissions(@NotNull PermissionTree root) {

    }

    @Override
    protected void registerCommands() {

    }

    @Override
    public void registerPlaceholders(@NotNull PlaceholderRegistry registry) {

    }

    @Nullable
    public NameTagFormat getPlayerNameTagFormat(@NotNull Player player) {
        return this.settings.getNameTagFormatsMap().values().stream()
            .filter(entry -> entry.isRankAvailable(player))
            .max(Comparator.comparingInt(NameTagFormat::getPriority))
            .orElse(null);
    }

    public void handleJoin(@NotNull PlayerJoinEvent event) {
        this.updatePlayerNameTag(event.getPlayer());
    }

    public void updatePlayerNameTag(@NotNull Player player) {
        if (this.tagHandler == null) return;

        NameTagFormat tag = this.getPlayerNameTagFormat(player);
        if (tag == null) return;

        PlaceholderContext placeholderContext = PlaceholderContext.builder()
            .with(CommonPlaceholders.PLAYER.resolver(player))
            .andThen(CommonPlaceholders.forPlaceholderAPI(player))
            .build();

        this.tagHandler.sendTeamPacket(player, tag, placeholderContext);
    }

    public void updatePlayerNameTags() {
        Players.getOnline().forEach(this::updatePlayerNameTag);
    }
}
