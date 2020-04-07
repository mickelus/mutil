package se.mickelus.mgui.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

public class GuiTextSmall extends GuiText {

    public GuiTextSmall(int x, int y, int width, String string) {
        super(x, y, width , string);
    }

    public void setString(String string) {
        this.string = string.replace("\\n", "\n");

        height = fontRenderer.getWordWrappedHeight(this.string, width * 2) / 2;
    }

    @Override
    public void draw(MatrixStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        matrixStack.push();
        matrixStack.scale(.5f, .5f, .5f);
        renderText(fontRenderer, matrixStack, string, (refX + x) * 2, (refY + y) * 2, width * 2, 0xffffff, opacity);
        matrixStack.pop();
    }
}
