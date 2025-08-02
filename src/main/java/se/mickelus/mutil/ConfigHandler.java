package se.mickelus.mutil;


import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.ParametersAreNonnullByDefault;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.ModConfigSpec;

@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
class ConfigHandler {
    public static Client client;
    static ModConfigSpec clientSpec;

    public static void setup() {
        if (FMLEnvironment.dist.isClient()) {
            setupClient();
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, clientSpec);
            FMLJavaModLoadingContext.get().getModEventBus().register(ConfigHandler.client);
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
