package com.pauljoda.modularsystems.core.registries;

import com.teambr.bookshelf.helpers.BlockHelper;
import com.teambr.bookshelf.helpers.LogHelper;
import com.teambr.bookshelf.util.JsonUtils;
import com.google.gson.reflect.TypeToken;
import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.calculations.Calculation;
import com.pauljoda.modularsystems.core.collections.BlockValues;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.WorldEvent;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class BlockValueRegistry {
    public static BlockValueRegistry INSTANCE = new BlockValueRegistry();

    protected HashMap<String, BlockValues> values;

    public BlockValueRegistry() {
        values = new LinkedHashMap<>();
    }

    /**
     * Add the values
     */
    public void init() {
        if(!loadFromFile())
            generateDefaults();
        else
            LogHelper.info("Block Values loaded successfully");
    }

    /**
     * Load the values from the file
     * @return True if successful
     */
    public boolean loadFromFile() {
        LogHelper.info("Loading Block Values...");
        values = JsonUtils.<LinkedHashMap<String, BlockValues>>readFromJson(new TypeToken<LinkedHashMap<String, BlockValues>>() {
        }, ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "blockValues.json");
        return values != null;
    }

    /**
     * Save the current registry to a file
     */
    public void saveToFile() {
        validateList();
        if(!values.isEmpty())
            JsonUtils.writeToJson(values, ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "blockValues.json");
    }

    /**
     * Used to generate the default values
     */
    public void generateDefaults() {
        validateList();
        addBlockValues(Blocks.redstone_block, -1,
                new Calculation(-1, 50, 0, 2, 0, -50, 0),
                new Calculation(-1, 50, 0, 2, 0, -50, 0),
                new Calculation(1, 1, 0, 0, 0, 0, 0));
        addBlockValues(Blocks.quartz_block, -1,
                new Calculation(1, 1, 0, 1, 0, 0, 0),
                new Calculation(1, 1, 0, 1, 0, 0, 0),
                new Calculation(1, 1, 0, 1, 0, 1, 3));
        addBlockValues(Blocks.gold_block, -1,
                new Calculation(1, 1, 0, 0, 0, 0, 0),
                new Calculation(8, 1, 0, 1, 0, 0, 50),
                new Calculation(1, 1, 0, 0, 0, 0, 0));
        addBlockValues(Blocks.diamond_block, -1,
                new Calculation(-8, 1, 0, 1, 0, -40, 0),
                new Calculation(10, 1, 0, 1, 0, 0, 100),
                new Calculation(1, 1, 0, 0, 0, 0, 0));
        addBlockValues(Blocks.lapis_block, -1,
                new Calculation(-5, 1, 0, 1, 0, -25, 0),
                new Calculation(5, 1, 0, 1, 0, 0, 50),
                new Calculation(1, 1, 0, 0, 0, 0, 0));
        addBlockValues(Blocks.brick_block, -1,
                new Calculation(10, 1, 0, 1, 0, 0, 50),
                new Calculation(-10, 1, 0, 1, 0, -50, 0),
                new Calculation(1, 1, 0, 1, 0, 0, 5));
    }

    public void addBlockValues(Block block, int meta, Calculation speed, Calculation efficiency, Calculation multiplicity) {
        validateList();
        if(!isBlockRegistered(block, meta)) {
            values.put(BlockHelper.getBlockString(block, meta), new BlockValues(speed, efficiency, multiplicity));
        } else {
            LogHelper.info("Someone attempted to add a value to a block that already has a value. Replacing...");
            values.get(BlockHelper.getBlockString(block, meta)).setSpeedFunction(speed);
            values.get(BlockHelper.getBlockString(block, meta)).setEfficiencyFunction(efficiency);
            values.get(BlockHelper.getBlockString(block, meta)).setMultiplicityFunction(multiplicity);
        }
    }

    /**
     * Is this block registered in the registry
     * @param block Block to check
     * @return True if found
     */
    public boolean isBlockRegistered(Block block, int meta) {
        return values.get(BlockHelper.getBlockString(block, meta)) != null || values.get(BlockHelper.getBlockString(block)) != null;
    }

    public double getSpeedValue(Block block, int meta, int x) {
        if(isBlockRegistered(block, meta)) {
            BlockValues blockValues = values.get(BlockHelper.getBlockString(block)) != null ? values.get(BlockHelper.getBlockString(block)) : values.get(BlockHelper.getBlockString(block, meta));
            return blockValues.getSpeedFunction().F(x);
        } else
            return 0;
    }

    public double getEfficiencyValue(Block block, int meta, int x) {
        if(isBlockRegistered(block, meta)) {
            BlockValues blockValues = values.get(BlockHelper.getBlockString(block)) != null ? values.get(BlockHelper.getBlockString(block)) : values.get(BlockHelper.getBlockString(block, meta));
            return blockValues.getEfficiencyFunction().F(x);
        } else
            return 0;
    }

    public double getMultiplicityValue(Block block, int meta, int x) {
        if(isBlockRegistered(block, meta)) {
            BlockValues blockValues = values.get(BlockHelper.getBlockString(block)) != null ? values.get(BlockHelper.getBlockString(block)) : values.get(BlockHelper.getBlockString(block, meta));
            return blockValues.getMultiplicityFunction().F(x);
        } else
            return 0;
    }

    /**
     * Make sure the list exists
     */
    private void validateList() {
        if(values == null)
            values = new LinkedHashMap<>();
    }

    @SubscribeEvent
    public void onWorldSaveEvent(WorldEvent.Save event) {
        saveToFile();
    }
}
