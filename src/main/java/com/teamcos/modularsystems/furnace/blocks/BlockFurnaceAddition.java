package com.teamcos.modularsystems.furnace.blocks;

import com.teamcos.modularsystems.core.ModularSystems;
import com.teamcos.modularsystems.core.managers.BlockManager;
import com.teamcos.modularsystems.core.proxy.ClientProxy;
import com.teamcos.modularsystems.helpers.Coord;
import com.teamcos.modularsystems.registries.FurnaceConfigHandler;
import com.teamcos.modularsystems.utilities.block.DummyBlock;
import com.teamcos.modularsystems.utilities.tiles.DummyTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFurnaceAddition extends DummyBlock {
    public BlockFurnaceAddition() {
        super(ModularSystems.tabModularSystems, Material.rock, true);
        
        setBlockName("modularsystems:blockFurnaceAddition");
        setStepSound(Block.soundTypeStone);
        setHardness(3.5f);
    }
    public int meta = 0;
   
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon("furnace_side");
    }

    public int getRenderType() {
        return ClientProxy.msRenderId;
    }

    @Override
    public boolean canRenderInPass(int pass) {
        //Set the static var in the client proxy
        ClientProxy.renderPass = pass;
        //the block can render in both passes, so return true always
        return true;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
        return Item.getItemFromBlock(BlockManager.furnaceAddition);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        DummyTile dummy = new DummyTile();
        dummy.setBlock(getIdFromBlock(this));
        return dummy;
    }

    @Override
    public int getMultiplier(World world, int x, int y, int z, int blockCount) {
        return FurnaceConfigHandler.getSmeltingMultiplierForBlock(world, new Coord(x, y, z), this, blockCount);
    }
}
