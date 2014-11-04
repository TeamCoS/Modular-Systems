package com.teamcos.modularsystems.helpers;

import com.teamcos.modularsystems.interfaces.MSUpgradeBlock;
import com.teamcos.modularsystems.notification.Notification;
import com.teamcos.modularsystems.notification.NotificationHelper;
import com.teamcos.modularsystems.registries.BannedFurnaceBlockRegistry;
import com.teamcos.modularsystems.registries.BannedOreProcessorBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class FurnaceHelper {

    //FURNACE
    public static boolean isBadBlock(Block blockId, World world) {

        if (blockId instanceof MSUpgradeBlock || blockId == Blocks.redstone_block) return false;
        if (blockId.hasTileEntity(0)) {
            if(world.isRemote)
                NotificationHelper.addNotification(new Notification(new ItemStack(blockId), EnumChatFormatting.RED + "ERROR: Tile Entity Found", blockId.getLocalizedName(), Notification.DEFAULT_DURATION));
            return true;
        }
        if (!blockId.isNormalCube()) {
            if(world.isRemote)
                NotificationHelper.addNotification(new Notification(new ItemStack(blockId), EnumChatFormatting.RED + "ERROR: Non-Solid Block", blockId.getLocalizedName(), Notification.DEFAULT_DURATION));
            return true;
        }

        int oreDictCheck = OreDictionary.getOreID(new ItemStack(blockId));
        int isWood = OreDictionary.getOreID("logWood");
        int isPlank = OreDictionary.getOreID("plankWood");
        return oreDictCheck == isWood || oreDictCheck == isPlank;

    }

    public static boolean isModularBlock(Block block) {
        return block instanceof MSUpgradeBlock;
    }

    //FURNACE: Checks if the block is valid to form furnace
    public static boolean isValidBlock(String blockId) {

        Block block = Block.getBlockFromName(blockId);
        if (BannedFurnaceBlockRegistry.isBanned(blockId) || BannedOreProcessorBlockRegistry.isBanned(blockId)) return false;

        if (block instanceof MSUpgradeBlock || blockId.equals(Blocks.redstone_block.getUnlocalizedName())) return true;

        return true;
    }
}
