package me.royvortex.starlight.data.serialize;

import com.google.gson.*;
import me.royvortex.starlight.command.CommandKey;

import java.lang.reflect.Type;

public class CommandKeySerializer implements JsonSerializer<CommandKey>, JsonDeserializer<CommandKey> {

    @Override
    public CommandKey deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return CommandKey.fromKeyString(json.getAsString());
    }

    @Override
    public JsonElement serialize(CommandKey key, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(key.toKeyString());
    }
}
