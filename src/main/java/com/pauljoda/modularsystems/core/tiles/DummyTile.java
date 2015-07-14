package com.pauljoda.modularsystems.core.tiles;

import com.pauljoda.modularsystems.core.collections.BlockValues;
import com.pauljoda.modularsystems.core.registries.BlockValueRegistry;
import com.teambr.bookshelf.api.waila.IWaila;
import com.teambr.bookshelf.collections.Location;
import com.teambr.bookshelf.common.tiles.BaseTile;
import com.teambr.bookshelf.helpers.BlockHelper;
import com.teambr.bookshelf.helpers.GuiHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class DummyTile extends BaseTile implements ISidedInventory, IWaila {
    protected Location coreLocation;
    protected int storedBlock;
    protected int metadata = 0;

    public DummyTile() {
        coreLocation = new Location();
    }

    /**
     * Get the core associated with this dummy
     * @return The core object
     */
    @SuppressWarnings("all")
    public AbstractCore getCore() {
        if(coreLocation == null) return null;

        return worldObj.getTileEntity(coreLocation.x, coreLocation.y, coreLocation.z) instanceof AbstractCore ?
                (AbstractCore) worldObj.getTileEntity(coreLocation.x, coreLocation.y, coreLocation.z) : null;
    }

    /**
     * Returns the blocks stored
     * @return The blocks if found, cobblestone if not
     */
    public Block getStoredBlock() {
        return Block.getBlockById(storedBlock) != null ? Block.getBlockById(storedBlock) : Blocks.air;
    }

    /******************************************************************************************************************
     **************************************************  Tile Methods  ************************************************
     ******************************************************************************************************************/

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        if(coreLocation == null)
            coreLocation = new Location();
        coreLocation.readFromNBT(tagCompound);
        storedBlock = tagCompound.getInteger("Stored Block");
        metadata = tagCompound.getInteger("MetaData");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        if(coreLocation != null)
            coreLocation.writeToNBT(tagCompound);
        tagCompound.setInteger("Stored Block", storedBlock);
        tagCompound.setInteger("MetaData", metadata);
    }

    /*****************************************************************************************************************
     *********************************************** Inventory methods ***********************************************
     *****************************************************************************************************************/

    @Override
    public int getSizeInventory() {
        AbstractCore core = getCore();
        return core == null ? 0 : core.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        AbstractCore core = getCore();
        return core == null ? null : core.getStackInSlot(i);
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        AbstractCore core = getCore();
        return core == null ? null : core.decrStackSize(i, j);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        AbstractCore core = getCore();
        return core == null ? null : core.getStackInSlotOnClosing(i);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        AbstractCore core = getCore();
        if(core != null) core.setInventorySlotContents(i, itemstack);
    }

    @Override
    public int getInventoryStackLimit() {
        AbstractCore core = getCore();
        return core == null ? 0 : core.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return getCore() != null;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        AbstractCore core = getCore();
        return core != null && core.isItemValidForSlot(i, itemstack);
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        AbstractCore core = getCore();
        return core != null && core.canInsertItem(i, itemstack, j);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        AbstractCore core = getCore();
        return core != null && core.canExtractItem(i, itemstack, j);
    }

    @Override
    public String getInventoryName() {
        AbstractCore core = getCore();
        return core == null ? "" : core.getInventoryName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        AbstractCore core = getCore();
        return core != null && core.hasCustomInventoryName();
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        AbstractCore core = getCore();
        return core != null ? core.getAccessibleSlotsFromSide(var1) : new int[1];
    }

    /******************************************************************************************************************
     *************************************************  Helper Methods  ***********************************************
     ******************************************************************************************************************/

    /**
     * Set the blocks to be stored in the dummy
     * @param id The blocks id
     */
    public void setBlock(int id) {
        this.storedBlock = id;
    }

    /**
     * Set the metadata of the stored blocks
     * @param metadata The metadata to store
     */
    public void setMetadata(int metadata) {
        this.metadata = metadata;
    }

    /**
     * Get the stored metadata
     * @return Metadata
     */
    public int getMetadata() {
        return metadata;
    }

    /**
     * Set the core for this dummy
     * @param core The core
     */
    public void setCore(AbstractCore core) {
        this.coreLocation = new Location(core);
    }

    /**
     * Invalidate the core location
     */
    public void unsetCore() {
        this.coreLocation = null;
    }

    @Override
    public void returnWailaHead(List<String> tip) {
        if(getStoredBlock() != Blocks.air) {
            tip.add(GuiHelper.GuiColor.YELLOW + "Stored Block: " + GuiHelper.GuiColor.WHITE + new ItemStack(getStoredBlock(), 1, getMetadata()).getDisplayName());
            if (BlockValueRegistry.INSTANCE.isBlockRegistered(getStoredBlock(), getMetadata()) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                BlockValues values = BlockValueRegistry.INSTANCE.values.get(BlockHelper.getBlockString(getStoredBlock(), getMetadata())) != null ? BlockValueRegistry.INSTANCE.values.get(BlockHelper.getBlockString(getStoredBlock(), getMetadata())) : BlockValueRegistry.INSTANCE.values.get(BlockHelper.getBlockString(getStoredBlock()));
                tip.add(GuiHelper.GuiColor.RED + "Speed Function:      " + GuiHelper.GuiColor.WHITE + values.getSpeedFunction().toString());
                tip.add(GuiHelper.GuiColor.GREEN + "Efficiency Function: " + GuiHelper.GuiColor.WHITE + values.getEfficiencyFunction().toString());
                tip.add(GuiHelper.GuiColor.ORANGE + "Multiplicity Function: " + GuiHelper.GuiColor.WHITE + values.getMultiplicityFunction().toString());
            } else if(BlockValueRegistry.INSTANCE.isBlockRegistered(getStoredBlock(), getMetadata()))
                tip.add(GuiHelper.GuiColor.ORANGE + "Press <SHIFT> to display functions");
        }
    }

    @Override
    public void returnWailaBody(List<String> tip) {}

    @Override
    public void returnWailaTail(List<String> tip) {}

    @Override
    public ItemStack returnWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return new ItemStack(Blocks.anvil);
    }
}