package se.mickelus.mgui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.mgui.gui.GuiRoot;
import se.mickelus.mgui.gui.GuiString;

public class TestHudGui extends GuiRoot {

    public TestHudGui() {
        super(Minecraft.getInstance());

        addChild(new GuiString(0, 0, "hej hopp!"));
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {

        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR) {
            return;
        }

        draw();
    }
}
