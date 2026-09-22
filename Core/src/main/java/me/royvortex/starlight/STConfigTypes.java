package me.royvortex.starlight;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.ConfigType;
import su.nightexpress.nightcore.util.RankTable;
import me.royvortex.starlight.module.ModuleDefinition;

public class STConfigTypes {

    public static final ConfigType<ModuleDefinition> MODULE_DEFINITION = ConfigType.of(
        ModuleDefinition::read,
        FileConfig::set
    );

    public static final ConfigType<RankTable> RANK_TABLE = ConfigType.of(
        RankTable::read,
        FileConfig::set
    );
}
