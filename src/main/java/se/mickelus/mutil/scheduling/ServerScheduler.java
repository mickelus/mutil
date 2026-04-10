package se.mickelus.mutil.scheduling;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ServerScheduler extends AbstractScheduler {
    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        tick();
    }
}
