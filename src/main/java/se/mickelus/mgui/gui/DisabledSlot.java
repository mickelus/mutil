package se.mickelus.mgui.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class DisabledSlot extends Slot {
    public DisabledSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPickup(PlayerEntity playerIn) {
        return false;
    }

    @Override
    public boolean mayPlace(@Nullable ItemStack stack) {
        return false;
    }
}
