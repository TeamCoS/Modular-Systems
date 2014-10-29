package com.teamcos.modularsystems.core.lib;

import com.teamcos.modularsystems.core.helper.BlockValueHelper;
import com.teamcos.modularsystems.core.helper.ConfigHelper;
import com.teamcos.modularsystems.core.managers.BlockManager;
import com.teamcos.modularsystems.furnace.config.BlockConfig;
import com.teamcos.modularsystems.manager.ApiBlockManager;
import com.teamcos.modularsystems.notification.Notification;
import com.teamcos.modularsystems.notification.NotificationTickHandler;
import com.teamcos.modularsystems.utilities.block.DummyBlock;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Random;

public class Reference {

    public static final Random random = new Random();
    public static final int BASIC_EXPANSION = 0;
    public static final int STORAGE_EXPANSION = 1;
    public static final int HOPPING_STORAGE_EXPANSION = 2;
    public static final int ARMOR_STORAGE_EXPANSION = 3;
    public static final int SMASHING_STORAGE_EXPANSION = 4;
    public static final int SORTING_STORAGE_EXPANSION = 5;
    public static final int CRAFTING_STORAGE_EXPANSION = 6;
    public static final int MAX_FURNACE_SIZE = 50;

    public static final String MOD_ID = "modularsystems";
    public static final String CHANNEL_NAME = MOD_ID;
    public static final String MOD_NAME = "Modular Systems";
    public static final String VERSION = "@VERSION@";
    //FURNACE: Blocks that are from my mod, used to prevent overlaying on reload
    public static String[] modularTiles = {
                    ApiBlockManager.dummyBlock.getUnlocalizedName(),
                    ApiBlockManager.dummyIOBlock.getUnlocalizedName(),
                    BlockManager.furnaceCraftingUpgrade.getUnlocalizedName(),
                    BlockManager.furnaceAddition.getUnlocalizedName()
            };

    //FURNACE: Checks if the block is valid to form furnace
    public static boolean isValidBlock(String blockId) {

        for (int i = 0; i < ConfigHelper.bannedBlocks.length; i++) {
            if (blockId.equals(ConfigHelper.bannedBlocks[i])) {
                return false;
            }
        }

        if (blockId.equals(BlockManager.furnaceCraftingUpgrade.getUnlocalizedName()) || blockId.equals(ApiBlockManager.dummyIOBlock.getUnlocalizedName()) || blockId.equals(Blocks.redstone_block.getUnlocalizedName()) || blockId.equals(BlockManager.furnaceAddition.getUnlocalizedName()))
            return true;

        return true;
    }

    //FURNACE: Checks if it is an active dummy
    public static boolean isModularTile(String blockId) {
        for (int i = 0; i < modularTiles.length; i++) {
            if (blockId.equals(modularTiles[i]))
                return true;
        }
        return false;
    }

    //FURNACE
    public static boolean isBadBlock(Block blockId) {

        if (blockId == BlockManager.furnaceCraftingUpgrade || blockId == ApiBlockManager.dummyIOBlock || blockId == Blocks.redstone_block || blockId == BlockManager.furnaceAddition || blockId == BlockManager.furnaceAddition)
            return false;

        if(blockId instanceof DummyBlock)
            return false;

        if (blockId.hasTileEntity(0)) {
            NotificationTickHandler.guiNotification.queueNotification(new Notification(new ItemStack(blockId), EnumChatFormatting.RED + "ERROR: Tile Entity Found", blockId.getLocalizedName()));
            return true;
        }

        if (!blockId.isNormalCube()) {
            NotificationTickHandler.guiNotification.queueNotification(new Notification(new ItemStack(blockId), EnumChatFormatting.RED + "ERROR: Non-Solid Block", blockId.getLocalizedName()));
            return true;
        }

        int oreDictCheck = OreDictionary.getOreID(new ItemStack(blockId));
        int isWood = OreDictionary.getOreID("logWood");
        int isPlank = OreDictionary.getOreID("plankWood");
        if(oreDictCheck == isWood || oreDictCheck == isPlank)
            NotificationTickHandler.guiNotification.queueNotification(new Notification(new ItemStack(blockId), EnumChatFormatting.RED + "ERROR: Invalid Block", blockId.getLocalizedName()));

        return oreDictCheck == isWood || oreDictCheck == isPlank;

    }

    //FURNACE: Gets Speed Multiplier
    public static double getSpeedMultiplierForBlock(Block block, int count) {
        BlockConfig blockValue = BlockValueHelper.getBlockValueForBlock(block);
        BlockConfig materialValue = BlockValueHelper.getMaterialValueForBlock(block);
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
        BlockConfig blockValue = BlockValueHelper.getBlockValueForBlock(block);
        BlockConfig materialValue = BlockValueHelper.getMaterialValueForBlock(block);
        if (blockValue != null) {
            return blockValue.efficiency(count);
        } else if (materialValue != null) {
            return materialValue.efficiency(count);
        } else {
            return 0.0;
        }
    }
}
