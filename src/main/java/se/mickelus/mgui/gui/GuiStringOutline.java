package se.mickelus.mgui.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.TextFormatting;

public class GuiStringOutline extends GuiString {
    private String cleanString;

    public GuiStringOutline(int x, int y, String string) {
        super(x, y, string);
        drawShadow = false;

        cleanString = TextFormatting.stripFormatting(this.string);
    }

    public GuiStringOutline(int x, int y, int width, String string) {
        super(x, y, width, string);
        drawShadow = false;

        cleanString = TextFormatting.stripFormatting(this.string);
    }

    public GuiStringOutline(int x, int y, String string, GuiAttachment attachment) {
        super(x, y, string, attachment);
        drawShadow = false;

        cleanString = TextFormatting.stripFormatting(this.string);
    }

    public GuiStringOutline(int x, int y, String string, int color) {
        super(x, y, string, color);
        drawShadow = false;

        cleanString = TextFormatting.stripFormatting(this.string);
    }

    public GuiStringOutline(int x, int y, String string, int color, GuiAttachment attachment) {
        super(x, y, string, color, attachment);
        drawShadow = false;

        cleanString = TextFormatting.stripFormatting(this.string);
    }

    @Override
    public void setString(String string) {
        super.setString(string);

        cleanString = TextFormatting.stripFormatting(this.string);
    }

    @Override
    protected void drawString(MatrixStack matrixStack, String text, int x, int y, int color, float opacity, boolean drawShadow) {

        super.drawString(matrixStack, cleanString, x - 1, y - 1, 0, opacity, false);
        super.drawString(matrixStack, cleanString, x, y - 1, 0, opacity, false);
        super.drawString(matrixStack, cleanString, x + 1, y - 1, 0, opacity, false);

        super.drawString(matrixStack, cleanString, x - 1, y + 1, 0, opacity, false);
        super.drawString(matrixStack, cleanString, x, y + 1, 0, opacity, false);
        super.drawString(matrixStack, cleanString, x + 1, y + 1, 0, opacity, false);

        super.drawString(matrixStack, cleanString, x + 1, y, 0, opacity, false);
        super.drawString(matrixStack, cleanString, x - 1, y, 0, opacity, false);

        super.drawString(matrixStack, text, x, y, color, opacity, false);
    }
}
