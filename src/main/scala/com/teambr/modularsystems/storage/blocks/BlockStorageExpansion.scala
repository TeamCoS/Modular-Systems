package com.teambr.modularsystems.storage.blocks

import com.teambr.bookshelf.Bookshelf
import com.teambr.bookshelf.collections.CubeTextures
import com.teambr.bookshelf.common.blocks.traits.BlockBakeable
import com.teambr.modularsystems.core.common.blocks.BaseBlock
import com.teambr.modularsystems.core.lib.Reference
import com.teambr.modularsystems.storage.tiles.{ TileEntityStorageExpansion, TileStorageCore }
import net.minecraft.block.material.Material
import net.minecraft.block.properties.{ IProperty, PropertyBool }
import net.minecraft.block.state.{ BlockState, IBlockState }
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{ BlockPos, EnumFacing, ResourceLocation }
import net.minecraft.world.{ IBlockAccess, World }
import net.minecraftforge.common.property.{ ExtendedBlockState, IUnlistedProperty }

import scala.collection.mutable.ListBuffer

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
        extends BaseBlock(Material.wood, name, tileEntity: Class[_ <: TileEntity]) with BlockBakeable  {
    lazy val PROPERTY_CONNECTED = PropertyBool.create("Connected")


    override def MODID: String = Reference.MOD_ID
    override def blockName: String = name

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

    override def getDisplayTextures(state : IBlockState): CubeTextures = {
        val map = Minecraft.getMinecraft.getTextureMapBlocks
        val sides = new ListBuffer[String]
        if (state.getValue(PROPERTY_CONNECTED).asInstanceOf[Boolean]) {
            for (icon <- icons) {
                sides += MODID + ":blocks/" + blockName + icon
            }
            for (i <- icons.size to 6) {
                sides += MODID + ":blocks/" + blockName
            }
            val textures = new CubeTextures(
                map.getTextureExtry(sides.head),
                map.getTextureExtry(sides(1)),
                map.getTextureExtry(sides(2)),
                map.getTextureExtry(sides(3)),
                map.getTextureExtry(sides(4)),
                map.getTextureExtry(sides(5))
            )
            textures
        } else
            new CubeTextures(
                map.getTextureExtry(MODID + ":blocks/" + "storageNoConnection"),
                map.getTextureExtry(MODID + ":blocks/" + "storageNoConnection"),
                map.getTextureExtry(MODID + ":blocks/" + "storageNoConnection"),
                map.getTextureExtry(MODID + ":blocks/" + "storageNoConnection"),
                map.getTextureExtry(MODID + ":blocks/" + "storageNoConnection"),
                map.getTextureExtry(MODID + ":blocks/" + "storageNoConnection")
            )
    }

    override def registerIcons(): Array[ResourceLocation] = {
        val locations = new ListBuffer[ResourceLocation]
        if (icons.size < 6) locations += new ResourceLocation(MODID + ":blocks/" + blockName)
        for (icon <- icons.indices) {
            locations += new ResourceLocation(MODID + ":blocks/" + blockName + icon)
        }
        locations += new ResourceLocation(MODID + ":blocks/" + "storageNoConnection")
        locations.toArray
    }

    override def createBlockState() : BlockState = {
        val listed = Array[IProperty](PROPERTY_CONNECTED) //Used to create sub variants
        val unListed = new Array[IUnlistedProperty[_]] (0) //Things we want, but don't need displayed
        new ExtendedBlockState(this, listed, unListed)
    }

    override def getExtendedState(state : IBlockState, world : IBlockAccess, pos : BlockPos) : IBlockState = {
        val hasConnection : java.lang.Boolean = world.getTileEntity(pos).asInstanceOf[TileEntityStorageExpansion].getCore.isDefined
        state.withProperty(PROPERTY_CONNECTED, hasConnection)
    }

    /**
     * Used to convert the meta to state
     * @param meta The meta
     * @return
     */
    override def getStateFromMeta(meta : Int) : IBlockState = getDefaultState.withProperty(PROPERTY_CONNECTED, java.lang.Boolean.valueOf(meta > 0))

    /**
     * Called to convert state from meta
     * @param state The state
     * @return
     */
    override def getMetaFromState(state : IBlockState) = if(state.getValue(PROPERTY_CONNECTED).asInstanceOf[Boolean]) 1 else 0


    override def getAllPossibleStates: Array[IBlockState] = {
        Array[IBlockState](getDefaultState.withProperty(PROPERTY_CONNECTED, true),
            getDefaultState.withProperty(PROPERTY_CONNECTED, false))
    }
}
