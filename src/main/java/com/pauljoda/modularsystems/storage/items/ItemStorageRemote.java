package com.pauljoda.modularsystems.storage.items;

import cofh.api.energy.IEnergyContainerItem;
import com.pauljoda.modularsystems.core.items.BaseItem;
import com.pauljoda.modularsystems.storage.tiles.TileStorageCore;
import com.teambr.bookshelf.Bookshelf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

/**
 * Modular-Systems
 * Created by Dyonovan on 01/08/15
 */
public class ItemStorageRemote extends BaseItem implements IEnergyContainerItem {

    public final int CAPACITY = 10000;
    public final int MAX_IN_OUT = 100;

    public ItemStorageRemote(String name, int maxStackSize) {
        super(name, maxStackSize);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (itemStack.stackTagCompound != null) {
            if (itemStack.stackTagCompound.hasKey("coreX")) {
                //todo check for distance and open gui of chest & reduce energy
                int x = itemStack.stackTagCompound.getInteger("coreX");
                int y = itemStack.stackTagCompound.getInteger("coreY");
                int z = itemStack.stackTagCompound.getInteger("coreZ");
                if (world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileStorageCore) {
                    TileStorageCore core  = (TileStorageCore) world.getTileEntity(x, y, z);
                    if (core.canOpen(player)) {
                        player.openGui(Bookshelf.instance, 0, world, x, y, z);
                    }
                }
            }
        }
        return itemStack;
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        if (container.stackTagCompound == null) {
            container.stackTagCompound = new NBTTagCompound();
        }
        int energy = container.stackTagCompound.getInteger("Energy");
        int energyReceived = Math.min(CAPACITY - energy, Math.min(MAX_IN_OUT, maxReceive));

        if (!simulate) {
            energy += energyReceived;
            container.stackTagCompound.setInteger("Energy", energy);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy")) {
            return 0;
        }
        int energy = container.stackTagCompound.getInteger("Energy");
        int energyExtracted = Math.min(energy, Math.min(MAX_IN_OUT, maxExtract));

        if (!simulate) {
            energy -= energyExtracted;
            container.stackTagCompound.setInteger("Energy", energy);
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy")) {
            return 0;
        }
        return container.stackTagCompound.getInteger("Energy");
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return CAPACITY;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
        if (itemStack.stackTagCompound != null) {
            if(itemStack.stackTagCompound.hasKey("coreX")) {
                NBTTagCompound tag = itemStack.stackTagCompound;
                list.add("Core Location " + tag.getInteger("coreX") + "-" + tag.getInteger("coreY") + "-" + tag.getInteger("coreZ"));
                return;
            }

        }
        list.add("Unlinked");
    }
}
