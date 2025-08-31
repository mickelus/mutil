package se.mickelus.mutil.data;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public interface DataDistributor {
    public void sendToAll(String directory, Map<ResourceLocation, JsonElement> dataMap);
    public void sendToPlayer(ServerPlayer player, String directory, Map<ResourceLocation, JsonElement> dataMap);
}
