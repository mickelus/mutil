package se.mickelus.mutil.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;

import java.util.*;
import java.util.stream.Collectors;

// todo 1.14: used to extend Gui, should extend GuiAbstract?
public class GuiElement extends GuiComponent {

    protected int x;
    protected int y;
    protected GuiAttachment attachmentPoint = GuiAttachment.topLeft;
    protected GuiAttachment attachmentAnchor = GuiAttachment.topLeft;

    protected int width;
    protected int height;

    protected float opacity = 1;

    protected boolean hasFocus = false;

    protected boolean isVisible = true;

    protected boolean shouldRemove = false;

    protected ArrayList<GuiElement> elements;

    protected Set<KeyframeAnimation> activeAnimations;

    public GuiElement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        elements = new ArrayList<>();

        activeAnimations = new HashSet<>();
    }

    public void draw(PoseStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        drawChildren(matrixStack, refX + x, refY + y, screenWidth, screenHeight, mouseX, mouseY, opacity * this.opacity);
    }

    public void updateAnimations() {
//        activeAnimations.stream()
//                .filter(animation -> !animation.isActive())
//                .forEach(KeyframeAnimation::stop);
        activeAnimations.removeIf(animation -> !animation.isActive());
        activeAnimations.forEach(KeyframeAnimation::preDraw);
    }

    protected void drawChildren(PoseStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        elements.removeIf(GuiElement::shouldRemove);
        elements.stream()
                .filter(GuiElement::isVisible)
                .forEach((element -> {
                    element.updateAnimations();
                    element.draw(
                            matrixStack, refX + getXOffset(this, element.attachmentAnchor) - getXOffset(element, element.attachmentPoint),
                            refY + getYOffset(this, element.attachmentAnchor) - getYOffset(element, element.attachmentPoint),
                            screenWidth, screenHeight, mouseX, mouseY, opacity);
                }));
    }

    protected static int getXOffset(GuiElement element, GuiAttachment attachment) {
        switch (attachment) {
            case topLeft:
            case middleLeft:
            case bottomLeft:
                return 0;
            case topCenter:
            case middleCenter:
            case bottomCenter:
                return element.getWidth() / 2;
            case topRight:
            case middleRight:
            case bottomRight:
                return element.getWidth();
        }
        return 0;
    }

    protected static int getYOffset(GuiElement element, GuiAttachment attachment) {
        switch (attachment) {
            case topLeft:
            case topCenter:
            case topRight:
                return 0;
            case middleLeft:
            case middleCenter:
            case middleRight:
                return element.getHeight() / 2;
            case bottomCenter:
            case bottomLeft:
            case bottomRight:
                return element.getHeight();
        }
        return 0;
    }

    public boolean onMouseClick(int x, int y, int button) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            if (elements.get(i).isVisible()) {
                if (elements.get(i).onMouseClick(x, y, button)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void onMouseRelease(int x, int y, int button) {
        elements.forEach(element -> element.onMouseRelease(x, y, button));
    }


    public boolean onMouseScroll(double mouseX, double mouseY, double distance) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            if (elements.get(i).isVisible()) {
                if (elements.get(i).onMouseScroll(mouseX, mouseY, distance)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            if (elements.get(i).isVisible()) {
                if (elements.get(i).onKeyPress(keyCode, scanCode, modifiers)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean onKeyRelease(int keyCode, int scanCode, int modifiers) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            if (elements.get(i).isVisible()) {
                if (elements.get(i).onKeyRelease(keyCode, scanCode, modifiers)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean onCharType(char character, int modifiers) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            if (elements.get(i).isVisible()) {
                if (elements.get(i).onCharType(character, modifiers)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void updateFocusState(int refX, int refY, int mouseX, int mouseY) {
        elements.stream()
                .filter(GuiElement::isVisible)
                .forEach(element -> element.updateFocusState(
                        refX + x + getXOffset(this, element.attachmentAnchor) - getXOffset(element, element.attachmentPoint),
                        refY + y + getYOffset(this, element.attachmentAnchor) - getYOffset(element, element.attachmentPoint),
                        mouseX, mouseY));

        boolean gainFocus = mouseX >= getX() + refX
                && mouseX < getX() + refX + getWidth()
                && mouseY >= getY() + refY
                && mouseY < getY() + refY + getHeight();

        if (gainFocus != hasFocus) {
            hasFocus = gainFocus;
            if (hasFocus) {
                onFocus();
            } else {
                onBlur();
            }
        }
    }

    protected void onFocus() {

    }

    protected void onBlur() {

    }

    public boolean hasFocus() {
        return hasFocus;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Set which point, relative this element, that it should be positioned on.
     * @param attachment
     * @return
     */
    public GuiElement setAttachmentPoint(GuiAttachment attachment) {
        attachmentPoint = attachment;

        return this;
    }

    /**
     * Set which point, relative the parent, that this element should be positioned on.
     * @param attachment
     * @return
     */
    public GuiElement setAttachmentAnchor(GuiAttachment attachment) {
        attachmentAnchor = attachment;

        return this;
    }

    public GuiElement setAttachment(GuiAttachment attachment) {
        attachmentPoint = attachment;
        attachmentAnchor = attachment;

        return this;
    }

    public GuiAttachment getAttachmentPoint() {
        return attachmentPoint;
    }

    public GuiAttachment getAttachmentAnchor() {
        return attachmentAnchor;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setVisible(boolean visible) {
        if (isVisible != visible) {
            if (visible) {
                onShow();
            } else {
                if (!onHide()) {
                    return;
                }
            }
            isVisible = visible;
        }
    }

    public boolean isVisible() {
        return isVisible;
    }

    protected void onShow() {}

    /**
     * Can be overridden to do something when the element is hidden. Returning false indicates that the handler will
     * take care of setting isVisible to false.
     * @return
     */
    protected boolean onHide() {
        this.hasFocus = false;
        return true;
    }

    public GuiElement setOpacity(float opacity) {
        this.opacity = opacity;
        return this;
    }

    public float getOpacity() {
        return opacity;
    }

    public void addAnimation(KeyframeAnimation animation) {
        activeAnimations.add(animation);
    }

    public void removeAnimation(KeyframeAnimation animation) {
        activeAnimations.remove(animation);
    }

    public void remove() {
        shouldRemove = true;
    }

    public boolean shouldRemove() {
        return shouldRemove;
    }

    public void addChild(GuiElement child) {
        this.elements.add(child);
    }

    public void clearChildren() {
        this.elements.clear();
    }

    public int getNumChildren() {
        return elements.size();
    }

    public GuiElement getChild(int index) {
        if (index >= 0 && index < elements.size()) {
            return elements.get(index);
        }
        return null;
    }

    public List<GuiElement> getChildren() {
        return Collections.unmodifiableList(elements);
    }

    /**
     * Return child elements which has the given type
     * @param type
     * @param <T>
     * @return
     */
    public <T> List<T> getChildren(Class<T> type) {
        return elements.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    // todo 1.16: rewrite to use ITextComponent instead of String
    @Deprecated
    public List<String> getTooltipLines() {
        if (isVisible()) {
            return elements.stream()
                    .map(GuiElement::getTooltipLines)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }


    protected static void drawRect(PoseStack matrixStack, int left, int top, int right, int bottom, int color, float opacity) {
        fill(matrixStack, left, top, right, bottom, colorWithOpacity(color, opacity));
    }

    protected static int colorWithOpacity(int color, float opacity) {
        return colorWithOpacity(color, Math.round(opacity * 255));
    }

    protected static int colorWithOpacity(int color, int opacity) {
        // replace alpha bits with passed opacity value, multiples opacity with current alpha bits if they are present
        return color & 0xffffff | (opacity * (color >> 24 == 0 ? 255 : color >> 24 & 255) / 255 << 24);
    }
}
