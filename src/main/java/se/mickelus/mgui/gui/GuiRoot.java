package se.mickelus.mgui.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.TransformationMatrix;

public class GuiRoot extends GuiElement {

    protected Minecraft mc;

    public GuiRoot(Minecraft mc) {
        super(0, 0, 0 ,0);
        this.mc = mc;
    }

    public void draw() {
        if (isVisible()) {
            MainWindow window = mc.getMainWindow();

            width = window.getScaledWidth();
            height = window.getScaledHeight();
            double mouseX = mc.mouseHelper.getMouseX() * width / window.getWidth();
            double mouseY = mc.mouseHelper.getMouseY() * height / window.getHeight();

            drawChildren(new MatrixStack(), 0, 0, width, height, (int) mouseX, (int) mouseY, 1);
        }
    }
}
