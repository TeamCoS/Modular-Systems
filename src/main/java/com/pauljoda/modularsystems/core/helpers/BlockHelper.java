package com.pauljoda.modularsystems.core.helpers;

import com.dyonovan.brlib.collections.Couplet;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class BlockHelper {
    /**
     * Get the string for this block
     * @param block Block to check
     * @param meta Block meta data
     * @return 'modid:name:meta' string representation
     */
    public static String getBlockString(Block block, int meta) {
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(block);
        return id.modId + ":" + id.name + ":" + meta;
    }

    /**
     * Used to get non-meta specific string
     * @param block Block to check
     * @return "modid:name:-1"
     */
    public static String getBlockString(Block block) {
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(block);
        return id.modId + ":" + id.name + ":" + String.valueOf(-1);
    }

    /**
     * Get the block and meta from the string
     * @param str Block string
     * @return The block and meta
     */
    public static Couplet<Block, Integer> getBlockFromString(String str) {
        String[] name = str.split(":");
        switch(name.length) {
            case 3 :
                return new Couplet<>(GameRegistry.findBlock(name[0], name[1]), Integer.valueOf(name[2]) == -1 ? 0 : Integer.valueOf(name[2]));
            case 2 :
                return new Couplet<>(GameRegistry.findBlock(name[0], name[1]), 0);
            case 1 :
            default :
                LogHelper.severe("Invalid string: " + str + ", is not a valid block");
                return new Couplet<>(Blocks.air, 0);
        }
    }
}
