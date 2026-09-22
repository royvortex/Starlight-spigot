package me.royvortex.starlight.module.essential.command;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import me.royvortex.starlight.STPlaceholders;
import me.royvortex.starlight.StarlightPlugin;
import me.royvortex.starlight.command.CommandArguments;
import me.royvortex.starlight.command.provider.type.AbstractCommandProvider;
import me.royvortex.starlight.module.essential.EssentialModule;
import me.royvortex.starlight.module.essential.EssentialPerms;
import me.royvortex.starlight.teleport.TeleportContext;
import me.royvortex.starlight.teleport.TeleportManager;
import me.royvortex.starlight.teleport.TeleportType;
import me.royvortex.starlight.user.UserManager;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;
import static me.royvortex.starlight.STPlaceholders.GENERIC_WORLD;
import static me.royvortex.starlight.STPlaceholders.PLAYER_DISPLAY_NAME;

public class DimensionCommandProvider extends AbstractCommandProvider {

    private static final Permission PERMISSION        = EssentialPerms.COMMAND.permission("dimension");
    private static final Permission PERMISSION_OTHERS = EssentialPerms.COMMAND.permission("dimension.others");

    private static final TextLocale DESCRIPTION = LangEntry.builder("Command.Dimension.Desc").text("Teleport to a world.");

    private static final MessageLocale MESSAGE_FEEDBACK = LangEntry.builder("Command.Dimension.Target").chatMessage(
        GRAY.wrap("You've teleported player " + SOFT_YELLOW.wrap(PLAYER_DISPLAY_NAME) + " to the " + SOFT_YELLOW.wrap(GENERIC_WORLD) + ".")
    );

    private static final MessageLocale MESSAGE_NOTIFY = LangEntry.builder("Command.Dimension.Notify").chatMessage(
        Sound.ENTITY_ENDERMAN_TELEPORT,
        GRAY.wrap("You have teleported to the " + ORANGE.wrap(GENERIC_WORLD) + ".")
    );

    private final EssentialModule module;
    private final UserManager     userManager;
    private final TeleportManager teleportManager;

    public DimensionCommandProvider(@NotNull StarlightPlugin plugin, @NotNull EssentialModule module, @NotNull UserManager userManager, @NotNull TeleportManager teleportManager) {
        super(plugin);
        this.module = module;
        this.userManager = userManager;
        this.teleportManager = teleportManager;
    }

    @Override
    public void registerDefaults() {
        this.registerLiteral("dimension", true, new String[]{"dimension", "dim"}, builder -> builder
            .description(DESCRIPTION)
            .permission(PERMISSION)
            .withArguments(
                Arguments.world(CommandArguments.WORLD),
                Arguments.playerName(CommandArguments.PLAYER).permission(PERMISSION_OTHERS).optional()
            )
            .withFlags(CommandArguments.FLAG_SILENT)
            .executes(this::moveToWorld)
        );
    }

    private boolean moveToWorld(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        return this.loadPlayerOrSenderAndRunInMainThread(context, arguments, this.module, this.userManager, target -> {
            World world = arguments.getWorld(CommandArguments.WORLD);
            Location location = world.getSpawnLocation();

            TeleportContext teleportContext = TeleportContext.builder(this.module, target, location)
                .sender(context.getSender())
                .callback(() -> {
                    if (context.getSender() != target) {
                        this.module.sendPrefixed(MESSAGE_FEEDBACK, context.getSender(), builder -> builder
                            .with(CommonPlaceholders.PLAYER.resolver(target))
                            .with(STPlaceholders.GENERIC_WORLD, () -> BukkitThing.getValue(world))
                        );
                    }

                    if (!context.hasFlag(CommandArguments.FLAG_SILENT)) {
                        this.module.sendPrefixed(MESSAGE_NOTIFY, target, builder -> builder
                            .with(STPlaceholders.GENERIC_WORLD, () -> BukkitThing.getValue(world))
                        );
                    }
                })
                .build();

            this.teleportManager.teleport(teleportContext, TeleportType.OTHER);
        });
    }
}
