package se.mickelus.mutil.scheduling;

import javax.annotation.ParametersAreNonnullByDefault;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@ParametersAreNonnullByDefault
public class ClientScheduler extends AbstractScheduler {
    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        this.tick(event);
    }

}
