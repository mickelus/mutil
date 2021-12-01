package se.mickelus.mgui.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;

public class GuiRoot extends GuiElement {

    protected Minecraft mc;

    public GuiRoot(Minecraft mc) {
        super(0, 0, 0 ,0);
        this.mc = mc;
    }

    public void draw() {
        if (isVisible()) {
            MainWindow window = mc.getWindow();

            width = window.getGuiScaledWidth();
            height = window.getGuiScaledHeight();
            double mouseX = mc.mouseHandler.xpos() * width / window.getScreenWidth();
            double mouseY = mc.mouseHandler.ypos() * height / window.getScreenHeight();

            drawChildren(new MatrixStack(), 0, 0, width, height, (int) mouseX, (int) mouseY, 1);
        }
    }
}
