package com.pauljoda.modularsystems.furnace;

import com.pauljoda.modularsystems.furnace.config.BlockConfig;

import java.util.LinkedHashMap;
import java.util.Map;

public final class FurnaceConfigHandler {

    private static Map<String, BlockConfig> blockConfigMap = new LinkedHashMap<String, BlockConfig>();
    private static Map<String, BlockConfig> materialConfigMap = new LinkedHashMap<String, BlockConfig>();

    private FurnaceConfigHandler() {}

    public static void publishBlockConfig(String blockName, BlockConfig config) {
        blockConfigMap.put(blockName, config);
    }

    public static BlockConfig retrieveBlockConfig(String blockName) {
        return blockConfigMap.get(blockName);
    }

    public static void publishMaterialConfig(String blockName, BlockConfig config) {
        materialConfigMap.put(blockName, config);
    }

    public static BlockConfig retrieveMaterialConfig(String blockName) {
        return materialConfigMap.get(blockName);
    }
}
