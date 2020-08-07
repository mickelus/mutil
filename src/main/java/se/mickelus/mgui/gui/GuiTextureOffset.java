package se.mickelus.mgui.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.ResourceLocation;

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
    public void draw(MatrixStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        calculateFocusState(refX, refY, mouseX, mouseY);
        drawChildren(matrixStack, refX + x, refY + y, screenWidth, screenHeight, mouseX, mouseY, opacity * this.opacity);

        matrixStack.push();
        matrixStack.translate(0.5F, 0.5F, 0);
        drawTexture(matrixStack, textureLocation, refX + x, refY + y, width - 1, height - 1, textureX, textureY,
                color, getOpacity() * opacity);
        matrixStack.pop();
    }
}
