package se.mickelus.mutil.gui;

import com.mojang.blaze3d.vertex.PoseStack;

public class GuiTextSmall extends GuiText {

    public GuiTextSmall(int x, int y, int width, String string) {
        super(x, y, width , string);
    }

    public void setString(String string) {
        this.string = string.replace("\\n", "\n");

        height = fontRenderer.wordWrapHeight(this.string, width * 2) / 2;
    }

    @Override
    public void draw(PoseStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        matrixStack.pushPose();
        matrixStack.scale(.5f, .5f, .5f);
        renderText(fontRenderer, matrixStack, string, (refX + x) * 2, (refY + y) * 2, width * 2, 0xffffff, opacity);
        matrixStack.popPose();

        calculateFocusState(refX, refY, mouseX, mouseY);
        drawChildren(matrixStack, refX + x, refY + y, screenWidth, screenHeight, mouseX, mouseY, opacity * this.opacity);
    }
}
