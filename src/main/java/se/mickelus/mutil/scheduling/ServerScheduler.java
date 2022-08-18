package se.mickelus.mutil.scheduling;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ServerScheduler extends AbstractScheduler {
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        tick(event);
    }
}
