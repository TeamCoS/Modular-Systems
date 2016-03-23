package com.teambr.modularsystems.power.container

import com.teambr.bookshelf.common.container.BaseContainer
import com.teambr.modularsystems.core.slots.SlotFurnaceOutputItemHandler
import com.teambr.modularsystems.power.tiles.TileBankLiquids
import net.minecraft.entity.player.InventoryPlayer

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
class ContainerBankLiquids(playerInventory: InventoryPlayer, tileEntity: TileBankLiquids)
        extends BaseContainer(playerInventory, tileEntity) {

    addSlotToContainer(new RestrictedSlot(tileEntity, 0, 25, 20))
    addSlotToContainer(new SlotFurnaceOutputItemHandler(playerInventory.player, tileEntity, 1, 25, 50))
    addPlayerInventorySlots(8, 84)
}
