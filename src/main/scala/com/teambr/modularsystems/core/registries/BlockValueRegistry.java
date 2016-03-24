package com.teambr.modularsystems.core.registries;

import com.google.gson.reflect.TypeToken;
import com.teambr.bookshelf.helper.BlockHelper;
import com.teambr.bookshelf.helper.LogHelper;
import com.teambr.bookshelf.util.JsonUtils;
import com.teambr.modularsystems.core.ModularSystems;
import com.teambr.modularsystems.core.collections.BlockValues;
import com.teambr.modularsystems.core.collections.Calculation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since August 05, 2015
 */
public class BlockValueRegistry {
    public static BlockValueRegistry INSTANCE = new BlockValueRegistry();

    public HashMap<String, BlockValues> values; //The block values
    public HashMap<String, BlockValues> materialValues; //The material values

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
        }, ModularSystems.configFolderLocation() + File.separator + "Registries" + File.separator + "blockValues.json");
        materialValues = JsonUtils.<LinkedHashMap<String, BlockValues>>readFromJson(new TypeToken<LinkedHashMap<String, BlockValues>>() {
        }, ModularSystems.configFolderLocation() + File.separator + "Registries" + File.separator + "materialValues.json");
        return values != null;
    }

    /**
      * Save the current registry to a file
      */
    public void saveToFile() {
        validateList();
        if(!values.isEmpty())
            JsonUtils.writeToJson(values, ModularSystems.configFolderLocation() + File.separator + "Registries" + File.separator + "blockValues.json");
        if(!materialValues.isEmpty())
            JsonUtils.writeToJson(materialValues, ModularSystems.configFolderLocation() + File.separator + "Registries" + File.separator + "materialValues.json");
    }

    /**
      * Used to generate the default values
      */
    public void generateDefaults() {
        validateList();
        //Move file and load it
        File file = new File(ModularSystems.configFolderLocation() + File.separator + "Registries" + File.separator + "blockValues.json");
        if (!file.exists()) {
            URL fileURL = ModularSystems.class.getResource("/blockValues.json");
            try {
                FileUtils.copyURLToFile(fileURL, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        values = JsonUtils.<LinkedHashMap<String, BlockValues>>readFromJson(new TypeToken<LinkedHashMap<String, BlockValues>>() {
        }, ModularSystems.configFolderLocation() + File.separator + "Registries" + File.separator + "blockValues.json");

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
      * Removes the values for a given block
      * @param block The block to delete
      * @param meta The meta to delete
      */
    public void deleteBlockValues(Block block, int meta) {
        validateList();
        if((values.get(BlockHelper.getBlockString(block)) != null ? values.get(BlockHelper.getBlockString(block)) : values.get(BlockHelper.getBlockString(block, meta))) != null) {
            LogHelper.info("Deleting values...");
            values.remove(values.get(BlockHelper.getBlockString(block)) != null ? BlockHelper.getBlockString(block) : BlockHelper.getBlockString(block, meta));
            LogHelper.info("Values deleted");
        }
        else {
            LogHelper.warning("You can't delete something that does not exist");
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

    /**
      * Get the speed value for the block
      * @param block The block
      * @param meta The meta of the block
      * @param x How many are in the function
      * @return What value the equation returns
      */
    public double getSpeedValue(Block block, int meta, int x) {
        if(isBlockRegistered(block, meta)) {
            BlockValues blockValues = values.get(BlockHelper.getBlockString(block)) != null ? values.get(BlockHelper.getBlockString(block)) : values.get(BlockHelper.getBlockString(block, meta));
            return blockValues.getSpeedFunction().F(x);
        } else
            return 0;
    }

    /**
      * Get the efficiency value for the block
      * @param block The block
      * @param meta The meta of the block
      * @param x How many are in the function
      * @return What value the equation returns
      */
    public double getEfficiencyValue(Block block, int meta, int x) {
        if(isBlockRegistered(block, meta)) {
            BlockValues blockValues = values.get(BlockHelper.getBlockString(block)) != null ? values.get(BlockHelper.getBlockString(block)) : values.get(BlockHelper.getBlockString(block, meta));
            return blockValues.getEfficiencyFunction().F(x);
        } else
            return 0;
    }

    /**
      * Get the multiplicity value for the block
      * @param block The block
      * @param meta The meta of the block
      * @param x How many are in the function
      * @return What value the equation returns
      */
    public double getMultiplicityValue(Block block, int meta, int x) {
        if(isBlockRegistered(block, meta)) {
            BlockValues blockValues = values.get(BlockHelper.getBlockString(block)) != null ? values.get(BlockHelper.getBlockString(block)) : values.get(BlockHelper.getBlockString(block, meta));
            return blockValues.getMultiplicityFunction().F(x);
        } else
            return 0;
    }

    /**
      * Get the speed value for the material
      * @param mat The string value of the material
      * @param x How many are in the function
      * @return What value the equation returns
      */
    public double getSpeedValueMaterial(String mat, int x) {
        if(isMaterialRegistered(mat)) {
            BlockValues blockValues = materialValues.get(mat);
            return blockValues.getSpeedFunction().F(x);
        } else
            return 0;
    }

    /**
      * Get the efficiency value for the material
      * @param mat The string value of the material
      * @param x How many are in the function
      * @return What value the equation returns
      */
    public double getEfficiencyValueMaterial(String mat, int x) {
        if(isMaterialRegistered(mat)) {
            BlockValues blockValues = materialValues.get(mat);
            return blockValues.getEfficiencyFunction().F(x);
        } else
            return 0;
    }

    /**
      * Get the multiplicity value for the material
      * @param mat The string value of the material
      * @param x How many are in the function
      * @return What value the equation returns
      */
    public double getMultiplicityValueMaterial(String mat, int x) {
        if(isMaterialRegistered(mat)) {
            BlockValues blockValues = materialValues.get(mat);
            return blockValues.getMultiplicityFunction().F(x);
        } else
            return 0;
    }

    /**
      * Get the values associated with a block
      * @param block The block
      * @param meta The metadata
      * @return The {@link BlockValues} for this block
      */
    public BlockValues getBlockValues(Block block, int meta) {
        if(isBlockRegistered(block, meta)) {
            return values.get(BlockHelper.getBlockString(block)) != null ? values.get(BlockHelper.getBlockString(block)) : values.get(BlockHelper.getBlockString(block, meta));
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

    /**
      * Save stuff for later
      */
    @SubscribeEvent
    public void onWorldSaveEvent(WorldEvent.Save event) {
        saveToFile();
    }
}