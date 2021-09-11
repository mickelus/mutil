package se.mickelus.mgui.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;

public class GuiText extends GuiElement {

    FontRenderer fontRenderer;

    String string;
    int color = 0xffffff;

    public GuiText(int x, int y, int width, String string) {
        super(x, y, width ,0);

        fontRenderer = Minecraft.getInstance().fontRenderer;
        setString(string);
    }

    public void setString(String string) {
        this.string = string.replace("\\n", "\n");

        height = fontRenderer.getWordWrappedHeight(this.string, width);
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void draw(MatrixStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        renderText(fontRenderer, matrixStack, string, refX + x, refY + y, width, color, opacity);

        super.draw(matrixStack, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
    }

    protected static void renderText(FontRenderer fontRenderer, MatrixStack matrixStack, String string, int x, int y, int width, int color,
            float opacity) {
        List<IReorderingProcessor> list = fontRenderer.trimStringToWidth(new StringTextComponent(string), width);
        Matrix4f matrix = matrixStack.getLast().getMatrix();

        for(IReorderingProcessor line : list) {
            float lineX = (float) x;
            IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());

//            if (fontRenderer.getBidiFlag()) {
//                int i = fontRenderer.getStringWidth(fontRenderer.bidiReorder(line.));
//                lineX += (float)(width - i);
//            }

            fontRenderer.func_238416_a_(line, lineX, (float)y, colorWithOpacity(color, opacity), true, matrix, buffer, false, 0, 15728880);

            buffer.finish();

            y += 9;
        }
    }
}
