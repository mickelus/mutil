package se.mickelus.mutil.data.deserializer;

import java.lang.reflect.Type;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@ParametersAreNonnullByDefault
public class ItemDeserializer implements JsonDeserializer<Item> {
    @Override
    public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String string = json.getAsString();
        if (string != null) {
            ResourceLocation resourceLocation = ResourceLocation.parse(string);
            if (BuiltInRegistries.ITEM.containsKey(resourceLocation)) {
                return BuiltInRegistries.ITEM.get(resourceLocation);
            }
        }

        return null;
    }
}