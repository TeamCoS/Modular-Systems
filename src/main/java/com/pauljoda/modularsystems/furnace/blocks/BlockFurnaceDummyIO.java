package com.pauljoda.modularsystems.furnace.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.pauljoda.modularsystems.ModularSystems;
import com.pauljoda.modularsystems.proxy.ClientProxy;

public class BlockFurnaceDummyIO extends Block {

    public BlockFurnaceDummyIO(Material par2Material) {
        super(par2Material);
        
        setBlockName("modularsystems:blockFurnaceIOBlock");
        setStepSound(Block.soundTypeStone);
        setHardness(3.5f);
        setCreativeTab(ModularSystems.tabModularSystems);
    }
    
    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("dispenser_front_vertical");
    }
    
    @Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	@Override
	public int getRenderType()
	{
		return ClientProxy.furnaceDummyRenderType;
	}

	@Override
	public boolean canRenderInPass(int pass)
	{
		//Set the static var in the client proxy
		ClientProxy.renderPass = pass;
		//the block can render in both passes, so return true always
		return true;
	}
	@Override
	public int getRenderBlockPass()
	{
		return 1;
	}

}