package se.mickelus.mgui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.mgui.gui.GuiAttachment;
import se.mickelus.mgui.gui.GuiRect;
import se.mickelus.mgui.gui.GuiString;
import se.mickelus.mgui.gui.hud.GuiRootHud;
import se.mickelus.mgui.gui.impl.GuiColors;

public class TestOverlayGui extends GuiRootHud {

    public TestOverlayGui() {
        addChild(new GuiRect(0, 0, 1, 1, GuiColors.add));
        addChild(new GuiRect(0, 0, 1, 1, GuiColors.remove).setAttachment(GuiAttachment.topRight));
        addChild(new GuiRect(0, 0, 1, 1, GuiColors.hover).setAttachment(GuiAttachment.bottomLeft));
        addChild(new GuiRect(0, 0, 1, 1, GuiColors.hoverMuted).setAttachment(GuiAttachment.bottomRight));
        addChild(new GuiString(0, 0, "test").setAttachment(GuiAttachment.middleCenter));
    }

    @SubscribeEvent
    public void renderOverlay(DrawBlockHighlightEvent event) {
        if (event.getTarget().getType().equals(RayTraceResult.Type.BLOCK)) {
            BlockRayTraceResult rayTrace = (BlockRayTraceResult) event.getTarget();

            World world = Minecraft.getInstance().world;
            VoxelShape shape = world.getBlockState(rayTrace.getPos()).getShape(Minecraft.getInstance().world, rayTrace.getPos());

            if (!shape.isEmpty()) {
                opacity = 1f;
                draw(Minecraft.getInstance().player, rayTrace, shape, event.getPartialTicks());
            }
        }
    }
}
