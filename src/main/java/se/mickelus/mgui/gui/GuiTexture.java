package se.mickelus.mgui.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.ResourceLocation;

public class GuiTexture extends GuiElement {

    protected ResourceLocation textureLocation;

    protected int textureX;
    protected int textureY;

    protected int color = 0xffffff;

    public GuiTexture(int x, int y, int width, int height, ResourceLocation textureLocation) {
        this(x, y, width, height, 0, 0, textureLocation);
    }

    public GuiTexture(int x, int y, int width, int height, int textureX, int textureY, ResourceLocation textureLocation) {
        super(x, y, width, height);

        this.textureX = textureX;
        this.textureY = textureY;

        this.textureLocation = textureLocation;
    }

    public GuiTexture setTextureCoordinates(int x, int y) {
        textureX = x;
        textureY = y;
        return this;
    }

    public GuiTexture setColor(int color) {
        this.color = color;
        return this;
    }

    @Override
    public void draw(MatrixStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(matrixStack, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);

        drawTexture(matrixStack, textureLocation, refX + x, refY + y, width, height, textureX, textureY,
                color, getOpacity() * opacity);
    }
}
