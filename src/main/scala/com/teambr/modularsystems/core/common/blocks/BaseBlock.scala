package com.teambr.modularsystems.core.common.blocks

import com.teambr.modularsystems.core.ModularSystems
import com.teambr.modularsystems.core.lib.Reference
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

/**
 * Modular-Systems
 * Created by Dyonovan on 04/08/15
 */
class BaseBlock(material: Material, name: String, tileEntity: Class[_ <: TileEntity]) extends BlockContainer(material) {

    //Construction
    setUnlocalizedName(Reference.MOD_ID + ":" + name)
    setCreativeTab(getCreativeTab)
    setHardness(getHardness)


    /**
     * Used to change the hardness of a block, but will default to 2.0F if not overwritten
     * @return The hardness value, default 2.0F
     */
    def getHardness: Float = 2.0F


    /**
     * Used to tell if this should be in a creative tab, and if so which one
     * @return Null if none, defaults to the main Modular Systems Tab
     */
    def getCreativeTab: CreativeTabs = ModularSystems.tabModularSystems


    override def createNewTileEntity(worldIn: World, meta: Int): TileEntity =
        if (tileEntity != null) tileEntity.newInstance() else null

}
