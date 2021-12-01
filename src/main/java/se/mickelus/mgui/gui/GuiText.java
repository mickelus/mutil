package se.mickelus.mgui.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.util.FormattedCharSequence;
import com.mojang.math.Matrix4f;
import net.minecraft.network.chat.TextComponent;

import java.util.List;

public class GuiText extends GuiElement {

    Font fontRenderer;

    String string;
    int color = 0xffffff;

    public GuiText(int x, int y, int width, String string) {
        super(x, y, width ,0);

        fontRenderer = Minecraft.getInstance().font;
        setString(string);
    }

    public void setString(String string) {
        this.string = string.replace("\\n", "\n");

        height = fontRenderer.wordWrapHeight(this.string, width);
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void draw(PoseStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        renderText(fontRenderer, matrixStack, string, refX + x, refY + y, width, color, opacity);

        super.draw(matrixStack, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
    }

    protected static void renderText(Font fontRenderer, PoseStack matrixStack, String string, int x, int y, int width, int color,
            float opacity) {
        List<FormattedCharSequence> list = fontRenderer.split(new TextComponent(string), width);
        Matrix4f matrix = matrixStack.last().pose();

        for(FormattedCharSequence line : list) {
            float lineX = (float) x;
            MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

//            if (fontRenderer.getBidiFlag()) {
//                int i = fontRenderer.getStringWidth(fontRenderer.bidiReorder(line.));
//                lineX += (float)(width - i);
//            }

            fontRenderer.drawInBatch(line, lineX, (float)y, colorWithOpacity(color, opacity), true, matrix, buffer, false, 0, 15728880);

            buffer.endBatch();

            y += 9;
        }
    }
}
