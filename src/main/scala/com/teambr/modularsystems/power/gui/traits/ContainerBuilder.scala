package com.teambr.modularsystems.power.gui.traits

import com.teambr.modularsystems.power.container.ContainerSolidsBank
import com.teambr.modularsystems.power.tiles.TileBankSolids
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container

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
trait ContainerBuilder[C <: Container] {
    def build(player: InventoryPlayer, title: TileBankSolids): C
}

object ContainerBuilder {
    def apply[C <: Container: ContainerBuilder]: ContainerBuilder[C] = implicitly[ContainerBuilder[C]]

    implicit val containerSolidsBank: ContainerBuilder[ContainerSolidsBank] = {
        new ContainerBuilder[ContainerSolidsBank] {
            def build(player: InventoryPlayer, tile: TileBankSolids): ContainerSolidsBank = new ContainerSolidsBank(player, tile)
        }
    }
}
