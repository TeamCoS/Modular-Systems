package com.pauljoda.modularsystems.furnace.blocks;

import com.dyonovan.brlib.collections.BlockTextures;
import com.dyonovan.brlib.common.blocks.BaseBlock;
import com.dyonovan.brlib.common.blocks.rotation.FourWayRotation;
import com.dyonovan.brlib.common.blocks.rotation.IRotation;
import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.lib.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;

public class BlockFurnaceCore extends BaseBlock {

    public BlockFurnaceCore() {
        super(Material.rock, Reference.MOD_ID + ":furnaceCore", null);
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

    @Override
    public CreativeTabs getCreativeTab() {
        return ModularSystems.tabModularSystems;
    }
}
