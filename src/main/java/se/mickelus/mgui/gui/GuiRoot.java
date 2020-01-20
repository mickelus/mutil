package se.mickelus.mgui.gui;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;

public class GuiRoot extends GuiElement {

    protected Minecraft mc;

    public GuiRoot(Minecraft mc) {
        super(0, 0, 0 ,0);
        this.mc = mc;
    }

    public void draw() {
        MainWindow window = mc.func_228018_at_();

        int width = window.getScaledWidth();
        int height = window.getScaledHeight();
        double mouseX = mc.mouseHelper.getMouseX() * width / window.getWidth();
        double mouseY = height - mc.mouseHelper.getMouseY() * height / window.getHeight() - 1;
        drawChildren(0,0, width, height, (int) mouseX, (int) mouseY, 1);
    }

}
