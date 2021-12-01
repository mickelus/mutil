package se.mickelus.mgui.gui;

import com.mojang.blaze3d.vertex.PoseStack;

public class GuiRect extends GuiElement {

    private int color;
    private boolean offset;

    public GuiRect(int x, int y, int width, int height, int color) {
        this(x, y, width, height, color, false);
    }

    public GuiRect(int x, int y, int width, int height, int color, boolean offset) {
        super(x, y, offset ? width + 1 : width, offset ? height + 1 : height);

        this.color = color;
        this.offset = offset;
    }

    public GuiRect setColor(int color) {
        this.color = color;
        return this;
    }

    @Override
    public void draw(PoseStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(matrixStack, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        if (offset) {
            matrixStack.pushPose();
            matrixStack.translate(0.5F, 0.5F, 0);
            drawRect(matrixStack, refX + x, refY + y, refX + x + width - 1, refY + y + height - 1, color, opacity * getOpacity());
            matrixStack.popPose();
        } else {
            drawRect(matrixStack, refX + x, refY + y, refX + x + width, refY + y + height, color, opacity * getOpacity());
        }
    }
}
