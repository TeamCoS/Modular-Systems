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

/**
 * The registry for all fluid burn times
 */
public class FluidFuelValues {
    public static FluidFuelValues INSTANCE = new FluidFuelValues();

    protected static HashMap<String, Integer> values;

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
        values.put(FluidRegistry.LAVA.getName(), 1600);
        values.put("oil", 200);
        values.put("fuel", 2200);
        values.put("rocket_fuel", 60);
        values.put("fire_water", 80);
        values.put("bioethanol", 2000);
        values.put("biofuel", 2000);
        values.put("redstone", 800);
        values.put("glowstone", 1000);
        values.put("ender", 1200);
        values.put("pyrotheum", 1200);
        saveToFile();
    }

    /**
     * Make sure the list exists
     */
    private void validateList() {
        if(values == null)
            values = new LinkedHashMap<>();
    }

    /**
     * Add a value to a fluid
     * @param name The name of the fluid
     * @param value The new burn time
     */
    public void addFluidFuel(String name, int value) {
        validateList();
        values.put(name, value);
    }

    /**
     * Get the fluid fuel value
     * @param name The name of the fluid
     * @return The burn time
     */
    public int getFluidFuelValue(String name) {
        if (values.containsKey(name)) {
            return values.get(name);
        }
        return 0;
    }

    /**
     * Checks if we have something for this fluid
     * @param name The name of the fluid
     * @return True if we have something for it
     */
    public static boolean isFluidFuel(String name) {
        return values.containsKey(name);
    }

    @SubscribeEvent
    public void onWorldSaveEvent(WorldEvent.Save event) {
        saveToFile();
    }
}
