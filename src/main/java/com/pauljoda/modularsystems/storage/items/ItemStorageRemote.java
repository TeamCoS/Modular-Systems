package com.pauljoda.modularsystems.storage.items;

import cofh.api.energy.IEnergyContainerItem;
import com.pauljoda.modularsystems.core.items.BaseItem;
import com.pauljoda.modularsystems.storage.tiles.TileStorageRemote;
import com.teambr.bookshelf.Bookshelf;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

/**
 * Modular-Systems
 * Created by Dyonovan on 01/08/15
 */
public class ItemStorageRemote extends BaseItem implements IEnergyContainerItem {

    public final int CAPACITY = 10000;
    public final int MAX_IN_OUT = 100;
    public final int MAX_DISTANCE = 30;

    public ItemStorageRemote(String name, int maxStackSize) {
        super(name, maxStackSize);
        setMaxDamage(16);
        setHasSubtypes(true);
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        setEnergy(itemStack, 0);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            if (itemStack.stackTagCompound != null) {
                if (itemStack.stackTagCompound.hasKey("coreX")) {
                    //todo check for distance
                    int x = itemStack.stackTagCompound.getInteger("coreX");
                    int y = itemStack.stackTagCompound.getInteger("coreY");
                    int z = itemStack.stackTagCompound.getInteger("coreZ");
                    if (world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileStorageRemote) {
                        TileStorageRemote tile = (TileStorageRemote) world.getTileEntity(x, y, z);
                        if (tile.getCore() != null && tile.getCore().canOpen(player) &&
                                extractEnergy(itemStack, MAX_IN_OUT, true) >= MAX_IN_OUT &&
                                (Vec3.createVectorHelper(player.posX, player.posY, player.posZ).
                                        distanceTo(Vec3.createVectorHelper(x, y, z)) <= MAX_DISTANCE)) {
                            extractEnergy(itemStack, MAX_IN_OUT, false);
                            player.openGui(Bookshelf.instance, 0, world, tile.getCore().xCoord, tile.getCore().yCoord, tile.getCore().zCoord);
                        }
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
            updateDamage(container);
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
            updateDamage(container);
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

    private void updateDamage(ItemStack stack) {
        float r = (float) getEnergyStored(stack) / getMaxEnergyStored(stack);
        int res = 16 - Math.round(r * 16);
        stack.setItemDamage(res);
    }

    void setEnergy(ItemStack container, int energy) {
        if (container.stackTagCompound == null) {
            container.stackTagCompound = new NBTTagCompound();
        }
        container.stackTagCompound.setInteger("Energy", energy);
        updateDamage(container);
    }

    void setFull(ItemStack container) {
        setEnergy(container, CAPACITY);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List) {
        ItemStack is = new ItemStack(this);
        setFull(is);
        par3List.add(is);

        is = new ItemStack(this);
        setEnergy(is, 0);
        par3List.add(is);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
        if (itemStack.stackTagCompound != null) {
            if (itemStack.stackTagCompound.hasKey("coreX")) {
                NBTTagCompound tag = itemStack.stackTagCompound;
                list.add("Receiver Location X:" + tag.getInteger("coreX") + " Y:" + tag.getInteger("coreY") + " Z:" + tag.getInteger("coreZ"));
            } else
                list.add("Unlinked");
            if (itemStack.stackTagCompound.hasKey("Energy"))
                list.add(itemStack.stackTagCompound.getInteger("Energy") + "/" + CAPACITY + " RF");
            else
                list.add("0/" + CAPACITY + " RF");
            return;
        }
        list.add("Unlinked");
    }
}
