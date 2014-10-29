package com.teamcos.modularsystems.furnace.blocks;

import com.teamcos.modularsystems.core.ModularSystems;
import com.teamcos.modularsystems.core.proxy.ClientProxy;
import com.teamcos.modularsystems.utilities.block.DummyBlock;
import com.teamcos.modularsystems.utilities.tiles.DummyTile;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCrafter extends DummyBlock {
    public BlockCrafter() {
        super(ModularSystems.tabModularSystems, Material.wood, true);
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

    @Override
    public boolean isCrafter() {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        DummyTile dummy = new DummyTile();
        dummy.setBlock(getIdFromBlock(this));
        return dummy;
    }
}
