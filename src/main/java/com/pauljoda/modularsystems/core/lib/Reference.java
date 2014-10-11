package com.pauljoda.modularsystems.core.lib;

import com.pauljoda.modularsystems.core.helper.ConfigHelper;
import com.pauljoda.modularsystems.core.managers.BlockManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Reference {

    public static final int BASIC_EXPANSION = 0;
    public static final int STORAGE_EXPANSION = 1;
    public static final int HOPPING_STORAGE_EXPANSION = 2;
    public static final int ARMOR_STORAGE_EXPANSION = 3;
    public static final int SMASHING_STORAGE_EXPANSION = 4;
    public static final int SORTING_STORAGE_EXPANSION = 5;
    public static final int CRAFTING_STORAGE_EXPANSION = 6;

    public static final String MOD_ID = "modularsystems";
    public static final String MOD_NAME = "Modular Systems";
    public static final String VERSION = "1.51";
    public static final String CHANNEL_NAME = MOD_ID;

    //FURNACE: Blocks that are from my mod, used to prevent overlaying on reload
    public static String[] modularTiles =
            {BlockManager.furnaceDummy.getUnlocalizedName(),
                    BlockManager.furnaceCraftingUpgrade.getUnlocalizedName(),
                    BlockManager.furnaceDummyIO.getUnlocalizedName(),
                    BlockManager.furnaceAddition.getUnlocalizedName(),
                    BlockManager.smelteryDummy.getUnlocalizedName(),
                    BlockManager.smelteryDummyIO.getUnlocalizedName()
            };

    //FURNACE: Checks if the block is valid to form furnace
    public static boolean isValidBlock(String blockId)
    {

        for(int i = 0; i < ConfigHelper.bannedBlocks.length; i++)
        {
            if(blockId.equals(ConfigHelper.bannedBlocks[i]))
            {
                return false;
            }
        }

        if(blockId.equals(BlockManager.furnaceCraftingUpgrade.getUnlocalizedName()) || blockId.equals(BlockManager.furnaceDummyIO.getUnlocalizedName()) || blockId.equals(Blocks.redstone_block.getUnlocalizedName()) || blockId.equals(BlockManager.furnaceAddition.getUnlocalizedName()))
            return true;

        return true;
    }

    //FURNACE: Checks if it is an active dummy
    public static boolean isModularTile(String blockId) {
        for(int i = 0; i < modularTiles.length; i++)
        {
            if(blockId.equals(modularTiles[i]))
                return true;
        }
        return false;
    }

    //FURNACE
    public static boolean isBadBlock(Block blockId) {

        if(blockId == BlockManager.furnaceCraftingUpgrade || blockId == BlockManager.furnaceDummyIO || blockId == Blocks.redstone_block || blockId == BlockManager.furnaceAddition || blockId == BlockManager.furnaceAddition)
            return false;

        if(blockId.hasTileEntity(0))
            return true;

        if(!blockId.isNormalCube())
            return true;

        int oreDictCheck = OreDictionary.getOreID(new ItemStack(blockId));
        int isWood = OreDictionary.getOreID("logWood");
        int isPlank = OreDictionary.getOreID("plankWood");
        return oreDictCheck == isWood || oreDictCheck == isPlank;

    }

    //FURNACE: Gets Speed Multiplier
    public static double getSpeedMultiplierForBlock(Block block)
    {
        if(block == Blocks.redstone_block)
            return 1.2;
        else if(block == Blocks.gold_block)
            return 1;
        else if(block == Blocks.diamond_block)
            return 3;
        else if(block == Blocks.netherrack)
            return 1;
        else if(block == Blocks.lapis_block)
            return 1;
        else if(block == Blocks.sandstone)
            return 0.3;
        else if(block == Blocks.brick_block)
            return 0.5;
        else if(block == Blocks.soul_sand)
            return 0.3;
        else if(block == Blocks.nether_brick)
            return 1.0;
        else if(block == Blocks.hardened_clay)
            return 0.6;
        else if(block.getMaterial() == Material.iron)
            return 1;
        else if(block.getMaterial() == Material.rock && block != Blocks.cobblestone)
            return 0.2;
        else
            return 0;
    }

    //FURNACE: Gets Efficiency Multiplier
    public static double getEfficiencyMultiplierForBlock(Block block)
    {
        if(block == Blocks.iron_block)
            return 0.1;
        else if(block == Blocks.coal_block)
            return 0.05;
        else if(block == Blocks.redstone_block)
            return -2;
        else if(block == Blocks.diamond_block)
            return 0.6;
        else if(block == Blocks.netherrack)
            return -0.7;
        else if(block == Blocks.stone)
            return 0.005;
        else if(block == Blocks.stonebrick)
            return 0.007;
        else if(block == Blocks.sand)
            return -0.5;
        else if(block == Blocks.lapis_block)
            return 0.1;
        else if(block == Blocks.sandstone)
            return 0.001;
        else if(block == Blocks.nether_brick)
            return -0.5;
        else if(block == Blocks.quartz_block)
            return 0.1;
        else if(block == Blocks.hardened_clay)
            return 0.001;
        else if(block.getMaterial() == Material.iron)
            return 0.03;
        else if(block.getMaterial() == Material.rock && block != Blocks.cobblestone)
            return 0.001;
        else
            return 0.0;
    }
}
