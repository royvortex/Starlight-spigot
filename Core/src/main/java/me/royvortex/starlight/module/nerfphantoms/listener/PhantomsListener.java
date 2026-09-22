package me.royvortex.starlight.module.nerfphantoms.listener;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Phantom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.manager.AbstractListener;
import su.nightexpress.nightcore.util.EntityUtil;
import me.royvortex.starlight.StarlightPlugin;
import me.royvortex.starlight.module.nerfphantoms.PhantomsModule;
import me.royvortex.starlight.module.nerfphantoms.config.PhantomsConfig;

public class PhantomsListener extends AbstractListener<StarlightPlugin> {

    //private final PhantomsModule module;

    public PhantomsListener(@NotNull StarlightPlugin plugin, @NotNull PhantomsModule module) {
        super(plugin);
        //this.module = module;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPhantomSpawn(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Phantom phantom)) return;

        if (PhantomsConfig.DISABLE_SPAWN_ENABLED.get()) {
            if (PhantomsConfig.DISABLE_SPAWN_REASONS.get().contains(event.getSpawnReason())) {
                event.setCancelled(true);
                return;
            }
        }

        if (PhantomsConfig.DAMAGE_MODIFIER_ENABLED.get()) {
            this.modifyAttribute(phantom, Attribute.ATTACK_DAMAGE, PhantomsConfig.DAMAGE_MODIFIER_VALUE.get());
        }
        if (PhantomsConfig.HEALTH_MODIFIER_ENABLED.get()) {
            this.modifyAttribute(phantom, Attribute.MAX_HEALTH, PhantomsConfig.HEALTH_MODIFIER_VALUE.get());
        }
    }

    private void modifyAttribute(@NotNull Phantom phantom, @NotNull Attribute attribute, double value) {
        AttributeInstance instance = phantom.getAttribute(attribute);
        if (instance == null) return;

        if (attribute == Attribute.ATTACK_DAMAGE) {
            value = EntityUtil.getAttribute(phantom, attribute) * value;
        }

        instance.setBaseValue(value);

        if (attribute == Attribute.MAX_HEALTH) {
            phantom.setHealth(value);
        }
    }
}
