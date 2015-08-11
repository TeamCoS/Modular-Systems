package com.teambr.modularsystems.storage.container

import com.teambr.bookshelf.common.container.BaseContainer
import com.teambr.modularsystems.storage.tiles.TileStorageRemote
import net.minecraft.entity.player.InventoryPlayer

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 10, 2015
 */
class ContainerStorageRemote(playerInventory: InventoryPlayer, tile: TileStorageRemote)
        extends BaseContainer(playerInventory, tile) {

    addSlotToContainer(new RestrictedSlot(tile, 0, 56, 35))
    addSlotToContainer(new RestrictedSlot(tile, 1, 116, 35))
    addPlayerInventorySlots(84)


}
