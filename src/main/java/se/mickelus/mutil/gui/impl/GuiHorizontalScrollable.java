package se.mickelus.mutil.gui.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import se.mickelus.mutil.gui.GuiElement;

public class GuiHorizontalScrollable extends GuiElement {
    private boolean dirty = false;
    private double scrollOffset = 0;
    private double scrollVelocity = 0;

    private boolean isGlobal = false;

    private int min;
    private int max;

    private long lastDraw = System.currentTimeMillis();

    public GuiHorizontalScrollable(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Set this scrollable to react to scrolling anywhere in the UI, not just while the scrollable has focus.
     * @param isGlobal
     * @return
     */
    public GuiHorizontalScrollable setGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal;
        return this;
    }

    public double getOffset() {
        return scrollOffset;
    }

    public void setOffset(double offset) {
        scrollOffset = Mth.clamp(offset, min, max);
    }

    public int getOffsetMax() {
        return max;
    }

    /**
     * Call when child layout/sizes change to cause bounds to update on next scroll.
     */
    public void markDirty() {
        dirty = true;
    }


    private void calculateBounds() {
        int tempMax = 0;
        this.min = 0;
        for (GuiElement element: getChildren()) {
            int x = getXOffset(this, element.getAttachmentAnchor()) - getXOffset(element, element.getAttachmentPoint());
            this.min = Math.min(x, this.min);
            tempMax = Math.max(x + element.getWidth(), tempMax);
        }
        this.max = Math.max(tempMax - width, 0);
        scrollOffset = Mth.clamp(scrollOffset, min, max);

        dirty = false;
    }

    @Override
    public boolean onMouseScroll(double mouseX, double mouseY, double distance) {
        if (isGlobal || hasFocus()) {

            if (Math.signum(scrollVelocity) != Math.signum(-distance)) {
                scrollVelocity = 0;
            }

            scrollVelocity -= distance * 12;
            scrollOffset = Mth.clamp(scrollOffset - distance * 6, min, max);
//            Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, (float) (0.5f + (scrollOffset - min) / max), 0.3f));

            return true;
        }
        return false;
    }

    @Override
    protected void drawChildren(PoseStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        long now = System.currentTimeMillis();
        if (scrollVelocity != 0) {
            double dist = (scrollVelocity * 0.2 + Math.signum(scrollVelocity) * 1) * (now - lastDraw) / 1000 * 50;
            if (Math.signum(scrollVelocity) != Math.signum(scrollVelocity - dist)) {
                dist = scrollVelocity;
                scrollVelocity = 0;
            } else {
                scrollVelocity -= dist;
            }

            scrollOffset = Mth.clamp(scrollOffset + dist, min, max);
        }

        lastDraw = now;
        super.drawChildren(matrixStack, refX - (int) scrollOffset, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);

        if (dirty) {
            calculateBounds();
        }
    }

    @Override
    public void addChild(GuiElement child) {
        super.addChild(child);
        markDirty();
    }

    @Override
    public void clearChildren() {
        super.clearChildren();
        markDirty();
    }
}
