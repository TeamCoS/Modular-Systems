package com.pauljoda.modularsystems.furnace.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.pauljoda.modularsystems.core.proxy.ClientProxy;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceDummy;

public class BlockFurnaceAddition extends BlockBasicDummy
{
    public BlockFurnaceAddition()
    {
        super(Material.rock, true);
        
        setBlockName("modularsystems:blockFurnaceAddition");
        setStepSound(Block.soundTypeStone);
        setHardness(3.5f);  
    }
    public int meta = 0;
   
    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("furnace_side");
    }    
}