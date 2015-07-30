package com.pauljoda.modularsystems.storage.tiles;

import com.pauljoda.modularsystems.storage.blocks.BlockStorageIO;
import com.pauljoda.modularsystems.storage.container.ContainerStorageIO;
import com.pauljoda.modularsystems.storage.gui.GuiStorageIO;
import com.teambr.bookshelf.collections.InventoryTile;
import com.teambr.bookshelf.util.InventoryUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/29/2015
 */
public class TileStorageIO extends TileEntityStorageExpansion {
    private static final int IMPORT_MAX = 8;
    private static final int EXPORT_MAX = 17;

    private static final int IMPORT = 0;
    private static final int EXPORT = 1;

    protected boolean [] canMove;
    protected boolean [] matchDamage;
    protected boolean [] matchNBT;
    protected boolean [] matchOreDict;
    protected boolean [] whiteList;

    protected InventoryTile filter;

    public TileStorageIO() {
        filter = new InventoryTile(18);
        canMove = new boolean[] {false, false};
        matchDamage = new boolean[] {false, false};
        matchNBT = new boolean[] {false, false};
        matchOreDict = new boolean[] {false, false};
        whiteList = new boolean[] {true, true};
    }

    @Override
    public void addedToNetwork() {}

    @Override
    public void removedFromNetwork() {}

    /*******************************************************************************************************************
     ********************************************** Value Methods ******************************************************
     *******************************************************************************************************************/

    public boolean isCanImport() {
        return canMove[IMPORT];
    }

    public void setCanImport(boolean canImport) {
        this.canMove[IMPORT] = canImport;
    }

    public boolean isCanExport() {
        return canMove[EXPORT];
    }

    public void setCanExport(boolean canExport) {
        this.canMove[EXPORT] = canExport;
    }

    public boolean isImportDamage() {
        return matchDamage[IMPORT];
    }

    public void setImportDamage(boolean importDamage) {
        this.matchDamage[IMPORT] = importDamage;
    }

    public boolean isImportOreDict() {
        return matchOreDict[IMPORT];
    }

    public void setImportOreDict(boolean importOreDict) {
        this.matchOreDict[IMPORT] = importOreDict;
    }

    public boolean isExportDamage() {
        return matchDamage[EXPORT];
    }

    public void setExportDamage(boolean exportDamage) {
        this.matchDamage[EXPORT] = exportDamage;
    }

    public boolean isImportNBT() {
        return matchNBT[IMPORT];
    }

    public void setImportNBT(boolean importNBT) {
        this.matchNBT[IMPORT] = importNBT;
    }

    public boolean isExportNBT() {
        return matchNBT[EXPORT];
    }

    public void setExportNBT(boolean exportNBT) {
        this.matchNBT[EXPORT] = exportNBT;
    }

    public boolean isExportOreDict() {
        return matchOreDict[EXPORT];
    }

    public void setExportOreDict(boolean exportOreDict) {
        this.matchOreDict[EXPORT] = exportOreDict;
    }

    public boolean whiteListExport() {
        return whiteList[EXPORT];
    }

    public void setWhiteListExport(boolean exportWhiteList) {
        this.whiteList[EXPORT] = exportWhiteList;
    }

    public boolean whiteListImport() {
        return whiteList[IMPORT];
    }

    public void setWhiteListImport(boolean importWhiteList) {
        this.whiteList[IMPORT] = importWhiteList;
    }

    /*******************************************************************************************************************
     ********************************************** Tile Methods ******************************************************
     *******************************************************************************************************************/

    private List<Integer> retrieveStacks(IInventory other, int mode) {
        List<Integer> returnStacks = new ArrayList<>();

        for(int i = 0; i < other.getSizeInventory(); i++) { //Cycle their inventory
            ItemStack stack = other.getStackInSlot(i);
            if(stack != null) { //Found something to compare
                boolean shouldStop = false;

                if(isFilterEmpty(mode)) {
                    returnStacks.add(i);
                    shouldStop = true;
                }

                if(shouldStop)
                    continue;


                for(int j = (mode == IMPORT ? 0 : IMPORT_MAX + 1); j < (mode == IMPORT ? IMPORT_MAX : EXPORT_MAX); j++) {
                    ItemStack filterStack = filter.getStackInSlot(j);
                    if(filterStack != null) {
                        boolean isMatch = true;
                        //Should probably be the same item, though it could be ore dict
                        if(stack.getItem() != filterStack.getItem() && !matchOreDict[mode])
                            isMatch = false;

                        //Do we care about damage?
                        if(matchDamage[mode] && stack.getItemDamage() != filterStack.getItemDamage())
                            isMatch = false;

                        //How about tags?
                        if(matchNBT[mode] && stack.getTagCompound() == filterStack.getTagCompound())
                            isMatch = false;

                        //Should be ore dict
                        if(matchOreDict[mode] && (OreDictionary.getOreIDs(stack).length == 0 ? OreDictionary.getOreIDs(filterStack).length != 0 : !Arrays.equals(OreDictionary.getOreIDs(stack), OreDictionary.getOreIDs(filterStack))))
                            isMatch = false;

                        if(whiteList[mode] && isMatch) //White List and matches
                            returnStacks.add(i);
                        else if(!whiteList[mode] && !isMatch) //Black list and doesn't match
                            returnStacks.add(i);
                    }
                }
            }
        }
        return returnStacks;
    }

    private boolean isFilterEmpty(int mode) {
        for (int j = (mode == IMPORT ? 0 : IMPORT_MAX + 1); j < (mode == IMPORT ? IMPORT_MAX : EXPORT_MAX); j++) {
            if(filter.getStackInSlot(j) != null)
                return false;
        }
        return true;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(getCore() != null) {

            //No point wasting resources if we can't do anything
            if(!canMove[IMPORT] && !canMove[EXPORT])
                return;

            //Add some random delay
            if(worldObj.rand.nextInt(20) != 10)
                return;

            ForgeDirection facing = ((BlockStorageIO)worldObj.getBlock(xCoord, yCoord, zCoord)).getDefaultRotation().convertMetaToDirection(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
            TileEntity otherInv = getTileInDirection(facing);

            //Make sure this is an inventory and is not ourselves
            if(otherInv instanceof IInventory && !(otherInv instanceof TileEntityStorageExpansion || otherInv instanceof TileStorageCore)) {
                //Do importing first
                if(canMove[IMPORT]) {
                    for(int slot : retrieveStacks((IInventory) otherInv, IMPORT)) {
                        InventoryUtils.moveItemInto((IInventory) otherInv, slot, getCore(), -1, 64, facing, true, true);
                        worldObj.markBlockForUpdate(core.x, core.y, core.z);
                        worldObj.markBlockForUpdate(otherInv.xCoord, otherInv.yCoord, otherInv.zCoord);
                    }
                }

                //Export next
                if(canMove[EXPORT]) {
                    for(int slot : retrieveStacks(getCore(), EXPORT)) {
                        InventoryUtils.moveItemInto(getCore(), slot, otherInv, -1, 64, facing.getOpposite(), true, true);
                        worldObj.markBlockForUpdate(core.x, core.y, core.z);
                        worldObj.markBlockForUpdate(otherInv.xCoord, otherInv.yCoord, otherInv.zCoord);
                    }
                }
            }
        }
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);
        filter.readFromNBT(tag);

        canMove[IMPORT] = tag.getBoolean("CanImport");
        canMove[EXPORT] = tag.getBoolean("CanExport");

        matchDamage[IMPORT] = tag.getBoolean("ImportDamage");
        matchDamage[EXPORT] = tag.getBoolean("ExportDamage");

        matchNBT[IMPORT] = tag.getBoolean("ImportNBT");
        matchNBT[EXPORT] = tag.getBoolean("ExportNBT");

        matchOreDict[IMPORT] = tag.getBoolean("ImportOre");
        matchOreDict[EXPORT] = tag.getBoolean("ExportOre");

        whiteList[IMPORT] = tag.getBoolean("ImportWhiteList");
        whiteList[EXPORT] = tag.getBoolean("ExportWhiteList");
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);
        filter.writeToNBT(tag);

        tag.setBoolean("CanImport", canMove[IMPORT]);
        tag.setBoolean("CanExport", canMove[EXPORT]);

        tag.setBoolean("ImportDamage", matchDamage[IMPORT]);
        tag.setBoolean("ExportDamage", matchDamage[EXPORT]);

        tag.setBoolean("ImportNBT", matchNBT[IMPORT]);
        tag.setBoolean("ExportNBT", matchNBT[EXPORT]);

        tag.setBoolean("ImportOre", matchOreDict[IMPORT]);
        tag.setBoolean("ExportOre", matchOreDict[EXPORT]);

        tag.setBoolean("ImportWhiteList", whiteList[IMPORT]);
        tag.setBoolean("ExportWhiteList", whiteList[EXPORT]);
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
