package com.pauljoda.modularsystems.crusher.blocks;

import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.crusher.tiles.TileCrusherCore;
import com.teambr.bookshelf.common.blocks.BaseBlock;
import net.minecraft.block.material.Material;

public class BlockCrusherCore extends BaseBlock {

    private final boolean active;

    public BlockCrusherCore(boolean isActive) {
        super(Material.rock, Reference.MOD_ID + ":crusherCore" + (isActive ? "Active" : ""), TileCrusherCore.class);
        active = isActive;
    }
}
