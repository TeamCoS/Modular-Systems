package com.teambr.modularsystems.power.container

import com.teambr.bookshelf.common.container.BaseContainer
import com.teambr.modularsystems.power.tiles.TileBankSolids
import net.minecraft.entity.player.InventoryPlayer

/**
 * This file was created for NeoTech
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 07, 2015
 */
class ContainerSolidsBank(playerInventory: InventoryPlayer, tileEntity: TileBankSolids)
        extends BaseContainer(playerInventory, tileEntity) {

    addInventoryGrid(8, 20, 9)
    addPlayerInventorySlots(8, 84)
}
