package se.mickelus.mutil.scheduling;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ClientScheduler extends AbstractScheduler {
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        this.tick(event);
    }

}
