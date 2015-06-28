package com.pauljoda.modularsystems.furnace.blocks;

import com.pauljoda.modularsystems.core.blocks.BaseBlock;
import com.pauljoda.modularsystems.core.blocks.rotation.FourWayRotation;
import com.pauljoda.modularsystems.core.blocks.rotation.IRotation;
import net.minecraft.block.material.Material;

public class BlockFurnaceCore extends BaseBlock {

    protected BlockFurnaceCore() {
        super(Material.rock, "furnaceCore", null);
    }

    @Override
    protected IRotation getDefaultRotation() {
        return new FourWayRotation();
    }
}
