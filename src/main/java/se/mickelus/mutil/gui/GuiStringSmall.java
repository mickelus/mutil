package se.mickelus.mutil.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;

public class GuiStringSmall extends GuiString {

    public GuiStringSmall(int x, int y, String string) {
        super(x*2, y*2, string);
    }

    public GuiStringSmall(int x, int y, String string, int color) {
        super(x*2, y*2, string, color);
    }

    public GuiStringSmall(int x, int y, String string, GuiAttachment attachment) {
        super(x*2, y*2, string, attachment);
    }

    public GuiStringSmall(int x, int y, String string, int color, GuiAttachment attachment) {
        super(x*2, y*2, string, color, attachment);
    }

    @Override
    public void setX(int x) {
        super.setX(x * 2);
    }

    @Override
    public void setY(int y) {
        super.setY(y * 2);
    }

    @Override
    public int getX() {
        return super.getX() / 2;
    }

    @Override
    public int getY() {
        return super.getY() / 2;
    }

    @Override
    public int getWidth() {
        return width / 2;
    }

    @Override
    public int getHeight() {
        return height / 2;
    }

    @Override
    protected void calculateFocusState(int refX, int refY, int mouseX, int mouseY) {
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

    @Override
    public void draw(PoseStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        calculateFocusState(refX, refY, mouseX, mouseY);
        activeAnimations.removeIf(keyframeAnimation -> !keyframeAnimation.isActive());
        activeAnimations.forEach(KeyframeAnimation::preDraw);
        matrixStack.pushPose();
        matrixStack.scale(.5f, .5f, .5f);
        drawString(matrixStack, string, refX * 2 + x, refY * 2 + y, color, opacity * getOpacity(), drawShadow);
        matrixStack.popPose();
    }
}
