package se.mickelus.mutil.scheduling;

import javax.annotation.ParametersAreNonnullByDefault;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@ParametersAreNonnullByDefault
public class ServerScheduler extends AbstractScheduler {
    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        this.tick(event);
    }
}
