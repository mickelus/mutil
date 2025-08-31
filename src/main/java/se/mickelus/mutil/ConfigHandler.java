package se.mickelus.mutil;


import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.tuple.Pair;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

@ParametersAreNonnullByDefault
//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
class ConfigHandler {
    public static Client client;
    static ModConfigSpec clientSpec;

    public static void setup(ModContainer container, Dist side) {
        if (side.isClient()) {
            setupClient();
            container.registerConfig(ModConfig.Type.CLIENT, clientSpec);
            //TODO: this might cause issues with config values
            //FMLJavaModLoadingContext.get().getModEventBus().register(ConfigHandler.client);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void setupClient() {
        final Pair<Client, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        client = specPair.getLeft();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Client {
        public ModConfigSpec.BooleanValue queryPerks;

        Client(ModConfigSpec.Builder builder) {
            queryPerks = builder
                    .comment("Controls if perks data should be queried on startup")
                    .define("query_perks", true);
        }
    }
}
