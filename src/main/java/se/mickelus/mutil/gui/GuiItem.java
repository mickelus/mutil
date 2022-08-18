package se.mickelus.mutil.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.ArrayList;
import java.util.List;

public class GuiItem extends GuiElement {
    private Minecraft mc;

    private ItemStack itemStack;

    private boolean showTooltip = true;
    private CountMode countMode = CountMode.normal;

    private float opacityThreshold = 1;
    private boolean resetDepthTest = true;

    private boolean renderDecoration = true;

    public GuiItem(int x, int y) {
        super(x, y, 16, 16);

        mc = Minecraft.getInstance();

        setVisible(false);
    }

    /**
     * Sets the opacity threshold for this element, the item will only render when the combined opacity of this element and it's parent is above the
     * threshold.
     *
     * @param opacityThreshold
     * @return
     */
    public GuiItem setOpacityThreshold(float opacityThreshold) {
        this.opacityThreshold = opacityThreshold;
        return this;
    }

    public GuiItem setTooltip(boolean showTooltip) {
        this.showTooltip = showTooltip;
        return this;
    }

    public GuiItem setCountVisibility(CountMode mode) {
        this.countMode = mode;
        return this;
    }

    public GuiItem setItem(ItemStack itemStack) {
        this.itemStack = itemStack;
        setVisible(itemStack != null);

        return this;
    }

    public GuiItem setResetDepthTest(boolean shouldReset) {
        this.resetDepthTest = shouldReset;
        return this;
    }

    public GuiItem setRenderDecoration(boolean shouldRender) {
        this.renderDecoration = shouldRender;
        return this;
    }

    // todo 1.16: opacity no longer works, did it ever work?
    @Override
    public void draw(PoseStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(matrixStack, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);

        if (opacity * getOpacity() >= opacityThreshold) {
            RenderSystem.applyModelViewMatrix();
            setBlitOffset(0);
            mc.getItemRenderer().blitOffset = 0;
            RenderSystem.enableDepthTest();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            mc.getItemRenderer().renderAndDecorateItem(itemStack, refX + x, refY + y);

            if (renderDecoration) {
                Font font = IClientItemExtensions.of(itemStack).getFont(itemStack, IClientItemExtensions.FontContext.ITEM_COUNT);
                mc.getItemRenderer().renderGuiItemDecorations(font != null ? font : mc.font, itemStack, refX + x, refY + y, getCountString());
            }

            if (resetDepthTest) {
                RenderSystem.disableDepthTest();
            }
        }
    }

    protected String getCountString() {
        switch (countMode) {
            case normal:
                return null;
            case always:
                return String.valueOf(itemStack.getCount());
            case never:
                return "";
        }

        return null;
    }

    @Override
    public List<Component> getTooltipLines() {
        if (showTooltip && itemStack != null && hasFocus()) {
            return new ArrayList<>(itemStack.getTooltipLines(Minecraft.getInstance().player,
                    mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL));
        }

        return null;
    }

    public enum CountMode {
        normal, // shows if count is > 1

        always,
        never
    }
}
