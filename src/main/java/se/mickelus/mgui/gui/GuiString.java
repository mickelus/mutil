package se.mickelus.mgui.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.Tesselator;
import se.mickelus.mgui.gui.animation.KeyframeAnimation;

public class GuiString extends GuiElement {

    protected String string;

    protected Font fontRenderer;

    protected int color = 0xffffffff;
    protected boolean drawShadow = true;

    protected boolean fixedWidth = false;

    public GuiString(int x, int y, String string) {
        super(x, y, 0, 9);

        fontRenderer = Minecraft.getInstance().font;

        this.string = string;
        width = fontRenderer.width(string);
    }

    public GuiString(int x, int y, int width, String string) {
        super(x, y, width, 9);

        fixedWidth = true;

        fontRenderer = Minecraft.getInstance().font;

        this.string = fontRenderer.plainSubstrByWidth(string, width);
    }

    public GuiString(int x, int y, String string, GuiAttachment attachment) {
        this(x, y, string);

        attachmentPoint = attachment;
    }

    public GuiString(int x, int y, String string, int color) {
        this(x, y, string);

        this.color = color;
    }

    public GuiString(int x, int y, String string, int color, GuiAttachment attachment) {
        this(x, y, string, attachment);

        this.color = color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setString(String string) {
        if (string != null && !string.equals(this.string)) {
            if (fixedWidth) {
                this.string = fontRenderer.plainSubstrByWidth(string, width);
            } else {
                this.string = string;
                width = fontRenderer.width(string);
            }
        }
    }

    public GuiString setShadow(boolean shadow) {
        drawShadow = shadow;
        return this;
    }

    @Override
    public void draw(PoseStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        activeAnimations.removeIf(keyframeAnimation -> !keyframeAnimation.isActive());
        activeAnimations.forEach(KeyframeAnimation::preDraw);
        RenderSystem.enableBlend();
        drawString(matrixStack, string, refX + x, refY + y, color, opacity * getOpacity(), drawShadow);
    }

    protected void drawString(PoseStack matrixStack, String text, int x, int y, int color, float opacity, boolean drawShadow) {
        color = colorWithOpacity(color, opacity);

        // if the vanilla fontrender considers the color to be almost transparent (0xfc) it flips the opacity back to 1
        if ((color & -67108864) != 0) {
            matrixStack.pushPose();

            // todo: why was this needed? removed as it breaks in world rendering
//            matrixStack.translate(0.0D, 0.0D, 300.0D);
            MultiBufferSource.BufferSource renderTypeBuffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

            // packed light value is magic copied from FontRender.renderString
            fontRenderer.drawInBatch(text, (float)x, (float)y, color, drawShadow, matrixStack.last().pose(), renderTypeBuffer, true, 0, 15728880);

            renderTypeBuffer.endBatch();
            matrixStack.popPose();
        }
    }
}
