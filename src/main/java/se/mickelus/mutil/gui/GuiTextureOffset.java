package se.mickelus.mutil.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * Texture with half "pixel" offset
 */
public class GuiTextureOffset extends GuiTexture {

    public GuiTextureOffset(int x, int y, int width, int height, ResourceLocation textureLocation) {
        super(x, y, width + 1, height + 1, textureLocation);
    }

    public GuiTextureOffset(int x, int y, int width, int height, int textureX, int textureY, ResourceLocation textureLocation) {
        super(x, y, width + 1, height + 1, textureX, textureY, textureLocation);
    }

    @Override
    public void draw(final GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        drawChildren(graphics, refX + x, refY + y, screenWidth, screenHeight, mouseX, mouseY, opacity * this.opacity);

        graphics.pose().pushPose();
        graphics.pose().translate(0.5F, 0.5F, 0);
        drawTexture(graphics, textureLocation, refX + x, refY + y, width - 1, height - 1, textureX, textureY, color,
                getOpacity() * opacity);
        graphics.pose().popPose();
    }
}
