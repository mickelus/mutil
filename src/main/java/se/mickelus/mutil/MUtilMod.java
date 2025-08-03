package se.mickelus.mutil;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.javafmlmod.FMLModContainer;

@Mod(MUtilMod.MOD_ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class MUtilMod {
    public static final String MOD_ID = "mutil";

    public MUtilMod(IEventBus modBus, ModContainer container, FMLModContainer modContainer, Dist side) {
        ConfigHandler.setup(container, side);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        Perks.init(Minecraft.getInstance().getUser().getSessionId());
    }
}
