package com.pauljoda.modularsystems.core.managers;

import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceCore;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;

public class BlockManager {

    public static BlockFurnaceCore furnaceCore, furnaceCoreActive;

    public static void init() {
        registerBlock(furnaceCore = new BlockFurnaceCore(false), Reference.MOD_ID + ":furnaceCore", TileEntityFurnaceCore.class);
        registerBlock(furnaceCoreActive = new BlockFurnaceCore(true), Reference.MOD_ID + ":furnaceCoreActive", TileEntityFurnaceCore.class);
    }

    public static void registerBlock(Block block, String name, Class<? extends TileEntity> tileEntity, String oreDict) {
        GameRegistry.registerBlock(block, name);
        if(tileEntity != null)
            GameRegistry.registerTileEntity(tileEntity, name);
        if(oreDict != null)
            OreDictionary.registerOre(oreDict, block);
    }

    private static void registerBlock(Block block, String name, Class<? extends TileEntity> tileEntity) {
        registerBlock(block, name, tileEntity, null);
    }
}
