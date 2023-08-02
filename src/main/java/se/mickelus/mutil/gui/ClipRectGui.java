package se.mickelus.mutil.gui;

import net.minecraft.client.gui.GuiGraphics;

public class ClipRectGui extends GuiElement {
    public ClipRectGui(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    protected void drawChildren(final GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY,
            float opacity) {
        graphics.enableScissor(refX, refY, refX + width, refY + height);
        super.drawChildren(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        graphics.disableScissor();
    }

    @Override
    public void updateFocusState(int refX, int refY, int mouseX, int mouseY) {
        boolean gainFocus = mouseX >= getX() + refX
                && mouseX < getX() + refX + getWidth()
                && mouseY >= getY() + refY
                && mouseY < getY() + refY + getHeight();

        if (gainFocus != hasFocus) {
            hasFocus = gainFocus;
            if (hasFocus) {
                onFocus();
            }
            else {
                onBlur();
            }
        }

        if (hasFocus) {
            elements.stream()
                    .filter(GuiElement::isVisible)
                    .forEach(element -> element.updateFocusState(
                            refX + x + getXOffset(this, element.attachmentAnchor) - getXOffset(element, element.attachmentPoint),
                            refY + y + getYOffset(this, element.attachmentAnchor) - getYOffset(element, element.attachmentPoint),
                            mouseX, mouseY));
        }
    }
}
