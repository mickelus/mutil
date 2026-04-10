package se.mickelus.mutil;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(MUtilMod.MOD_ID)
public class MUtilMod {
    public static final String MOD_ID = "mutil";

    public MUtilMod(ModContainer modContainer) {
        ConfigHandler.setup(modContainer);
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    private static class ClientEvents {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            Perks.init(Minecraft.getInstance().getUser().getProfileId().toString());
        }
    }
}
