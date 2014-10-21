package com.teamcos.modularsystems.furnace.blocks;


import com.teamcos.modularsystems.core.proxy.ClientProxy;
import com.teamcos.modularsystems.utilities.block.DummyBlock;
import net.minecraft.block.material.Material;

public class BlockFurnaceDummy extends DummyBlock
{

    public BlockFurnaceDummy()
    {
        super(Material.rock, false);
    }

    @Override
    public int getRenderType()
    {
        return ClientProxy.furnaceDummyRenderType;
    }
}
