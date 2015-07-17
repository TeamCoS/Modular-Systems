package com.pauljoda.modularsystems.core.managers;

import com.pauljoda.modularsystems.core.blocks.BlockDummyIO;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.tiles.DummyIO;
import com.pauljoda.modularsystems.core.tiles.DummyTile;
import com.pauljoda.modularsystems.crusher.blocks.BlockCrusherCore;
import com.pauljoda.modularsystems.crusher.tiles.TileCrusherCore;
import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceCore;
import com.pauljoda.modularsystems.core.blocks.BlockDummy;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import com.pauljoda.modularsystems.power.blocks.BlockPower;
import com.pauljoda.modularsystems.power.tiles.TileIC2Bank;
import com.pauljoda.modularsystems.power.tiles.TileLiquidsBank;
import com.pauljoda.modularsystems.power.tiles.TileRFBank;
import com.pauljoda.modularsystems.power.tiles.TileSolidsBank;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;

public class BlockManager {

    public static Block furnaceCore, furnaceCoreActive, crusherCore, crusherCoreActive;
    public static Block dummy, io, powerRF, powerSolids, powerLiquids, powerIC2;

    public static void init() {
        registerBlock(furnaceCore = new BlockFurnaceCore(false), "furnaceCore", TileEntityFurnaceCore.class);
        registerBlock(furnaceCoreActive = new BlockFurnaceCore(true), "furnaceCoreActive", TileEntityFurnaceCore.class);
        registerBlock(crusherCore = new BlockCrusherCore(false), "crusherCore", TileCrusherCore.class);
        registerBlock(crusherCoreActive = new BlockCrusherCore(true), "crusherCoreActive", TileCrusherCore.class);
        registerBlock(dummy = new BlockDummy(Material.rock, Reference.MOD_ID + ":dummy", DummyTile.class), "dummy", DummyTile.class);
        registerBlock(io = new BlockDummyIO(Material.rock, Reference.MOD_ID + ":dummyIO", DummyIO.class), "dummyIO", DummyIO.class);
        registerBlock(powerRF = new BlockPower(Reference.MOD_ID + ":powerRF", TileRFBank.class), "powerRF", TileRFBank.class);
        registerBlock(powerSolids = new BlockPower(Reference.MOD_ID + ":powerSolids", TileSolidsBank.class), "powerSolids", TileSolidsBank.class);
        registerBlock(powerLiquids = new BlockPower(Reference.MOD_ID + ":powerLiquids", TileLiquidsBank.class), "powerLiquids", TileLiquidsBank.class);
        if (Loader.isModLoaded("IC2"))
            registerBlock(powerIC2 = new BlockPower(Reference.MOD_ID + ":powerIC2", TileIC2Bank.class), "powerIC2", TileIC2Bank.class);
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
