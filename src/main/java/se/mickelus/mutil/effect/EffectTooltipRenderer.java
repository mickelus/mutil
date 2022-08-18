package se.mickelus.mutil.effect;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;

import java.util.function.Function;
import java.util.function.Supplier;

public class EffectTooltipRenderer implements IClientMobEffectExtensions {
    private final Function<MobEffectInstance, String> constructEffectTooltip;

    public EffectTooltipRenderer(Function<MobEffectInstance, String> constructEffectTooltip) {
        this.constructEffectTooltip = constructEffectTooltip;
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderInventoryEffectTooltip(EffectRenderingInventoryScreen<?> screen, PoseStack poseStack, int x, int y, Supplier<Component> tooltip) {
        Minecraft mc = Minecraft.getInstance();
        Window window = mc.getWindow();

        int width = window.getGuiScaledWidth();
        int height = window.getGuiScaledHeight();
        int mouseX = (int) (mc.mouseHandler.xpos() * width / window.getScreenWidth());
        int mouseY = (int) (mc.mouseHandler.ypos() * height / window.getScreenHeight());

        if (x < mouseX && mouseX < x + 120 && y < mouseY && mouseY < y + 32) {
            screen.renderTooltip(poseStack, tooltip.get(), mouseX, mouseY);
        }
    }

    @Override
    public boolean renderInventoryIcon(MobEffectInstance instance, EffectRenderingInventoryScreen<?> screen, PoseStack poseStack, int x, int y, int blitOffset) {
        renderInventoryEffectTooltip(screen, poseStack, x, y, () -> Component.literal(constructEffectTooltip.apply(instance)));
        return false;
    }
}
