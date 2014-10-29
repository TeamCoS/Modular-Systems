package com.teamcos.modularsystems.core.lib;

import com.teamcos.modularsystems.core.managers.BlockManager;
import com.teamcos.modularsystems.interfaces.MSUpgradeBlock;
import com.teamcos.modularsystems.manager.ApiBlockManager;
import com.teamcos.modularsystems.registries.BannedFurnaceBlockRegistry;
import com.teamcos.modularsystems.registries.BannedOreProcessorBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

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
}
