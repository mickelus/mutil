package se.mickelus.mutil.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class GuiTexture extends GuiElement {

    protected ResourceLocation textureLocation;

    protected int textureX;
    protected int textureY;

    protected int color = 0xffffff;

    protected Supplier<ShaderInstance> shader = GameRenderer::getPositionTexShader;
    private boolean useDefaultBlending = true;

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

    public GuiTexture setShader(Supplier<ShaderInstance> shader) {
        this.shader = shader;
        return this;
    }

    public void setUseDefaultBlending(boolean useDefault) {
        this.useDefaultBlending = useDefault;
    }

    @Override
    public void draw(PoseStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(matrixStack, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);

        drawTexture(matrixStack, textureLocation, refX + x, refY + y, width, height, textureX, textureY,
                color, getOpacity() * opacity);
    }

    protected void drawTexture(PoseStack matrixStack, ResourceLocation textureLocation, int x, int y, int width, int height,
            int u, int v, int color, float opacity) {
        RenderSystem.setShader(shader);
        RenderSystem.setShaderColor(
                (color >> 16 & 255) / 255f, // red
                (color >> 8 & 255) / 255f,  // green
                (color & 255) / 255f,       // blue
                opacity);
        RenderSystem.setShaderTexture(0, textureLocation);
        RenderSystem.enableBlend();

        if (useDefaultBlending) {
            RenderSystem.defaultBlendFunc();
        }
        this.blit(matrixStack, x, y, u, v, width, height);
    }
}
