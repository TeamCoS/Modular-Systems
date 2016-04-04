package com.teambr.modularsystems.storage.container.slot;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis "pauljoda"
 * @since 4/3/2016
 */
public class PlayerArmorSlot extends Slot {

    private static final EntityEquipmentSlot[] slotArray =
            new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST,
                    EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
    private EntityEquipmentSlot entityequipmentslot;

    public PlayerArmorSlot(InventoryPlayer inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        entityequipmentslot = slotArray[3 - (index - 36)];
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1
     * in the case of armor slots)
     */
    public int getSlotStackLimit()
    {
        return 1;
    }
    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack stack) {
        return stack != null && stack.getItem().isValidArmor(stack, entityequipmentslot, ((InventoryPlayer) inventory).player);
    }

    @SideOnly(Side.CLIENT)
    public String getSlotTexture()
    {
        return ItemArmor.EMPTY_SLOT_NAMES[entityequipmentslot.getIndex()];
    }
}
