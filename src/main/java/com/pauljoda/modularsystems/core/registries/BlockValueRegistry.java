package com.pauljoda.modularsystems.core.registries;

import com.google.gson.reflect.TypeToken;
import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.calculations.Calculation;
import com.pauljoda.modularsystems.core.collections.BlockValues;
import com.teambr.bookshelf.helpers.BlockHelper;
import com.teambr.bookshelf.helpers.LogHelper;
import com.teambr.bookshelf.util.JsonUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.world.WorldEvent;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class BlockValueRegistry {
    public static BlockValueRegistry INSTANCE = new BlockValueRegistry();

    public HashMap<String, BlockValues> values;
    public HashMap<String, BlockValues> materialValues;

    public BlockValueRegistry() {
        values = new LinkedHashMap<>();
        materialValues = new LinkedHashMap<>();
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
        materialValues = JsonUtils.<LinkedHashMap<String, BlockValues>>readFromJson(new TypeToken<LinkedHashMap<String, BlockValues>>() {
        }, ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "materialValues.json");
        return values != null;
    }

    /**
     * Save the current registry to a file
     */
    public void saveToFile() {
        validateList();
        if(!values.isEmpty())
            JsonUtils.writeToJson(values, ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "blockValues.json");
        if(!materialValues.isEmpty())
            JsonUtils.writeToJson(materialValues, ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "materialValues.json");
    }

    /**
     * Used to generate the default values
     */
    public void generateDefaults() {
        validateList();
        //TODO: Move file and load it

        addMaterialValues(Material.rock,
                new Calculation(-1, 200, 0, 1, 0, -100, 0),
                new Calculation(1, 100, 0, 1, 0, 0, 450),
                new Calculation(1, 1, 0, 1, 0, 0, 0));
        addMaterialValues(Material.iron,
                new Calculation(1, 100, 0, 1, 0, 100, 0),
                new Calculation(10, 1, 0, 1, 0, 0, 1600),
                new Calculation(1, 1, 0, 1, 0, 0, 0));
    }

    /**
     * Add a value to a block
     * @param block The block to map
     * @param meta The meta of that block
     * @param speed Speed Function
     * @param efficiency Efficiency Function
     * @param multiplicity Multiplicity Function
     */
    public void addBlockValues(Block block, int meta, Calculation speed, Calculation efficiency, Calculation multiplicity) {
        validateList();
        if(!isBlockRegistered(block, meta)) {
            values.put(BlockHelper.getBlockString(block, meta), new BlockValues(speed, efficiency, multiplicity));
        } else {
            LogHelper.info("Someone attempted to add a value to a blocks that already has a value. Replacing...");
            String id = values.get(BlockHelper.getBlockString(block, meta)) != null ? BlockHelper.getBlockString(block, meta) : BlockHelper.getBlockString(block);
            values.get(id).setSpeedFunction(speed);
            values.get(id).setEfficiencyFunction(efficiency);
            values.get(id).setMultiplicityFunction(multiplicity);
            LogHelper.info("Replaced");
        }
    }

    /**
     * Used to add a material value
     * @param material The material to map
     * @param speed Speed function
     * @param efficiency Efficiency function
     * @param multiplicity Multiplicity function
     */
    public void addMaterialValues(Material material, Calculation speed, Calculation efficiency, Calculation multiplicity) {
        validateList();
        if(!isMaterialRegistered(material)){
            materialValues.put(getMaterialString(material), new BlockValues(speed, efficiency, multiplicity));
        } else {
            LogHelper.info("Someone attempted to add a value to a material that already has a value. Replacing...");
            materialValues.get(getMaterialString(material)).setSpeedFunction(speed);
            materialValues.get(getMaterialString(material)).setEfficiencyFunction(efficiency);
            materialValues.get(getMaterialString(material)).setMultiplicityFunction(multiplicity);
            LogHelper.info("Replaced");
        }
    }

    /**
     * Is this blocks registered in the registry
     * @param block Block to check
     * @return True if found
     */
    public boolean isBlockRegistered(Block block, int meta) {
        return values.get(BlockHelper.getBlockString(block, meta)) != null || values.get(BlockHelper.getBlockString(block)) != null;
    }

    /**
     * Used to check if the material has been registered
     * @param material The material to check
     * @return True if mapped
     */
    public boolean isMaterialRegistered(Material material) {
        return materialValues.get(getMaterialString(material)) != null;
    }

    /**
     * Used to check if the material has been registered
     * @param material The material to check
     * @return True if mapped
     */
    public boolean isMaterialRegistered(String material) {
        return materialValues.get(material) != null;
    }

    /**
     * Converts the material to a string. Would be nice if Minecraft already had something like this
     * @param material Material to name
     * @return The string representation of this material
     */
    public static String getMaterialString(Material material) {
        if(material == Material.air)
            return "air";
        else if(material == Material.grass)
            return "grass";
        else if(material == Material.ground)
            return "ground";
        else if(material == Material.wood)
            return "wood";
        else if(material == Material.rock)
            return "rock";
        else if(material == Material.iron)
            return "iron";
        else if(material == Material.piston)
            return "piston";
        else if(material == Material.web)
            return "web";
        else if(material == Material.cake)
            return "cake";
        else if(material == Material.portal)
            return "portal";
        else if(material == Material.dragonEgg)
            return "dragonEgg";
        else if(material == Material.gourd)
            return "gourd";
        else if(material == Material.clay)
            return "clay";
        else if(material == Material.cactus)
            return "cactus";
        else if(material == Material.craftedSnow)
            return "craftedSnow";
        return "NULL";
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

    public double getSpeedValueMaterial(String mat, int x) {
        if(isMaterialRegistered(mat)) {
            BlockValues blockValues = materialValues.get(mat);
            return blockValues.getSpeedFunction().F(x);
        } else
            return 0;
    }

    public double getEfficiencyValueMaterial(String mat, int x) {
        if(isMaterialRegistered(mat)) {
            BlockValues blockValues = materialValues.get(mat);
            return blockValues.getEfficiencyFunction().F(x);
        } else
            return 0;
    }

    public double getMultiplicityValueMaterial(String mat, int x) {
        if(isMaterialRegistered(mat)) {
            BlockValues blockValues = materialValues.get(mat);
            return blockValues.getMultiplicityFunction().F(x);
        } else
            return 0;
    }

    public BlockValues getBlockValues(Block block, int meta) {
        if(isBlockRegistered(block, meta)) {
            BlockValues blockValues = values.get(BlockHelper.getBlockString(block)) != null ? values.get(BlockHelper.getBlockString(block)) : values.get(BlockHelper.getBlockString(block, meta));
            return blockValues;
        } else
            return null;
    }

    /**
     * Make sure the list exists
     */
    private void validateList() {
        if(values == null)
            values = new LinkedHashMap<>();
        if(materialValues == null)
            materialValues = new LinkedHashMap<>();
    }

    @SubscribeEvent
    public void onWorldSaveEvent(WorldEvent.Save event) {
        saveToFile();
    }
}
