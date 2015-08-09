package com.teambr.modularsystems.crusher.container

import com.teambr.bookshelf.common.container.BaseContainer
import com.teambr.modularsystems.crusher.tiles.TileCrusherCore
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.SlotFurnaceOutput

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 08, 2015
 */
class ContainerCrusherCore(playerInventory: InventoryPlayer, tile: TileCrusherCore)
        extends BaseContainer(playerInventory, tile) {

    addSlotToContainer(new RestrictedSlot(tile, 0, 36, 35))
    addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, tile, 1, 96, 35))
    addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, tile, 2, 126, 35))
    addPlayerInventorySlots(8, 84)
}
