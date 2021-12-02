package se.mickelus.mutil.gui;

import com.mojang.blaze3d.vertex.PoseStack;

public class GuiClickable extends GuiElement {

    protected final Runnable onClickHandler;

    public GuiClickable(int x, int y, int width, int height, Runnable onClickHandler) {
        super(x, y, width, height);

        this.onClickHandler = onClickHandler;
    }

    @Override
    public void draw(PoseStack matrixStack, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(matrixStack, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
    }

    @Override
    public boolean onMouseClick(int x, int y, int button) {
        if (hasFocus()) {
            onClickHandler.run();
            return true;
        }

        return false;
    }
}
