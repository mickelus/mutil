package se.mickelus.mgui.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.stream.Collectors;

public class GuiItem extends GuiElement {

    private Minecraft mc;
    private Font fontRenderer;

    private ItemStack itemStack;

    private boolean showTooltip = true;
    private CountMode countMode = CountMode.normal;

    private float opacityThreshold = 1;

    public GuiItem(int x, int y) {
        super(x, y, 16, 16);

        mc = Minecraft.getInstance();

        setVisible(false);
    }

    /**
     * Sets the opacity threshold for this element, the item will only render when the combined opacity of this element and it's parent is above the
     * threshold.
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

        if (itemStack != null) {
            fontRenderer = null; // itemStack.getItem().getFontRenderer(itemStack); // FIXME: does 1.18 have an equivalent?

            if (fontRenderer == null) {
                fontRenderer = mc.font;
            }
        }

        return this;
    }
    // todo 1.16: opacity no longer works, did it ever work?
    @Override
    public void draw(PoseStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(matrixStack, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);

        if (opacity * getOpacity() >= opacityThreshold) {
            PoseStack renderSystemStack = RenderSystem.getModelViewStack();
            renderSystemStack.pushPose();
            RenderSystem.enableDepthTest();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            // Lighting.turnBackOn(); // FIXME
            mc.getItemRenderer().renderAndDecorateItem(itemStack, refX + x, refY + y);
            // Lighting.turnOff(); // FIXME
            mc.getItemRenderer().renderGuiItemDecorations(fontRenderer, itemStack, refX + x, refY + y, getCountString());

            RenderSystem.disableDepthTest();
            renderSystemStack.popPose();
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
    public List<String> getTooltipLines() {
        if (showTooltip && itemStack != null && hasFocus()) {
            return itemStack.getTooltipLines(Minecraft.getInstance().player,
                    this.mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL)
                    .stream()
                    .map(Component::getString)
                    .collect(Collectors.toList());
        }

        return null;
    }

    public enum CountMode {
        /** shows if count is > 1 **/
        normal,

        always,
        never
    }
}
