package com.teamcos.modularsystems.furnace;

import com.teamcos.modularsystems.furnace.config.BlockConfig;
import net.minecraft.block.material.Material;

import java.util.LinkedHashMap;
import java.util.Map;

public final class FurnaceConfigHandler {

    private static Map<String, BlockConfig> blockConfigMap = new LinkedHashMap<String, BlockConfig>();
    private static Map<Material, BlockConfig> materialConfigMap = new LinkedHashMap<Material, BlockConfig>();

    private FurnaceConfigHandler() {}

    public static void publishBlockConfig(String blockName, BlockConfig config) {
        blockConfigMap.put(blockName, config);
    }

    public static BlockConfig retrieveBlockConfig(String blockName) {
        return blockConfigMap.get(blockName);
    }

    public static void publishMaterialConfig(Material blockName, BlockConfig config) {
        materialConfigMap.put(blockName, config);
    }

    public static BlockConfig retrieveMaterialConfig(Material material) {
        return materialConfigMap.get(material);
    }
}
