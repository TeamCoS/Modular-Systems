package com.teambr.modularsystems.storage.container

import com.teambr.bookshelf.common.container.BaseContainer
import net.minecraft.inventory.IInventory

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis pauljoda
 * @since August 14, 2015
 */
class ContainerSmashing(playerInventory : IInventory, ownerInventory : IInventory) extends BaseContainer(playerInventory, ownerInventory) {
    addSlotToContainer(new RestrictedSlot(ownerInventory, 0, 80, 35))
    addPlayerInventorySlots(84)
}
