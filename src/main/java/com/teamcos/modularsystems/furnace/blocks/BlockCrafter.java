package com.teamcos.modularsystems.furnace.blocks;

import com.teamcos.modularsystems.core.proxy.ClientProxy;
import com.teamcos.modularsystems.utilities.block.DummyBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockCrafter extends DummyBlock {
    public BlockCrafter() {
        super(Material.wood, true);
        setBlockName("modularsystems:blockFurnaceCraftingUpgrade");
        setStepSound(Block.soundTypeWood);
        setHardness(3.5f);
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon("crafting_table_top");
    }

	@Override
	public int getRenderType() {
		return ClientProxy.msRenderId;
	}
}
