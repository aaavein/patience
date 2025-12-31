package net.aaavein.patience.util;

import com.google.gson.*;

import java.lang.reflect.Type;

public class SlotRangeSerializer implements JsonSerializer<SlotRange>, JsonDeserializer<SlotRange> {

    @Override
    public JsonElement serialize(SlotRange src, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public SlotRange deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        return SlotRange.parse(json.getAsString());
    }
}