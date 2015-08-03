package com.teambr.modularsystems.storage.blocks

import com.teambr.modularsystems.core.lib.Reference
import com.teambr.modularsystems.temp.blocks.BlockBase
import net.minecraft.block.material.Material

/**
 * Modular-Systems
 * Created by Dyonovan on 02/08/15
 */
class BlockStorageCore extends BlockBase(Material.wood, Reference.MOD_ID + ":storageCore", TileStorageCore.class) {


    override def createNewTileEntity(worldIn: World, meta: Int): TileEntity = ???
}
