package se.mickelus.mutil.data;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class MergingDataStore<V, U> extends DataStore<V> {
    private static final Logger logger = LogManager.getLogger();

    protected Class<U> arrayClass;

    public MergingDataStore(Gson gson, String namespace, String directory, Class<V> entryClass, Class<U> arrayClass, DataDistributor synchronizer) {
        super(gson, namespace, directory, entryClass, synchronizer);

        this.arrayClass = arrayClass;
    }

    @Override
    protected Map<ResourceLocation, JsonElement> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        logger.debug("Reading data for {} data store...", directory);
        Map<ResourceLocation, JsonElement> map = Maps.newHashMap();
        int i = this.directory.length() + 1;

        for (Map.Entry<ResourceLocation, List<Resource>> entry : resourceManager.listResourceStacks(directory, rl -> rl.getPath().endsWith(".json")).entrySet()) {
            if (!namespace.equals(entry.getKey().getNamespace())) {
                continue;
            }

            String path = entry.getKey().getPath();
            ResourceLocation location = new ResourceLocation(entry.getKey().getNamespace(), path.substring(i, path.length() - jsonExtLength));

            JsonArray allResources = new JsonArray();

            for (Resource resource : entry.getValue()) {
                try (Reader reader = resource.openAsReader()) {
                    JsonObject json = GsonHelper.fromJson(gson, reader, JsonObject.class);

                    if (json != null) {
                        if (shouldLoad(json)) {
                            allResources.add(json);
                        } else {
                            logger.debug("Skipping data '{}' from '{}' due to condition", entry.getKey(), resource.sourcePackId());
                        }
                    } else {
                        logger.error("Couldn't load data from '{}' in data pack '{}' as it's empty or null",
                                entry.getKey(), resource.sourcePackId());
                    }
                } catch (RuntimeException | IOException e) {
                    logger.error("Couldn't load data from '{}' in data pack '{}'", entry.getKey(), resource.sourcePackId(), e);
                }
            }

            if (allResources.size() > 0) {
                map.put(location, allResources);
            }
        }

        return map;
    }

    @Override
    public void loadFromPacket(Map<ResourceLocation, String> data) {
        Map<ResourceLocation, JsonElement> splashList = data.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> GsonHelper.fromJson(gson, entry.getValue(), JsonArray.class)
                ));

        parseData(splashList);
    }

    public void parseData(Map<ResourceLocation, JsonElement> splashList) {
        logger.info("Loaded {} {}", String.format("%3d", splashList.values().size()), directory);
        dataMap = splashList.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> mergeData(gson.fromJson(entry.getValue(), arrayClass))
                ));

        processData();

        listeners.forEach(Runnable::run);
    }

    protected abstract V mergeData(U collection);
}
