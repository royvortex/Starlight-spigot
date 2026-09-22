package me.royvortex.starlight;

import java.nio.file.Path;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.command.NightCommand;
import su.nightexpress.nightcore.config.PluginDetails;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.Version;
import me.royvortex.starlight.api.StarlightAPI;
import me.royvortex.starlight.api.provider.AfkProvider;
import me.royvortex.starlight.api.provider.VanishProvider;
import me.royvortex.starlight.command.CommandRegistry;
import me.royvortex.starlight.config.Config;
import me.royvortex.starlight.config.Lang;
import me.royvortex.starlight.config.PermissionTree;
import me.royvortex.starlight.config.Perms;
import me.royvortex.starlight.data.DataHandler;
import me.royvortex.starlight.hook.impl.PlaceholderHook;
import me.royvortex.starlight.module.LoadCondition;
import me.royvortex.starlight.module.ModuleContext;
import me.royvortex.starlight.module.ModuleContextProvider;
import me.royvortex.starlight.module.ModuleDefinition;
import me.royvortex.starlight.module.ModuleId;
import me.royvortex.starlight.module.ModuleLoader;
import me.royvortex.starlight.module.ModuleRegistry;
import me.royvortex.starlight.module.afk.AfkModule;
import me.royvortex.starlight.module.backlocation.BackLocationModule;
import me.royvortex.starlight.module.bans.BansModule;
import me.royvortex.starlight.module.chat.ChatModule;
import me.royvortex.starlight.module.deathmessages.DeathMessagesModule;
import me.royvortex.starlight.module.essential.EssentialModule;
import me.royvortex.starlight.module.extras.ExtrasModule;
import me.royvortex.starlight.module.greetings.GreetingsModule;
import me.royvortex.starlight.module.homes.HomesModule;
import me.royvortex.starlight.module.inventories.InventoriesModule;
import me.royvortex.starlight.module.items.ItemsModule;
import me.royvortex.starlight.module.kits.KitsModule;
import me.royvortex.starlight.module.nametags.NametagsModule;
import me.royvortex.starlight.module.nerfphantoms.PhantomsModule;
import me.royvortex.starlight.module.playerwarps.PlayerWarpsModule;
import me.royvortex.starlight.module.ptp.PTPModule;
import me.royvortex.starlight.module.rtp.RTPModule;
import me.royvortex.starlight.module.scheduler.SchedulerModule;
import me.royvortex.starlight.module.spawns.SpawnsModule;
import me.royvortex.starlight.module.texts.TextsModule;
import me.royvortex.starlight.module.vanish.VanishModule;
import me.royvortex.starlight.module.warmups.WarmupsModule;
import me.royvortex.starlight.module.warps.WarpsModule;
import me.royvortex.starlight.nms.SunNMS;
import me.royvortex.starlight.nms.mc_1_21_11.MC_1_21_11;
import me.royvortex.starlight.nms.v26p1.NMSv26p1;
import me.royvortex.starlight.teleport.TeleportManager;
import me.royvortex.starlight.user.UserManager;

public class StarlightPlugin extends NightPlugin implements StarlightAPI, ModuleContextProvider {

    private static StarlightAPI api;

    private CommandRegistry commandRegistry;
    private ModuleRegistry  moduleRegistry;

    private DataHandler dataHandler;
    private UserManager userManager;

    private TeleportManager teleportManager;

    private SunNMS sunNMS;

    @NonNull
    public static StarlightAPI getAPI() {
        return api;
    }

    public StarlightPlugin() {
        api = this;
    }

    @Override
    @NonNull
    protected PluginDetails getDefaultDetails() {
        return PluginDetails.create("Starlight", new String[]{"starlight", "sl"})
            .setConfigClass(Config.class);
    }

    @Override
    protected void addRegistries() {
        this.registerLang(Lang.class);
    }

    @Override
    protected boolean disableCommandManager() {
        return true;
    }

    @Override
    protected void onStartup() {
        this.commandRegistry = new CommandRegistry(this);
        this.moduleRegistry = new ModuleRegistry();
    }

    @Override
    public void enable() {
        this.setupInternalNMS();

        this.dataHandler = new DataHandler(this);
        this.dataHandler.setup();

        this.userManager = new UserManager(this, this.dataHandler);
        this.userManager.setup();

        this.teleportManager = new TeleportManager(this, this.sunNMS);
        this.teleportManager.setup();

        /*if (this.moduleRegistry.isCompleted()) {
            this.info("Reloading all modules...");
            this.moduleRegistry.reload();
        }
        else {*/
        //this.info("Initializing modules...");
        this.loadModules();
        //}

        this.commandRegistry.setup();
        this.registerCommands();
        this.registerPermissions(Perms.ROOT);

        if (Plugins.hasPlaceholderAPI()) {
            PlaceholderHook.setup(this);
        }
    }

    @Override
    public void disable() {
        if (Plugins.hasPlaceholderAPI()) {
            PlaceholderHook.shutdown();
        }

        if (this.moduleRegistry != null) this.moduleRegistry.clear();
        if (this.dialogRegistry != null) this.dialogRegistry.clear();
        if (this.userManager != null) this.userManager.shutdown();
        if (this.dataHandler != null) this.dataHandler.shutdown();
        if (this.commandRegistry != null) this.commandRegistry.shutdown();
    }

    @Override
    protected void onShutdown() {
        super.onShutdown();
    }

    private void loadModules() {
        ModuleLoader loader = new ModuleLoader(this, this.moduleRegistry);

        loader.register(ModuleId.AFK, ModuleDefinition.named("AFK"), AfkModule::new);
        loader.register(ModuleId.BANS, ModuleDefinition.named("Bans"), BansModule::new);
        loader.register(ModuleId.BACK_LOCATION, ModuleDefinition.named("Back"),
            context -> new BackLocationModule(context, this.teleportManager));
        loader.register(ModuleId.CUSTOM_TEXT, ModuleDefinition.named("Custom Text"), TextsModule::new);
        loader.register(ModuleId.CHAT, ModuleDefinition.named("Chat"), ChatModule::new);
        loader.register(ModuleId.DEATH_MESSAGES, ModuleDefinition.named("Death Messages"), DeathMessagesModule::new);
        loader.register(ModuleId.ESSENTIAL, ModuleDefinition.named("Essential"),
            context -> new EssentialModule(context, this.teleportManager));
        loader.register(ModuleId.EXTRAS, ModuleDefinition.named("Extras"), ExtrasModule::new);
        loader.register(ModuleId.GREETINGS, ModuleDefinition.named("Greetings"), GreetingsModule::new);
        loader.register(ModuleId.HOMES, ModuleDefinition.named("Homes"),
            context -> new HomesModule(context, this.teleportManager));
        loader.register(ModuleId.INVENTORIES, ModuleDefinition.named("Inventories"),
            context -> new InventoriesModule(context, this.sunNMS));
        loader.register(ModuleId.ITEMS, ModuleDefinition.named("Items"), ItemsModule::new);
        loader.register(ModuleId.KITS, ModuleDefinition.named("Kits"), KitsModule::new);
        loader.register(ModuleId.NAME_TAGS, ModuleDefinition.named("Nametags"), NametagsModule::new,
            LoadCondition::packetLibrary);
        loader.register(ModuleId.NERF_PHANTOMS, ModuleDefinition.named("Nerf Phantoms"), PhantomsModule::new);
        loader.register(ModuleId.PLAYER_WARPS, ModuleDefinition.named("Player Warps"),
            context -> new PlayerWarpsModule(context, this.teleportManager));
        loader.register(ModuleId.PTP, ModuleDefinition.named("PTP"),
            context -> new PTPModule(context, this.teleportManager));
        loader.register(ModuleId.RTP, ModuleDefinition.named("RTP"),
            context -> new RTPModule(context, this.teleportManager));
        loader.register(ModuleId.SCHEDULER, ModuleDefinition.named("Scheduler"), SchedulerModule::new);
        loader.register(ModuleId.SPAWNS, ModuleDefinition.named("Spawn"),
            context -> new SpawnsModule(context, this.teleportManager));
        loader.register(ModuleId.VANISH, ModuleDefinition.named("Vanish"), VanishModule::new);
        loader.register(ModuleId.WARMUPS, ModuleDefinition.named("Warmups"),
            context -> new WarmupsModule(context, this.teleportManager));
        loader.register(ModuleId.WARPS, ModuleDefinition.named("Warps"),
            context -> new WarpsModule(context, this.teleportManager));

        //loader.register(ModuleId.SPAWNERS, ModuleDefinition.named("Spawners"), SpawnersModule::new);
        //loader.register(ModuleId.SOCIALS, ModuleDefinition.named("Socials"), SocialsModule::new);

        loader.loadAll();
    }

    @Override
    @NonNull
    public ModuleContext createModuleContext(@NonNull String id, @NonNull Path path,
                                             @NonNull ModuleDefinition definition) {
        return new ModuleContext(this, this.dataHandler, this.userManager, this.commandRegistry, this.dialogRegistry, id, path, definition);
    }

    private void setupInternalNMS() {
        if (Version.isBehind(Version.MC_1_21_11)) {
            this.error("Your server version is not supported. Some of the features will be disabled.");
            return;
        }

        try {
            this.sunNMS = switch (Version.getCurrent()) {
                case MC_1_21_11 -> new MC_1_21_11();
                default -> new NMSv26p1();
            };
        }
        catch (Exception | NoClassDefFoundError e) {
            e.printStackTrace();
        }

        if (this.sunNMS == null) {
            this.error("Unable to hook into server's internals. Some of the features will be disabled.");
        }
    }

    private void registerCommands() {
        this.rootCommand = NightCommand.forPlugin(this, builder -> builder
            .branch(Commands.literal("reload")
                .description(CoreLang.COMMAND_RELOAD_DESC)
                .permission(Perms.COMMAND_RELOAD)
                .executes((context, arguments) -> {
                    this.doReload(context.getSender());
                    return true;
                })
            )
        );
    }

    private void registerPermissions(@NonNull PermissionTree tree) {
        tree.toList().forEach(permission -> {
            if (this.getPluginManager().getPermission(permission.getName()) == null) {
                this.getPluginManager().addPermission(permission);
            }
        });
    }

    @NonNull
    public DataHandler getData() {
        return this.dataHandler;
    }

    @NonNull
    public UserManager getUserManager() {
        return userManager;
    }

    @NonNull
    public ModuleRegistry getModuleRegistry() {
        return this.moduleRegistry;
    }

    @Nullable
    public SunNMS getInternals() {
        return this.sunNMS;
    }

    @NonNull
    public Optional<SunNMS> internals() {
        return Optional.ofNullable(this.sunNMS);
    }

    @NonNull
    public CommandRegistry getCommandRegistry() {
        return this.commandRegistry;
    }

    @NonNull
    public TeleportManager getTeleportManager() {
        return this.teleportManager;
    }

    @Override
    @NonNull
    public Optional<? extends AfkProvider> afkProvider() {
        return this.moduleRegistry.byType(AfkModule.class);
    }

    @Override
    @NonNull
    public Optional<? extends VanishProvider> vanishProvider() {
        return this.moduleRegistry.byType(VanishModule.class);
    }
}
