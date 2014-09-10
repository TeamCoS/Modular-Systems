package com.pauljoda.modularsystems.furnace.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.pauljoda.modularsystems.ModularSystems;
import com.pauljoda.modularsystems.proxy.ClientProxy;

public class BlockFurnaceAddition extends Block {

    public BlockFurnaceAddition() {
        super(Material.rock);
        
        setBlockName("modularsystems:blockFurnaceAdditionInactive");
        setStepSound(Block.soundTypeStone);
        setHardness(3.5f);
        setCreativeTab(ModularSystems.tabModularSystems);
    }
    
    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("furnace_side");
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