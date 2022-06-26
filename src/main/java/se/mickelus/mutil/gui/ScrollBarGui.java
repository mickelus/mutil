package se.mickelus.mutil.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import se.mickelus.mutil.gui.impl.GuiHorizontalScrollable;

public class ScrollBarGui extends GuiElement {
    private final GuiHorizontalScrollable scrollable;

    private final boolean unscrollableHidden;

    public ScrollBarGui(int x, int y, int width, int height, GuiHorizontalScrollable scrollable) {
        this(x, y, width, height, scrollable, false);
    }

    public ScrollBarGui(int x, int y, int width,  int height, GuiHorizontalScrollable scrollable, boolean unscrollableHidden) {
        super(x, y, width, height);

        this.scrollable = scrollable;
        this.unscrollableHidden = unscrollableHidden;
    }

    private boolean isActive() {
        return !unscrollableHidden || scrollable.getOffsetMax() > 0;
    }

    @Override
    public void draw(PoseStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        if (isActive()) {
            super.draw(matrixStack, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);

            drawBackground(matrixStack, refX + x, refY + y);
            int contentWidth = scrollable.getOffsetMax() + scrollable.getWidth();

            int handleWidth = Math.max(3, (int) (scrollable.getWidth() * 1f / contentWidth * width) + 1);
            int handleOffset = (int) (scrollable.getOffset() / contentWidth * width);

            drawHandle(matrixStack, refX + x + handleOffset, refY + y, handleWidth);
        }
    }

    protected void drawBackground(PoseStack matrixStack, int x, int y) {
        drawRect(matrixStack, x, y, x + width, y + height, 0xffffff, opacity * 0.2f);
    }

    protected void drawHandle(PoseStack matrixStack, int x, int y, int handleWidth) {
        drawRect(matrixStack, x, y, x + handleWidth, y + height, 0xffffff, opacity * 0.7f);
    }
}
