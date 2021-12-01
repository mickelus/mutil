package se.mickelus.mgui.gui;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class ToggleableSlot extends SlotItemHandler {

    private boolean isEnabled = true;
    private int realX, realY;

    public ToggleableSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);

        realX = xPosition;
        realY = yPosition;
    }

    public void toggle(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public boolean isActive() {
        return isEnabled;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return isEnabled;
    }

    @Override
    public boolean mayPlace(@Nullable ItemStack stack) {
        return isEnabled;
    }
}
