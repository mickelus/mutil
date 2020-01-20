package se.mickelus.mgui.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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

//        xPos and yPos are final in class Slot.
//        if (enabled) {
//            xPos = realX;
//            yPos = realY;
//        } else {
//            xPos = -10000;
//            yPos = -10000;
//        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public boolean canTakeStack(PlayerEntity playerIn) {
        return isEnabled;
    }

    @Override
    public boolean isItemValid(@Nullable ItemStack stack) {
        return isEnabled;
    }
}
