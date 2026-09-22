package me.royvortex.starlight.module.essential.command;

import org.bukkit.inventory.Inventory;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.NightMessage;
import me.royvortex.starlight.StarlightPlugin;
import me.royvortex.starlight.command.CommandArguments;
import me.royvortex.starlight.command.provider.type.AbstractCommandProvider;
import me.royvortex.starlight.module.essential.EssentialModule;
import me.royvortex.starlight.module.essential.EssentialPerms;
import me.royvortex.starlight.module.essential.EssentialSettings;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class DisposalCommandProvider extends AbstractCommandProvider {

    private static final Permission PERMISSION        = EssentialPerms.COMMAND.permission("disposal");
    private static final Permission PERMISSION_OTHERS = EssentialPerms.COMMAND.permission("disposal.others");

    private static final TextLocale DESCRIPTION = LangEntry.builder("Command.Disposal.Desc").text(
        "Open Virtual Disposal.");

    private static final MessageLocale MESSAGE_FEEDBACK = LangEntry.builder("Command.Disposal.Target").chatMessage(
        GRAY.wrap("You have opened " + ORANGE.wrap("Virtual Disposal") + " for " + WHITE.wrap(
            CommonPlaceholders.PLAYER_DISPLAY_NAME) + ".")
    );

    private static final MessageLocale MESSAGE_NOTIFY = LangEntry.builder("Command.Disposal.Notify").chatMessage(
        GRAY.wrap("You have opened " + ORANGE.wrap("Virtual Disposal."))
    );

    private final EssentialModule   module;
    private final EssentialSettings settings;

    public DisposalCommandProvider(@NotNull StarlightPlugin plugin, @NotNull EssentialModule module, @NotNull EssentialSettings settings) {
        super(plugin);
        this.module = module;
        this.settings = settings;
    }

    @Override
    public void registerDefaults() {
        this.registerLiteral("disposal", true, new String[]{"disposal", "trash"}, builder -> builder
            .description(DESCRIPTION)
            .permission(PERMISSION)
            .withArguments(Arguments.playerName(CommandArguments.PLAYER).optional().permission(PERMISSION_OTHERS))
            .withFlags(CommandArguments.FLAG_SILENT)
            .executes(this::openDisposal)
        );
    }

    private boolean openDisposal(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        return this.runForOnlinePlayerOrSender(context, arguments, this.module, target -> {
            Inventory inventory = plugin.getServer().createInventory(null, this.settings.disposalSize.get(),
                NightMessage.asLegacy(this.settings.disposalTitle.get()));
            target.openInventory(inventory);

            if (!context.hasFlag(CommandArguments.FLAG_SILENT)) {
                this.module.sendPrefixed(MESSAGE_NOTIFY, target);
            }
            if (target != context.getSender()) {
                this.module.sendPrefixed(MESSAGE_FEEDBACK, context.getSender(), builder -> builder.with(
                    CommonPlaceholders.PLAYER.resolver(target)));
            }
            return true;
        });
    }
}
