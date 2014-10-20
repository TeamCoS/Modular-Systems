package com.teamcos.modularsystems.helpers;

import cpw.mods.fml.common.IFuelHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;

public class VanillaFuelHandler implements IFuelHandler {
    @Override
    public int getBurnTime(ItemStack fuel) {

        Item item;

        if (fuel == null ) {
            return 0;
        } else if ((item = fuel.getItem()) instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
            Block block = Block.getBlockFromItem(item);

            if (block == Blocks.wooden_slab) {
                return 150;
            } else if (block.getMaterial() == Material.wood) {
                return 300;
            } else if (block == Blocks.coal_block) {
                return 16000;
            } else {
                return 0;
            }
        } else  if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD")) {
            return 200;
        } else if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD")) {
            return 200;
        } else if (item instanceof ItemHoe && ((ItemHoe) item).getToolMaterialName().equals("WOOD")) {
            return 200;
        } else if (item == Items.stick) {
            return 100;
        } else if (item == Items.coal) {
            return 1600;
        } else if (item == Items.lava_bucket) {
            return 20000;
        } else if (item == Item.getItemFromBlock(Blocks.sapling)) {
            return 100;
        } else if (item == Items.blaze_rod) {
            return 2400;
        } else {
            return 0;
        }
    }
}
