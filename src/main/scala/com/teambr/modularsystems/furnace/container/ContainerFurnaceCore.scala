package com.teambr.modularsystems.furnace.container

import com.teambr.bookshelf.common.container.BaseContainer
import com.teambr.modularsystems.furnace.tiles.TileEntityFurnaceCore
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
 * @since August 07, 2015
 */
class ContainerFurnaceCore(playerInventory: InventoryPlayer, tile: TileEntityFurnaceCore)
        extends BaseContainer(playerInventory, tile) {

    addSlotToContainer(new RestrictedSlot(tile, 0, 56, 35))
    addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, tile, 1, 116, 35))
    addPlayerInventorySlots(8, 84)

}
