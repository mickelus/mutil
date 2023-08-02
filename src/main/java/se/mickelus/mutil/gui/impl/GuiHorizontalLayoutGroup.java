package se.mickelus.mutil.gui.impl;

import net.minecraft.client.gui.GuiGraphics;
import se.mickelus.mutil.gui.GuiElement;

public class GuiHorizontalLayoutGroup extends GuiElement {
    private boolean needsLayout = false;

    private int spacing = 0;

    public GuiHorizontalLayoutGroup(int x, int y, int height, int spacing) {
        super(x, y, 0, height);

        this.spacing = spacing;
    }

    @Override
    public void addChild(GuiElement child) {
        super.addChild(child);
        triggerLayout();
    }

    public void triggerLayout() {
        needsLayout = true;
    }

    public void forceLayout() {
        layoutChildren();
    }

    private void layoutChildren() {
        int offset = 0;

        for (GuiElement child : getChildren()) {
            child.setX(offset);
            offset += child.getWidth() + spacing;
        }

        setWidth(offset - spacing);

        needsLayout = false;
    }

    @Override
    protected void drawChildren(final GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        if (needsLayout) {
            layoutChildren();
        }
        super.drawChildren(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
    }
}
