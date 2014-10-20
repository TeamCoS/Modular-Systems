package com.teamcos.modularsystems.furnace.blocks;

import com.teamcos.modularsystems.utilities.block.DummyBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockFurnaceAddition extends DummyBlock {
    public BlockFurnaceAddition() {
        super(Material.rock, true);
        
        setBlockName("modularsystems:blockFurnaceAddition");
        setStepSound(Block.soundTypeStone);
        setHardness(3.5f);  
    }
    public int meta = 0;
   
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon("furnace_side");
    }    
}
