package com.teambr.modularsystems.api.jei.transfer;

import com.teambr.modularsystems.core.network.PacketManager;
import com.teambr.modularsystems.storage.container.ContainerStorageCore;
import com.teambr.modularsystems.storage.network.FillCraftingGrid;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.gui.ingredients.IGuiIngredient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * This file was created for Modular-Systems
 * <p>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis "pauljoda"
 * @since 3/29/2016
 */
public class CraftingTransferHandler implements IRecipeTransferHandler {
    @Override
    public Class<? extends Container> getContainerClass() {
        return ContainerStorageCore.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return "minecraft.crafting";
    }

    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(@Nonnull Container container, @Nonnull IRecipeLayout recipeLayout,
                                               @Nonnull EntityPlayer player, boolean maxTransfer, boolean doTransfer) {
        if(doTransfer && container instanceof ContainerStorageCore) {
            Map<Integer, ? extends IGuiIngredient<ItemStack>> inputs = recipeLayout.getItemStacks().getGuiIngredients();
            NBTTagCompound tag = new NBTTagCompound();
            for(int x = 0; x < 10; x++) {
                if(inputs.size() > x + 1) {
                    IGuiIngredient<ItemStack> input = inputs.get(x + 1);
                    if (input.isInput()) {
                        NBTTagList allPossibleTagList = new NBTTagList();
                        for(ItemStack stack : input.getAllIngredients()) {
                            NBTTagCompound itemTag = new NBTTagCompound();
                            stack.writeToNBT(itemTag);
                            allPossibleTagList.appendTag(itemTag);
                        }
                        tag.setTag("Stack:" + x, allPossibleTagList);
                    }
                }
            }

            ((ContainerStorageCore)container).fillCraftingGrid(tag);
            PacketManager.net.sendToServer(new FillCraftingGrid(tag));
        }
        return null;
    }
}
