package com.pauljoda.modularsystems.furnace.blocks;

import com.pauljoda.modularsystems.core.blocks.BaseBlock;
import com.pauljoda.modularsystems.core.blocks.rotation.FourWayRotation;
import com.pauljoda.modularsystems.core.blocks.rotation.IRotation;
import com.pauljoda.modularsystems.core.collections.BlockTextures;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockFurnaceCore extends BaseBlock {

    public BlockFurnaceCore() {
        super(Material.rock, "furnaceCore", null);
    }

    @Override
    public IRotation getDefaultRotation() {
        return new FourWayRotation();
    }

    @Override
    public void generateDefaultTextures(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("minecraft:furnace_side");
        textures = new BlockTextures(iconRegister, "minecraft:furnace_side");
        textures.setFront(iconRegister.registerIcon("minecraft:furnace_front_off"));
        textures.setTop(iconRegister.registerIcon("minecraft:furnace_top"));
        textures.setBottom(iconRegister.registerIcon("minecraft:furnace_top"));
    }
}
