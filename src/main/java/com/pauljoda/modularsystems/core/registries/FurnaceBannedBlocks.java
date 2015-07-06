package com.pauljoda.modularsystems.core.registries;

import com.google.gson.reflect.TypeToken;
import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.helpers.LogHelper;
import com.pauljoda.modularsystems.core.utils.JsonUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FurnaceBannedBlocks {
    public static FurnaceBannedBlocks INSTANCE = new FurnaceBannedBlocks();

    protected List<String> bannedBlocks;

    public FurnaceBannedBlocks() {
        bannedBlocks = new ArrayList<>();
    }

    /**
     * Add the values
     */
    public void init() {
        if(!loadFromFile())
            generateDefaults();
        else
            LogHelper.info("Furnace Banned Blocks loaded successfully");
    }

    /**
     * Load the values from the file
     * @return True if successful
     */
    public boolean loadFromFile() {
        LogHelper.info("Loading Furnace Banned Blocks...");
        bannedBlocks = JsonUtils.<ArrayList<String>>readFromJson(new TypeToken<ArrayList<String>>(){}, ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "furnaceBannedBlocks.json");
        return bannedBlocks != null;
    }

    /**
     * Save the current registry to a file
     */
    public void saveToFile() {
        validateList();
        if(!bannedBlocks.isEmpty())
            JsonUtils.writeToJson(bannedBlocks, ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "furnaceBannedBlocks.json");
    }

    /**
     * Used to generate the default values
     */
    public void generateDefaults() {
        LogHelper.info("Either this is the first load of the file is missing, generating default banned blocks");
        addBannedBlock(Blocks.log, -1);
        addBannedBlock(Blocks.log2, -1);
        addBannedBlock(Blocks.dirt, -1);
        addBannedBlock(Blocks.ice, -1);
        addBannedBlock(Blocks.snow, -1);
        addBannedBlock(Blocks.snow_layer, -1);
        addBannedBlock(Blocks.leaves, -1);
        addBannedBlock(Blocks.leaves2, -1);
        addBannedBlock(Blocks.pumpkin, -1);
        addBannedBlock(Blocks.tnt, -1);
        addBannedBlock(Blocks.hay_block, -1);
        addBannedBlock(Blocks.wool, -1);
        addBannedBlock(Blocks.grass, -1);
        addBannedBlock(Blocks.bedrock, -1);
        addBannedBlock(Blocks.diamond_ore, -1);
        addBannedBlock(Blocks.emerald_ore, -1);
        addBannedBlock(Blocks.gold_block, -1);
        addBannedBlock(Blocks.iron_door, -1);

        File path = new File(ModularSystems.configFolderLocation + File.separator + "Registries");
        if(!path.exists())
            path.mkdirs();
    }

    /**
     * Check on the fly if this is bad
     * @param block The block to check
     * @return True if it shouldn't be there
     */
    public static boolean isBadBlockFromBlock(Block block) {
        if (block == Blocks.redstone_block) return false;

        if (block.hasTileEntity(0)) {
           return true;
        }
        if (!block.isNormalCube()) {
           return true;
        }

        int oreDictCheck = OreDictionary.getOreID(new ItemStack(block));
        int isWood = OreDictionary.getOreID("logWood");
        int isPlank = OreDictionary.getOreID("plankWood");
        return oreDictCheck == isWood || oreDictCheck == isPlank;

    }

    /**
     * Make sure the list exists
     */
    private void validateList() {
        if(bannedBlocks == null)
            bannedBlocks = new ArrayList<>();
    }

    /**
     * Add a block to the ban registry
     * @param block The block to ban
     * @param meta The blocks metadata
     */
    public void addBannedBlock(Block block, int meta) {
        validateList();
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(block);
        bannedBlocks.add(id.modId + ":" + id.name + ":" + meta);
    }

    /**
     * Used for adding by name, useful with command
     * @param blockName The name of the block
     *                  'modid:blockname:meta'
     */
    public void addBannedBlock(String blockName) {
        validateList();
        bannedBlocks.add(blockName);
    }

    /**
     * Check if the block has been listed as banned
     * @param block The block to check
     * @param meta The block's metadata
     * @return True if is banned
     */
    public boolean isBlockBanned(Block block, int meta) {
        //Build the string
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(block);
        String blockName = id.modId + ":" + id.name + ":" + meta;
        String blockWithNoMeta = id.modId + ":" + id.name + ":" + -1;

        return bannedBlocks.contains(blockName) || bannedBlocks.contains(blockWithNoMeta);
    }

    @SubscribeEvent
    public void onWorldSaveEvent(WorldEvent.Save event) {
        saveToFile();
    }
}
