package com.teamcos.modularsystems.oreprocessing.blocks;


import com.teamcos.modularsystems.core.proxy.ClientProxy;
import com.teamcos.modularsystems.utilities.block.DummyBlock;
import net.minecraft.block.material.Material;

public class BlockSmelteryDummy extends DummyBlock
{

    public BlockSmelteryDummy()
    {
        super(Material.rock, false);
    }

    @Override
    public int getRenderType()
    {
        return ClientProxy.smelteryDummyRenderType;
    }
}
