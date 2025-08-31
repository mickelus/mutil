package se.mickelus.mutil.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;

public class GuiStringOutline extends GuiString {
    private String cleanString;

    public GuiStringOutline(int x, int y, String string) {
        super(x, y, string);
        drawShadow = false;

        cleanString = ChatFormatting.stripFormatting(this.string);
    }

    public GuiStringOutline(int x, int y, int width, String string) {
        super(x, y, width, string);
        drawShadow = false;

        cleanString = ChatFormatting.stripFormatting(this.string);
    }

    public GuiStringOutline(int x, int y, String string, GuiAttachment attachment) {
        super(x, y, string, attachment);
        drawShadow = false;

        cleanString = ChatFormatting.stripFormatting(this.string);
    }

    public GuiStringOutline(int x, int y, String string, int color) {
        super(x, y, string, color);
        drawShadow = false;

        cleanString = ChatFormatting.stripFormatting(this.string);
    }

    public GuiStringOutline(int x, int y, String string, int color, GuiAttachment attachment) {
        super(x, y, string, color, attachment);
        drawShadow = false;

        cleanString = ChatFormatting.stripFormatting(this.string);
    }

    @Override
    public void setString(String string) {
        super.setString(string);

        cleanString = ChatFormatting.stripFormatting(this.string);
    }

    @Override
    protected void drawString(final GuiGraphics graphics, String text, int x, int y, int color, float opacity, boolean drawShadow) {

        graphics.pose().pushPose();
        super.drawString(graphics, cleanString, x - 1, y - 1, 0, opacity, false);
        super.drawString(graphics, cleanString, x, y - 1, 0, opacity, false);
        super.drawString(graphics, cleanString, x + 1, y - 1, 0, opacity, false);

        super.drawString(graphics, cleanString, x - 1, y + 1, 0, opacity, false);
        super.drawString(graphics, cleanString, x, y + 1, 0, opacity, false);
        super.drawString(graphics, cleanString, x + 1, y + 1, 0, opacity, false);

        super.drawString(graphics, cleanString, x + 1, y, 0, opacity, false);
        super.drawString(graphics, cleanString, x - 1, y, 0, opacity, false);

        // magic offset to avoid z-fighting for in-world rendering
        graphics.pose().translate(0, 0, 0.0020000000949949026D);
        super.drawString(graphics, text, x, y, color, opacity, false);
        graphics.pose().popPose();
    }
}
