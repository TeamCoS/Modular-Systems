package com.teambr.modularsystems.core.common.container;

import com.teambr.bookshelf.common.container.BaseContainer;
import com.teambr.bookshelf.common.container.InventoryCallback;
import com.teambr.bookshelf.common.tiles.traits.Inventory;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis "pauljoda"
 * @since 3/24/2016
 */
public class ContainerBlockValueConfig extends BaseContainer {
    public String itemName;
    public Block currentBlock;
    public int meta = -1;

    public ContainerBlockValueConfig(IInventory playerInventory) {
        super(playerInventory, new InventoryBlockConfig(null));

        ((InventoryBlockConfig)inventory()).setContainer(this);

        addSlotToContainer(new SlotItemHandler(inventory(), 0, 8, 8));

        addPlayerInventorySlots(8, 120);
    }

    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        transferStackInSlot(player, 0);
        if(inventory().getStackInSlot(0) != null)
            player.dropPlayerItemWithRandomChoice(inventory().getStackInSlot(0), false);
    }
}
