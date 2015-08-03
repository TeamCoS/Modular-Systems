package com.teambr.modularsystems.temp.blocks

import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.tileentity.TileEntity

/**
 * Modular-Systems
 * Created by Dyonovan on 02/08/15
 */
abstract class BlockBase(val material: Material, val name: String, val tile: Class[_ <: TileEntity]) extends BlockContainer(material) {
    setUnlocalizedName(name)
    setCreativeTab(getCreativeTab match {
        case Some(i) => i
        case _ => null
    })
    setHardness(getHardness)

    def getCreativeTab : Option[CreativeTabs] = None

    def getHardness: Float = 2.0F
}

