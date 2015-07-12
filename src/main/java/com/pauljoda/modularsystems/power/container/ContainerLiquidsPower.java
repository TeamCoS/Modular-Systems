package com.pauljoda.modularsystems.power.container;

import com.pauljoda.modularsystems.power.tiles.TileLiquidsPower;
import com.teambr.bookshelf.inventory.BaseContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerLiquidsPower extends BaseContainer {

    public ContainerLiquidsPower(InventoryPlayer playerInventory, TileLiquidsPower tileEntity) {
        super(playerInventory, tileEntity);

        addSlotToContainer(new Slot(tileEntity, tileEntity.BUCKET_IN, 60, 40));
        addSlotToContainer(new SlotFurnace(playerInventory.player, tileEntity, tileEntity.BUCKET_OUT, 120, 40));

        bindPlayerInventory(playerInventory);
    }

    private void bindPlayerInventory(InventoryPlayer playerInventory)
    {
        // Inventory
        for(int y = 0; y < 3; y++)
            for(int x = 0; x < 9; x++)
                addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 20 + x * 18, 84 + y * 18));

        // Action Bar
        for(int x = 0; x < 9; x++)
            addSlotToContainer(new Slot(playerInventory, x, 20 + x * 18, 142));
    }
}
