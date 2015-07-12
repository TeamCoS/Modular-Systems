package com.pauljoda.modularsystems.core.registries;

import com.google.gson.reflect.TypeToken;
import com.pauljoda.modularsystems.core.ModularSystems;
import com.teambr.bookshelf.helpers.LogHelper;
import com.teambr.bookshelf.util.JsonUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.FluidRegistry;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class FluidFuelValues {
    public static FluidFuelValues INSTANCE = new FluidFuelValues();

    protected HashMap<String, Integer> values;

    public FluidFuelValues() {
        values = new LinkedHashMap<>();
    }

    /**
     * Add the values
     */
    public void init() {
        if(!loadFromFile())
            generateDefaults();
        else
            LogHelper.info("Fluid Fuel Values loaded successfully");
    }

    /**
     * Load the values from the file
     * @return True if successful
     */
    public boolean loadFromFile() {
        LogHelper.info("Loading Fluid Fuel Values...");
        values = JsonUtils.<LinkedHashMap<String, Integer>>readFromJson(new TypeToken<LinkedHashMap<String, Integer>>() {
        }, ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "fluidFuelValues.json");
        return values != null;
    }

    /**
     * Save the current registry to a file
     */
    public void saveToFile() {
        validateList();
        if(!values.isEmpty())
            JsonUtils.writeToJson(values, ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "fluidFuelValues.json");
    }

    /**
     * Used to generate the default values
     */
    public void generateDefaults() {
        validateList();
        values.put(FluidRegistry.LAVA.getName(), 16000);
        values.put("oil", 2000);
        values.put("fuel", 22000);
        values.put("rocket_fuel", 600);
        values.put("fire_water", 800);
        values.put("bioethanol", 20000);
        values.put("biofuel", 20000);
        values.put("redstone", 8000);
        values.put("glowstone", 10000);
        values.put("ender", 12000);
        values.put("pyrotheum", 12000);
        saveToFile();
    }

    /**
     * Make sure the list exists
     */
    private void validateList() {
        if(values == null)
            values = new LinkedHashMap<>();
    }

    public void addFluidFuel(String name, int value) {
        validateList();
        values.put(name, value);
    }

    @SubscribeEvent
    public void onWorldSaveEvent(WorldEvent.Save event) {
        saveToFile();
    }
}
