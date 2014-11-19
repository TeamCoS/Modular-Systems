package com.teamcos.modularsystems.registries;

import com.teamcos.modularsystems.furnace.config.BlockConfig;
import com.teamcos.modularsystems.helpers.Coord;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

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
       if(blockConfigMap.containsKey(blockName))
            return blockConfigMap.get(blockName);
        else {
            String input[] = blockName.split(":");
            if(blockConfigMap.containsKey(input[0] + ":" + input[1]))
                return blockConfigMap.get(input[0] + ":" + input[1]);
            else {
                if(blockConfigMap.containsKey(input[0]))
                    return blockConfigMap.get(input[0]);
            }
        }
        return null;
    }

    public static void publishMaterialConfig(Material blockName, BlockConfig config) {
        materialConfigMap.put(blockName, config);
    }

    public static BlockConfig retrieveMaterialConfig(Material material) {
        return materialConfigMap.get(material);
    }

    //FURNACE: Gets Speed Multiplier
    public static double getSpeedMultiplierForBlock(World world, Coord loc, Block block, int count) {
        GameRegistry.UniqueIdentifier uniqueIdentifier = GameRegistry.findUniqueIdentifierFor(block);
        String blockName = uniqueIdentifier.modId + ":" + uniqueIdentifier.name + ":" + world.getBlockMetadata(loc.x, loc.y, loc.z);
        BlockConfig blockValue = retrieveBlockConfig(blockName);
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
    public static double getEfficiencyMultiplierForBlock(World world, Coord loc, Block block, int count) {
        GameRegistry.UniqueIdentifier uniqueIdentifier = GameRegistry.findUniqueIdentifierFor(block);
        String blockName = uniqueIdentifier.modId + ":" + uniqueIdentifier.name + ":" + world.getBlockMetadata(loc.x, loc.y, loc.z);
        BlockConfig blockValue = retrieveBlockConfig(blockName);
        BlockConfig materialValue = retrieveMaterialConfig(block.getMaterial());
        if (blockValue != null) {
            return blockValue.efficiency(count);
        } else if (materialValue != null) {
            return materialValue.efficiency(count);
        } else {
            return 0.0;
        }
    }

    //FURNACE: Gets Efficiency Multiplier
    public static int getSmeltingMultiplierForBlock(World world, Coord loc, Block block, int count) {
        GameRegistry.UniqueIdentifier uniqueIdentifier = GameRegistry.findUniqueIdentifierFor(block);
        String blockName = uniqueIdentifier.modId + ":" + uniqueIdentifier.name + ":" + world.getBlockMetadata(loc.x, loc.y, loc.z);
        BlockConfig blockValue = retrieveBlockConfig(blockName);
        BlockConfig materialValue = retrieveMaterialConfig(block.getMaterial());
        if (blockValue != null) {
            return blockValue.multiplier(count);
        } else if (materialValue != null) {
            return materialValue.multiplier(count);
        } else {
            return 0;
        }
    }
}
