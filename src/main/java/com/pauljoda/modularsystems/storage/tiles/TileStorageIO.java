package com.pauljoda.modularsystems.storage.tiles;

import com.pauljoda.modularsystems.storage.container.ContainerStorageIO;
import com.pauljoda.modularsystems.storage.gui.GuiStorageIO;
import com.teambr.bookshelf.collections.InventoryTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/29/2015
 */
public class TileStorageIO extends TileEntityStorageExpansion {
    private static final int IMPORT_MAX = 8;
    private static final int EXPORT_MAX = 17;

    protected boolean canImport;
    protected boolean canExport;

    protected boolean importDamage;
    protected boolean exportDamage;

    protected boolean importOreDict;
    protected boolean exportOreDict;

    protected InventoryTile filter;

    public TileStorageIO() {
        filter = new InventoryTile(18);

        canImport = canExport = importDamage = exportDamage = importOreDict = exportOreDict = false;
    }

    @Override
    public void addedToNetwork() {}

    @Override
    public void removedFromNetwork() {}

    /*******************************************************************************************************************
     ********************************************** Value Methods ******************************************************
     *******************************************************************************************************************/

    public boolean isCanImport() {
        return canImport;
    }

    public void setCanImport(boolean canImport) {
        this.canImport = canImport;
    }

    public boolean isCanExport() {
        return canExport;
    }

    public void setCanExport(boolean canExport) {
        this.canExport = canExport;
    }

    public boolean isImportDamage() {
        return importDamage;
    }

    public void setImportDamage(boolean importDamage) {
        this.importDamage = importDamage;
    }

    public boolean isImportOreDict() {
        return importOreDict;
    }

    public void setImportOreDict(boolean importOreDict) {
        this.importOreDict = importOreDict;
    }

    public boolean isExportDamage() {
        return exportDamage;
    }

    public void setExportDamage(boolean exportDamage) {
        this.exportDamage = exportDamage;
    }

    public boolean isExportOreDict() {
        return exportOreDict;
    }

    public void setExportOreDict(boolean exportOreDict) {
        this.exportOreDict = exportOreDict;
    }

    /*******************************************************************************************************************
     ********************************************** Tile Methods ******************************************************
     *******************************************************************************************************************/

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);
        filter.readFromNBT(tag);

        canImport = tag.getBoolean("CanImport");
        canExport = tag.getBoolean("CanExport");

        importDamage = tag.getBoolean("ImportDamage");
        exportDamage = tag.getBoolean("ExportDamage");

        importOreDict = tag.getBoolean("ImportOre");
        exportOreDict = tag.getBoolean("ExportOre");
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);
        filter.writeToNBT(tag);

        tag.setBoolean("CanImport", canImport);
        tag.setBoolean("CanExport", canExport);

        tag.setBoolean("ImportDamage", importDamage);
        tag.setBoolean("ExportDamage", exportDamage);

        tag.setBoolean("ImportOre", importOreDict);
        tag.setBoolean("ExportOre", exportOreDict);
    }

    /*******************************************************************************************************************
     ***************************************** IInventory Methods ******************************************************
     *******************************************************************************************************************/

    @Override
    public int getSizeInventory() {
        return 18;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return filter.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if(filter.getStackInSlot(slot) != null) {
            ItemStack returnStack;
            if(filter.getStackInSlot(slot).stackSize <= amount) {
                returnStack = filter.getStackInSlot(slot);
                filter.setStackInSlot(null, slot);
                this.markDirty();
                return returnStack;
            } else {
                returnStack = filter.getStackInSlot(slot).splitStack(amount);
                if(filter.getStackInSlot(slot).stackSize <= 0)
                    filter.setStackInSlot(null, slot);
                this.markDirty();
                return returnStack;
            }
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return filter.getStackInSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        filter.setStackInSlot(stack, slot);
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName() {
        return "inventory.storageIO.title";
    }

    /**
     * Returns if the inventory is named
     */
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * Do not give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player) {
        return getCore() != null;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true;
    }

    /*******************************************************************************************************************
     ****************************************** IOpensGui Methods ******************************************************
     *******************************************************************************************************************/

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return getCore() != null ? new ContainerStorageIO(player.inventory, this) : null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return getCore() != null ? new GuiStorageIO(new ContainerStorageIO(player.inventory, this), this) : null;
    }
}
