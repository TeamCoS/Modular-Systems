package com.teamcos.modularsystems.oreprocessing.blocks;

import com.teamcos.modularsystems.core.proxy.ClientProxy;
import com.teamcos.modularsystems.utilities.block.DummyIOBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockSmelteryIO extends DummyIOBlock
{
    public BlockSmelteryIO()
    {
        super(Material.rock, true);
        setBlockName("modularsystems:blockSmelteryIO");
    }

    @Override
    public int getRenderType()
    {
        return ClientProxy.smelteryDummyRenderType;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("dispenser_front_vertical");
    }
}
