package se.mickelus.mutil.gui;

import net.minecraft.client.gui.GuiGraphics;
import se.mickelus.mutil.gui.impl.GuiHorizontalScrollable;

public class ScrollBarGui extends GuiElement {
    private final GuiHorizontalScrollable scrollable;

    private final boolean unscrollableHidden;

    public ScrollBarGui(int x, int y, int width, int height, GuiHorizontalScrollable scrollable) {
        this(x, y, width, height, scrollable, false);
    }

    public ScrollBarGui(int x, int y, int width, int height, GuiHorizontalScrollable scrollable, boolean unscrollableHidden) {
        super(x, y, width, height);

        this.scrollable = scrollable;
        this.unscrollableHidden = unscrollableHidden;
    }

    private boolean isActive() {
        return !unscrollableHidden || scrollable.getOffsetMax() > 0;
    }

    @Override
    public void draw(final GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY,
            float opacity) {
        if (isActive()) {
            super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);

            drawBackground(graphics, refX + x, refY + y);
            int contentWidth = scrollable.getOffsetMax() + scrollable.getWidth();

            int handleWidth = Math.max(3, (int) (scrollable.getWidth() * 1f / contentWidth * width) + 1);
            int handleOffset = (int) (scrollable.getOffset() / contentWidth * width);

            drawHandle(graphics, refX + x + handleOffset, refY + y, handleWidth);
        }
    }

    protected void drawBackground(final GuiGraphics graphics, int x, int y) {
        drawRect(graphics, x, y, x + width, y + height, 0xffffff, opacity * 0.2f);
    }

    protected void drawHandle(final GuiGraphics graphics, int x, int y, int handleWidth) {
        drawRect(graphics, x, y, x + handleWidth, y + height, 0xffffff, opacity * 0.7f);
    }
}
