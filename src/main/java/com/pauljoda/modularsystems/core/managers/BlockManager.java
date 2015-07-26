package com.pauljoda.modularsystems.core.managers;

import com.pauljoda.modularsystems.core.blocks.BlockDummyIO;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.registries.ConfigRegistry;
import com.pauljoda.modularsystems.core.tiles.DummyIO;
import com.pauljoda.modularsystems.core.tiles.DummyTile;
import com.pauljoda.modularsystems.crusher.blocks.BlockCrusherCore;
import com.pauljoda.modularsystems.crusher.tiles.TileCrusherCore;
import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceCore;
import com.pauljoda.modularsystems.core.blocks.BlockDummy;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import com.pauljoda.modularsystems.generator.blocks.BlockGeneratorCore;
import com.pauljoda.modularsystems.generator.tiles.TileGeneratorCore;
import com.pauljoda.modularsystems.power.blocks.BlockPower;
import com.pauljoda.modularsystems.power.tiles.*;
import com.pauljoda.modularsystems.storage.blocks.BlockStorageCore;
import com.pauljoda.modularsystems.storage.blocks.BlockStorageExpansion;
import com.pauljoda.modularsystems.storage.tiles.*;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Manager for the Blocks. Used to register and control what is added
 */
public class BlockManager {

    public static Block furnaceCore, furnaceCoreActive, crusherCore, crusherCoreActive;
    public static Block generatorCore, generatorCoreActive;
    public static Block storageCore, storageBasic, storageCapacity, storageSearch, storageSort, storageSecurity;
    public static Block dummy, io, powerRF, powerSolids, powerLiquids, powerIC2, powerMana;
    public static Block supplierRF, supplierIC2;

    public static void init() {
        //Systems
        if (ConfigRegistry.furnaceCore) {
            registerBlock(furnaceCore = new BlockFurnaceCore(false), "furnaceCore", TileEntityFurnaceCore.class);
            registerBlock(furnaceCoreActive = new BlockFurnaceCore(true), "furnaceCoreActive", TileEntityFurnaceCore.class);
        }
        if (ConfigRegistry.crusherCore) {
            registerBlock(crusherCore = new BlockCrusherCore(false), "crusherCore", TileCrusherCore.class);
            registerBlock(crusherCoreActive = new BlockCrusherCore(true), "crusherCoreActive", TileCrusherCore.class);
        }
        if (ConfigRegistry.generatorCore) {
            registerBlock(generatorCore = new BlockGeneratorCore(false), "generatorCore", TileGeneratorCore.class);
            registerBlock(generatorCoreActive = new BlockGeneratorCore(true), "generatorCoreActive", TileGeneratorCore.class);
        }
        if(ConfigRegistry.storageSystem) {
            registerBlock(storageCore = new BlockStorageCore(), "storageCore", TileStorageCore.class);

            registerBlock(storageBasic = new BlockStorageExpansion(Material.wood, Reference.MOD_ID + ":storageBasic", TileStorageBasic.class), "storageBasic", TileStorageBasic.class);
            registerBlock(storageCapacity = new BlockStorageExpansion(Material.wood, Reference.MOD_ID + ":storageCapacity", TileStorageCapacity.class), "storageCapacity", TileStorageCapacity.class);
            registerBlock(storageSearch = new BlockStorageExpansion(Material.wood, Reference.MOD_ID + ":storageSearch", TileStorageSearch.class), "storageSearch", TileStorageSearch.class);
            registerBlock(storageSort = new BlockStorageExpansion(Material.wood, Reference.MOD_ID + ":storageSort", TileStorageSorting.class), "storageSort", TileStorageSorting.class);
            registerBlock(storageSecurity = new BlockStorageExpansion(Material.wood, Reference.MOD_ID + ":storageSecurity", TileStorageSecurity.class), "storageSecurity", TileStorageSecurity.class);
        }

        registerBlock(dummy = new BlockDummy(Material.rock, Reference.MOD_ID + ":dummy", DummyTile.class), "dummy", DummyTile.class);
        registerBlock(io = new BlockDummyIO(Material.rock, Reference.MOD_ID + ":dummyIO", DummyIO.class), "dummyIO", DummyIO.class);
        registerBlock(powerRF = new BlockPower(Reference.MOD_ID + ":powerRF", TileRFBank.class), "powerRF", TileRFBank.class);
        registerBlock(powerSolids = new BlockPower(Reference.MOD_ID + ":powerSolids", TileSolidsBank.class), "powerSolids", TileSolidsBank.class);
        registerBlock(powerLiquids = new BlockPower(Reference.MOD_ID + ":powerLiquids", TileLiquidsBank.class), "powerLiquids", TileLiquidsBank.class);

        if (Loader.isModLoaded("IC2"))
            registerBlock(powerIC2 = new BlockPower(Reference.MOD_ID + ":powerIC2", TileIC2Bank.class), "powerIC2", TileIC2Bank.class);
        if (Loader.isModLoaded("Botania"))
            registerBlock(powerMana = new BlockPower(Reference.MOD_ID + ":powerMana", TileManaBank.class), "powerMana", TileManaBank.class);

        registerBlock(supplierRF = new BlockPower(Reference.MOD_ID + ":supplierRF", TileRFSupplier.class), "supplierRF", TileRFSupplier.class);
        if (Loader.isModLoaded("IC2"))
            registerBlock(supplierIC2 = new BlockPower(Reference.MOD_ID + ":supplierIC2", TileIC2LVProvider.class), "supplierIC2", TileIC2LVProvider.class);
    }

    /**
     * Helper method for registering block
     * @param block The block to register
     * @param name The name to register the block to
     * @param tileEntity The tile entity, null if none
     * @param oreDict The ore dict tag, should it be needed
     */
    public static void registerBlock(Block block, String name, Class<? extends TileEntity> tileEntity, String oreDict) {
        GameRegistry.registerBlock(block, name);
        if(tileEntity != null)
            GameRegistry.registerTileEntity(tileEntity, name);
        if(oreDict != null)
            OreDictionary.registerOre(oreDict, block);
    }

    /**
     * No ore dict helper method
     * @param block The block to add
     * @param name The name
     * @param tileEntity The tile
     */
    private static void registerBlock(Block block, String name, Class<? extends TileEntity> tileEntity) {
        registerBlock(block, name, tileEntity, null);
    }

    /**
     * Used for no tile blocks with ore dict
     * @param block The block to register
     * @param name The name of the block
     * @param oreDict The ore dict tag
     */
    private static void registerBlock(Block block, String name, String oreDict) {
        registerBlock(block, name, null, oreDict);
    }

    /**
     * For those blocks with no tile/oredict
     * @param block The block to add
     * @param name The name of the block
     */
    private static void registerBlock(Block block, String name) {
        registerBlock(block, name, null, null);
    }
}
