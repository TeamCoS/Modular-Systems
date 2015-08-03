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

import java.util.ArrayList;
import java.util.List;

/**
 * Modular-Systems
 * Created by Dyonovan on 01/08/15
 */
public class ItemStorageRemote extends BaseItem implements IEnergyContainerItem {

    public final int CAPACITY = 10000;
    public final int MAX_IN_OUT = 100;

    public ItemStorageRemote() {
        super("itemStorageRemote", 1);
        setMaxDamage(16);
        setHasSubtypes(true);
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        setEnergy(itemStack, 0);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        ArrayList<Integer> coords = getCoords(itemStack.getTagCompound());
        if (coords != null) {
            if (world.getTileEntity(coords.get(0), coords.get(1), coords.get(2)) instanceof TileStorageRemote) {
                TileStorageRemote tile = (TileStorageRemote) world.getTileEntity(coords.get(0), coords.get(1), coords.get(2));
                if (isValid(tile, player)) {
                    extractEnergy(itemStack, MAX_IN_OUT, false);
                    player.openGui(Bookshelf.instance, 0, world, tile.getCore().xCoord, tile.getCore().yCoord, tile.getCore().zCoord);
                }
            }
        }

        return itemStack;
    }

    private ArrayList<Integer> getCoords(NBTTagCompound tag) {
        ArrayList<Integer> list = new ArrayList<>();
        if (tag.hasKey("coreX")) {
            list.add(tag.getInteger("coreX"));
            list.add(tag.getInteger("coreY"));
            list.add(tag.getInteger("coreZ"));
            return list;
        }
        return null;
    }

    private boolean isValid(TileStorageRemote tile, EntityPlayer player) {
        if (tile.getCore() != null) {
            if (tile.getWorldObj().provider.dimensionId == player.dimension) {
                if (tile.getCore().canOpen(player)) {
                    int distance = (int)Math.round(Vec3.createVectorHelper(player.posX, player.posY, player.posZ).
                            distanceTo(Vec3.createVectorHelper(tile.xCoord, tile.yCoord, tile.zCoord)));
                    if (distance <= tile.max_distance) {
                        return true;
                    }
                }
            }
        }
        return false;
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
