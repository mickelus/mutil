package se.mickelus.mutil.scheduling;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ClientScheduler extends AbstractScheduler {
    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        this.tick();
    }

}
