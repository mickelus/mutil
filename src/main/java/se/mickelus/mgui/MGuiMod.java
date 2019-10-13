package se.mickelus.mgui;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MGuiMod.MOD_ID)
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class MGuiMod {
    public static final String MOD_ID = "mgui";

    public MGuiMod() {
        System.out.println("constructing mod");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    public void setup(FMLCommonSetupEvent event) {
        System.out.println("setup event");
    }
}
