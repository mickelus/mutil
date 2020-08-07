package se.mickelus.mgui.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.List;
import java.util.stream.Collectors;

public class GuiItem extends GuiElement {

    private Minecraft mc;
    private FontRenderer fontRenderer;

    private ItemStack itemStack;

    private boolean showTooltip = true;
    private boolean enforceCount = false;

    public GuiItem(int x, int y) {
        super(x, y, 16, 16);

        mc = Minecraft.getInstance();

        setVisible(false);
    }

    public GuiItem setTooltip(boolean showTooltip) {
        this.showTooltip = showTooltip;
        return this;
    }

    /**
     * Enforces this to always render the item count, even if the count is 1
     * @param enforceCount
     * @return
     */
    public GuiItem enforceCount(boolean enforceCount) {
        this.enforceCount = enforceCount;
        return this;
    }

    public GuiItem setItem(ItemStack itemStack) {
        this.itemStack = itemStack;
        setVisible(itemStack != null);

        if (itemStack != null) {
            fontRenderer = itemStack.getItem().getFontRenderer(itemStack);

            if (fontRenderer == null) {
                fontRenderer = mc.fontRenderer;
            }
        }

        return this;
    }
    // todo 1.16: opacity no longer works, did it ever work?
    @Override
    public void draw(MatrixStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(matrixStack, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);

        RenderSystem.pushMatrix();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.enableStandardItemLighting();
        mc.getItemRenderer().renderItemAndEffectIntoGUI(itemStack, refX + x, refY + y);
        RenderHelper.disableStandardItemLighting();
        mc.getItemRenderer().renderItemOverlayIntoGUI(fontRenderer, itemStack, refX + x, refY + y,
                enforceCount ? itemStack.getCount() + "" : null);

        RenderSystem.disableDepthTest();
        RenderSystem.popMatrix();
    }

    @Override
    public List<String> getTooltipLines() {
        if (showTooltip && itemStack != null && hasFocus()) {
            return itemStack.getTooltip(Minecraft.getInstance().player,
                    this.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL)
                    .stream()
                    .map(ITextComponent::getString)
                    .collect(Collectors.toList());
        }

        return null;
    }
}
