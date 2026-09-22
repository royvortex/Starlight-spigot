package me.royvortex.starlight.data;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.column.ColumnDataReader;
import me.royvortex.starlight.command.CommandKey;
import me.royvortex.starlight.user.property.UserProperty;
import me.royvortex.starlight.user.property.UserPropertyRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserColumns {

    public static final Column<Integer> ID           = Column.intType("id").autoIncrement().primaryKey().build();
    public static final Column<UUID>    UUID         = Column.uuidType("uuid").build();
    public static final Column<String>  NAME         = Column.stringType("name", 24).build();
    public static final Column<Long>    DATE_CREATED = Column.longType("dateCreated").build();
    public static final Column<Long>    LAST_ONLINE  = Column.longType("last_online").build();
    public static final Column<String>  INET_ADDRESS = Column.tinyText("ip").build();

    public static final Column<Map<CommandKey, Long>> COMMAND_COOLDOWNS = Column.json("commandCooldowns", ColumnDataReader.jsonMap(DataHandler.GSON, CommandKey.class, Long.class))
        .defaultValue("{}")
        .build();

    public static final Column<Map<String, Object>> PROPERTIES = Column.json("properties", (resultSet, column) -> {
        Map<String, Object> properties = new HashMap<>();

        String jsonString = resultSet.getString(column);
        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();

        json.asMap().forEach((key, element) -> {
            UserProperty<?> property = UserPropertyRegistry.getByName(key);
            if (property == null) return;

            Object value = DataHandler.GSON.fromJson(element, property.getType());
            properties.put(property.getName(), value);
        });

        return properties;
    }).defaultValue("{}").build();
}
