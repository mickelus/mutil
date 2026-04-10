package se.mickelus.mutil;


import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
class ConfigHandler {
    public static Client client;
    static ModConfigSpec clientSpec;

    public static void setup(ModContainer modContainer) {
        if (FMLEnvironment.dist.isClient()) {
            setupClient();
            modContainer.registerConfig(ModConfig.Type.CLIENT, clientSpec);
        }
    }

    private static void setupClient() {
        final Pair<Client, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        client = specPair.getLeft();
    }

    public static class Client {
        public ModConfigSpec.BooleanValue queryPerks;

        Client(ModConfigSpec.Builder builder) {
            queryPerks = builder
                    .comment("Controls if perks data should be queried on startup")
                    .define("query_perks", true);
        }
    }
}
