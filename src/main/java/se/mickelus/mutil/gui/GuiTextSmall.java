package se.mickelus.mutil.gui;

import net.minecraft.client.gui.GuiGraphics;

public class GuiTextSmall extends GuiText {

    public GuiTextSmall(int x, int y, int width, String string) {
        super(x, y, width , string);
    }

    public void setString(String string) {
        this.string = string.replace("\\n", "\n");

        height = fontRenderer.wordWrapHeight(this.string, width * 2) / 2;
    }

    @Override
    public void draw(final GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        graphics.pose().pushPose();
        graphics.pose().scale(.5f, .5f, .5f);
        renderText(graphics, fontRenderer, string, (refX + x) * 2, (refY + y) * 2, width * 2, 0xffffff, opacity);
        graphics.pose().popPose();

        drawChildren(graphics, refX + x, refY + y, screenWidth, screenHeight, mouseX, mouseY, opacity * this.opacity);
    }
}
