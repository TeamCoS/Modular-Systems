package com.teamcos.modularsystems.oreprocessing.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockSmelteryOverlay extends Block
{
    public BlockSmelteryOverlay()
    {
        super(Material.cloth);
        this.setBlockName("modularsystems:smelteryOverlay");
    }
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("modularsystems:smelteryOverlay");
    }
}
