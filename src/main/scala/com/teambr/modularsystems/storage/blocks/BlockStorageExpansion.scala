package com.teambr.modularsystems.storage.blocks

import com.teambr.bookshelf.Bookshelf
import com.teambr.modularsystems.core.common.blocks.BaseBlock
import com.teambr.modularsystems.storage.tiles.{ TileEntityStorageExpansion, TileStorageCore }
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.{ BlockState, IBlockState }
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{ EnumWorldBlockLayer, BlockPos, EnumFacing }
import net.minecraft.world.{ IBlockAccess, World }
import net.minecraftforge.common.property.{ ExtendedBlockState, IUnlistedProperty }

/**
 * Modular-Systems
 * Created by Dyonovan on 05/08/15
 *
 * Used as a common class for all blocks. Makes things a bit easier
 *
 * @param name The unlocalized name of the block
 * @param icons List of block side icons, up to 6. Less than 6 will default to just blockName
 * @param tileEntity Should the block have a tile, pass the class
 */
class BlockStorageExpansion(name: String, icons: List[String], tileEntity: Class[_ <: TileEntity])
        extends BaseBlock(Material.wood, name, tileEntity: Class[_ <: TileEntity]) {

    override def onBlockAdded(world: World, pos: BlockPos, state : IBlockState) : Unit = {
        world.getTileEntity(pos) match {
            case tile : TileEntityStorageExpansion =>
                tile.searchAndConnect()
            case t =>
        }
    }

    override def onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
        if (world.getTileEntity(pos).asInstanceOf[TileEntityStorageExpansion].getCore.isDefined) {
            val corePos = world.getTileEntity(pos).asInstanceOf[TileEntityStorageExpansion].getCore.get.getPos
            if (world.getTileEntity(corePos).asInstanceOf[TileStorageCore].canOpen(player)) {
                player.openGui(Bookshelf, 0, world, corePos.getX, corePos.getY, corePos.getZ)
            }
        }
        true
    }

    override def breakBlock(world: World, pos: BlockPos, state: IBlockState) {
        world.getTileEntity(pos).asInstanceOf[TileEntityStorageExpansion].removeFromNetwork(true)
        super.breakBlock(world, pos, state)
    }

    /**
     * Used to say what our block state is
     */
    override def createBlockState() : BlockState = {
        val listed : Array[IProperty] = new Array(0)
        val unlisted = new Array[IUnlistedProperty[_]](0)
        new ExtendedBlockState(this, listed, unlisted)
    }

    override def getExtendedState(state : IBlockState, world : IBlockAccess, pos : BlockPos) : IBlockState = {
        val hasConnection : java.lang.Boolean = world.getTileEntity(pos).asInstanceOf[TileEntityStorageExpansion].getCore.isDefined
        new StorageState(hasConnection, pos, world, state.getBlock)
    }

    def getConnectionArrayForFace(world : IBlockAccess, pos : BlockPos,  facing : EnumFacing): Array[Boolean] = {
        val connections = new Array[Boolean](16)
         facing match {
                case EnumFacing.UP =>
                    connections(0) = canBlockConnect(world.getBlockState(pos.add(-1, 0, -1)).getBlock)
                    connections(1) = canBlockConnect(world.getBlockState(pos.add(0, 0, -1)).getBlock)
                    connections(2) = canBlockConnect(world.getBlockState(pos.add(1, 0, -1)).getBlock)
                    connections(3) = canBlockConnect(world.getBlockState(pos.add(-1, 0, 0)).getBlock)
                    connections(4) = canBlockConnect(world.getBlockState(pos.add(1, 0, 0)).getBlock)
                    connections(5) = canBlockConnect(world.getBlockState(pos.add(-1, 0, 1)).getBlock)
                    connections(6) = canBlockConnect(world.getBlockState(pos.add(0, 0, 1)).getBlock)
                    connections(7) = canBlockConnect(world.getBlockState(pos.add(1, 0, 1)).getBlock)
                    connections(8) = !world.isAirBlock(pos.add(-1, 1, -1))
                    connections(9) = !world.isAirBlock(pos.add(0, 1, -1))
                    connections(10) = !world.isAirBlock(pos.add(1, 1, -1))
                    connections(11) = !world.isAirBlock(pos.add(-1, 1, 0))
                    connections(12) = !world.isAirBlock(pos.add(1, 1, 0))
                    connections(13) = !world.isAirBlock(pos.add(-1, 1, 1))
                    connections(14) = !world.isAirBlock(pos.add(0, 1, 1))
                    connections(15) = !world.isAirBlock(pos.add(1, 1, 1))
                    return connections
                case EnumFacing.DOWN =>
                    connections(0) = canBlockConnect(world.getBlockState(pos.add(-1, 0, 1)).getBlock)
                    connections(1) = canBlockConnect(world.getBlockState(pos.add(0, 0, 1)).getBlock)
                    connections(2) = canBlockConnect(world.getBlockState(pos.add(1, 0, 1)).getBlock)
                    connections(3) = canBlockConnect(world.getBlockState(pos.add(-1, 0, 0)).getBlock)
                    connections(4) = canBlockConnect(world.getBlockState(pos.add(1, 0, 0)).getBlock)
                    connections(5) = canBlockConnect(world.getBlockState(pos.add(-1, 0, -1)).getBlock)
                    connections(6) = canBlockConnect(world.getBlockState(pos.add(0, 0, -1)).getBlock)
                    connections(7) = canBlockConnect(world.getBlockState(pos.add(1, 0, -1)).getBlock)
                    connections(8) = !world.isAirBlock(pos.add(-1, -1, 1))
                    connections(9) = !world.isAirBlock(pos.add(0, -1, 1))
                    connections(10) = !world.isAirBlock(pos.add(1, -1, 1))
                    connections(11) = !world.isAirBlock(pos.add(-1, -1, 0))
                    connections(12) = !world.isAirBlock(pos.add(1, -1, 0))
                    connections(13) = !world.isAirBlock(pos.add(-1, -1, -1))
                    connections(14) = !world.isAirBlock(pos.add(0, -1, -1))
                    connections(15) = !world.isAirBlock(pos.add(1, -1, -1))
                    return connections
                case EnumFacing.NORTH =>
                    connections(0) = canBlockConnect(world.getBlockState(pos.add(1, 1, 0)).getBlock)
                    connections(1) = canBlockConnect(world.getBlockState(pos.add(0, 1, 0)).getBlock)
                    connections(2) = canBlockConnect(world.getBlockState(pos.add(-1, 1, 0)).getBlock)
                    connections(3) = canBlockConnect(world.getBlockState(pos.add(1, 0, 0)).getBlock)
                    connections(4) = canBlockConnect(world.getBlockState(pos.add(-1, 0, 0)).getBlock)
                    connections(5) = canBlockConnect(world.getBlockState(pos.add(1, -1, 0)).getBlock)
                    connections(6) = canBlockConnect(world.getBlockState(pos.add(0, -1, 0)).getBlock)
                    connections(7) = canBlockConnect(world.getBlockState(pos.add(-1, -1, 0)).getBlock)
                    connections(8) = !world.isAirBlock(pos.add(1, 1, -1))
                    connections(9) = !world.isAirBlock(pos.add(0, 1, -1))
                    connections(10) = !world.isAirBlock(pos.add(-1, 1, -1))
                    connections(11) = !world.isAirBlock(pos.add(1, 0, -1))
                    connections(12) = !world.isAirBlock(pos.add(-1, 0, -1))
                    connections(13) = !world.isAirBlock(pos.add(1, -1, -1))
                    connections(14) = !world.isAirBlock(pos.add(0, -1, -1))
                    connections(15) = !world.isAirBlock(pos.add(-1, -1, -1))
                    return connections
                case EnumFacing.SOUTH =>
                    connections(0) = canBlockConnect(world.getBlockState(pos.add(-1, 1, 0)).getBlock)
                    connections(1) = canBlockConnect(world.getBlockState(pos.add(0, 1, 0)).getBlock)
                    connections(2) = canBlockConnect(world.getBlockState(pos.add(1, 1, 0)).getBlock)
                    connections(3) = canBlockConnect(world.getBlockState(pos.add(-1, 0, 0)).getBlock)
                    connections(4) = canBlockConnect(world.getBlockState(pos.add(1, 0, 0)).getBlock)
                    connections(5) = canBlockConnect(world.getBlockState(pos.add(-1, -1, 0)).getBlock)
                    connections(6) = canBlockConnect(world.getBlockState(pos.add(0, -1, 0)).getBlock)
                    connections(7) = canBlockConnect(world.getBlockState(pos.add(1, -1, 0)).getBlock)
                    connections(8) = !world.isAirBlock(pos.add(-1, 1, 1))
                    connections(9) = !world.isAirBlock(pos.add(0, 1, 1))
                    connections(10) = !world.isAirBlock(pos.add(1, 1, 1))
                    connections(11) = !world.isAirBlock(pos.add(-1, 0, 1))
                    connections(12) = !world.isAirBlock(pos.add(1, 0, 1))
                    connections(13) = !world.isAirBlock(pos.add(-1, -1, 1))
                    connections(14) = !world.isAirBlock(pos.add(0, -1, 1))
                    connections(15) = !world.isAirBlock(pos.add(1, -1, 1))
                    return connections
                case EnumFacing.WEST =>
                    connections(0) = canBlockConnect(world.getBlockState(pos.add(0, 1, -1)).getBlock)
                    connections(1) = canBlockConnect(world.getBlockState(pos.add(0, 1, 0)).getBlock)
                    connections(2) = canBlockConnect(world.getBlockState(pos.add(0, 1, 1)).getBlock)
                    connections(3) = canBlockConnect(world.getBlockState(pos.add(0, 0, -1)).getBlock)
                    connections(4) = canBlockConnect(world.getBlockState(pos.add(0, 0, 1)).getBlock)
                    connections(5) = canBlockConnect(world.getBlockState(pos.add(0, -1, -1)).getBlock)
                    connections(6) = canBlockConnect(world.getBlockState(pos.add(0, -1, 0)).getBlock)
                    connections(7) = canBlockConnect(world.getBlockState(pos.add(0, -1, 1)).getBlock)
                    connections(8) = !world.isAirBlock(pos.add(-1, 1, -1))
                    connections(9) = !world.isAirBlock(pos.add(-1, 1, 0))
                    connections(10) = !world.isAirBlock(pos.add(-1, 1, 1))
                    connections(11) = !world.isAirBlock(pos.add(-1, 0, -1))
                    connections(12) = !world.isAirBlock(pos.add(-1, 0, 1))
                    connections(13) = !world.isAirBlock(pos.add(-1, -1, -1))
                    connections(14) = !world.isAirBlock(pos.add(-1, -1, 0))
                    connections(15) = !world.isAirBlock(pos.add(-1, -1, 1))
                    return connections
                case EnumFacing.EAST =>
                    connections(0) = canBlockConnect(world.getBlockState(pos.add(0, 1, 1)).getBlock)
                    connections(1) = canBlockConnect(world.getBlockState(pos.add(0, 1, 0)).getBlock)
                    connections(2) = canBlockConnect(world.getBlockState(pos.add(0, 1, -1)).getBlock)
                    connections(3) = canBlockConnect(world.getBlockState(pos.add(0, 0, 1)).getBlock)
                    connections(4) = canBlockConnect(world.getBlockState(pos.add(0, 0, -1)).getBlock)
                    connections(5) = canBlockConnect(world.getBlockState(pos.add(0, -1, 1)).getBlock)
                    connections(6) = canBlockConnect(world.getBlockState(pos.add(0, -1, 0)).getBlock)
                    connections(7) = canBlockConnect(world.getBlockState(pos.add(0, -1, -1)).getBlock)
                    connections(8) = !world.isAirBlock(pos.add(1, 1, 1))
                    connections(9) = !world.isAirBlock(pos.add(1, 1, 0))
                    connections(10) = !world.isAirBlock(pos.add(1, 1, -1))
                    connections(11) = !world.isAirBlock(pos.add(1, 0, 1))
                    connections(12) = !world.isAirBlock(pos.add(1, 0, -1))
                    connections(13) = !world.isAirBlock(pos.add(1, -1, 1))
                    connections(14) = !world.isAirBlock(pos.add(1, -1, 0))
                    connections(15) = !world.isAirBlock(pos.add(1, -1, -1))
                    return connections
                case _ => return connections
            }
        connections
    }

    def canBlockConnect(block : Block): Boolean = {
        block.isInstanceOf[BlockStorageExpansion]
    }

    override def canRenderInLayer(layer : EnumWorldBlockLayer) : Boolean =
        layer == EnumWorldBlockLayer.CUTOUT || layer == EnumWorldBlockLayer.TRANSLUCENT

    override def getRenderType : Int = 3
}
