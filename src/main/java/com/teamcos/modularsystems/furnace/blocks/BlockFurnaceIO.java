package com.teamcos.modularsystems.furnace.blocks;

import com.teamcos.modularsystems.core.proxy.ClientProxy;
import com.teamcos.modularsystems.utilities.block.DummyIOBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockFurnaceIO extends DummyIOBlock
{
    public BlockFurnaceIO()
    {
        super(Material.rock, true);
        setBlockName("modularsystems:blockFurnaceIO");
    }

    @Override
    public int getRenderType()
    {
        return ClientProxy.furnaceDummyRenderType;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("dispenser_front_vertical");
    }
}
