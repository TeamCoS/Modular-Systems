package com.pauljoda.modularsystems.furnace.blocks;

import com.dyonovan.brlib.common.blocks.BaseBlock;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceDummy;
import net.minecraft.block.material.Material;

public class FurnaceDummy extends BaseBlock {
    public FurnaceDummy() {
        super(Material.rock, Reference.MOD_ID + ":furnaceDummy", TileEntityFurnaceDummy.class);
    }
}
