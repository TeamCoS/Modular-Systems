package com.teamcos.modularsystems.registries;

import com.teamcos.modularsystems.furnace.config.BlockConfig;
import net.minecraft.block.Block;
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

    //FURNACE: Gets Speed Multiplier
    public static double getSpeedMultiplierForBlock(Block block, int count) {
        BlockConfig blockValue = retrieveBlockConfig(block.getUnlocalizedName());
        BlockConfig materialValue = retrieveMaterialConfig(block.getMaterial());
        if (blockValue != null) {
            return blockValue.speed(count);
        } else if (materialValue != null) {
            return materialValue.speed(count);
        } else {
            return 0;
        }
    }

    //FURNACE: Gets Efficiency Multiplier
    public static double getEfficiencyMultiplierForBlock(Block block, int count) {
        BlockConfig blockValue = retrieveMaterialConfig(block.getMaterial());
        BlockConfig materialValue = retrieveBlockConfig(block.getUnlocalizedName());
        if (blockValue != null) {
            return blockValue.efficiency(count);
        } else if (materialValue != null) {
            return materialValue.efficiency(count);
        } else {
            return 0.0;
        }
    }

    //FURNACE: Gets Efficiency Multiplier
    public static int getSmeltingMultiplierForBlock(Block block, int count) {
        BlockConfig blockValue = FurnaceConfigHandler.retrieveMaterialConfig(block.getMaterial());
        BlockConfig materialValue = FurnaceConfigHandler.retrieveBlockConfig(block.getUnlocalizedName());
        if (blockValue != null) {
            return blockValue.multiplier(count);
        } else if (materialValue != null) {
            return materialValue.multiplier(count);
        } else {
            return 0;
        }
    }
}
