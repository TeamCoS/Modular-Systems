package com.teamcos.modularsystems.utilities.tiles;

import com.teamcos.modularsystems.collections.Doublet;
import com.teamcos.modularsystems.collections.StandardValues;
import com.teamcos.modularsystems.fuelprovider.FuelProvider;
import com.teamcos.modularsystems.fuelprovider.ItemFuelProvider;
import com.teamcos.modularsystems.functions.*;
import com.teamcos.modularsystems.helpers.Coord;
import com.teamcos.modularsystems.helpers.LocalBlockCollections;
import com.teamcos.modularsystems.interfaces.ICore;
import com.teamcos.modularsystems.utilities.tiles.shapes.Cuboid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.*;

public abstract class FueledRecipeTile extends ModularTileEntity implements ISidedInventory, ICore {

    private static final int cookSpeed = 200;
    private static final FuelSorter fuelSorter = new FuelSorter();
    protected static final Random random = new Random();
    protected final StandardValues values;
    protected Cuboid cube;
    //Furnace related things
    private int furnaceBurnTime;
    private int currentItemBurnTime;
    private int furnaceCookTime;
    private boolean isDirty = true;
    private boolean wellFormed = false;
    private String custom_name;

    public FueledRecipeTile(int maxSize) {
        this.values = new StandardValues(this, new BlockCountWorldFunction(), maxSize);
        this.cube = new Cuboid();
    }

    protected abstract void updateBlockState(boolean positiveBurnTime, World world, int x, int y, int z);

    protected abstract int getItemBurnTime(ItemStack is);

    protected abstract ItemStack recipe(ItemStack is);

    public abstract int getMaxSize();

    /**
     * ***************************************************************************************************************
     * *********************************************  Multiblock Methods  **********************************************
     * ****************************************************************************************************************
     */

    @Override
    public boolean updateMultiblock() {
        if (isDirty) {
            if (isWellFormed()) {
                buildMultiblock();
            } else {
                deconstructMultiblock();
            }
            isDirty = false;
        }
        return wellFormed;
    }

    public void breakMultiblock() {
        deconstructMultiblock();
    }

    private boolean isWellFormed() {
        ProperlyFormedWorldFunction myFunction = new ProperlyFormedWorldFunction();
        LocalBlockCollections.searchCuboidMultiBlock(worldObj, myFunction, new Cuboid(this), new Coord(this));
        wellFormed = myFunction.shouldContinue();
        return wellFormed;
    }

    private void buildMultiblock() {
        LocalBlockCollections.searchCuboidMultiBlock(worldObj, xCoord, yCoord, zCoord, new ConvertDummiesWorldFunction(this), getMaxSize());
        cube.setCube(this);
        values.setValues();
    }

    private void deconstructMultiblock() {
        values.unsetValues();
        LocalBlockCollections.searchCuboidMultiBlock(
                worldObj,
                new RevertDummiesWorldFunction(this),
                cube,
                new Coord(this)
        );
        cube.reset();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

        int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, metadata, 2);
    }

    /******************************************************************************************************************
     ************************************************  Furnace Methods  ***********************************************
     ******************************************************************************************************************/

    protected void doFurnaceWork() {
        boolean flag = this.furnaceBurnTime > 0;
        boolean didWork = false;

        if (!worldObj.isRemote) {
            if (flag) {
                --this.furnaceBurnTime;
            }

            if (canSmelt(values.getInput(), recipe(values.getInput()), values.getOutput())) {
                List<FuelProvider> providers = getFuelProviders(values.getTiles());
                if (!providers.isEmpty() && this.furnaceBurnTime <= 0) {
                    int scaledBurnTime = scaledBurnTime(providers.get(0).consume());
                    values.checkInventorySlots();
                    this.currentItemBurnTime = this.furnaceBurnTime = scaledBurnTime;
                    cook();
                    didWork = true;
                } else if (isBurning()) {
                    didWork = cook();
                } else {
                    this.furnaceCookTime = 0;
                    didWork = true;
                }
            }

            if (didWork) {
                updateBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                markDirty();
            }
        }
    }

    protected List<FuelProvider> getFuelProviders(List<Coord> coords) {
        List<FuelProvider> providers = new ArrayList<FuelProvider>();
        FuelProvider provider;
        for (Coord coord : coords) {
            TileEntity te = worldObj.getTileEntity(coord.x, coord.y, coord.z);
            if (te != null) {
                if (te instanceof FuelProvider && (provider = (FuelProvider) te).canProvide()) {
                    providers.add(provider);
                }
            }
        }
        provider = new ItemFuelProvider(values.getFuel());
        if (provider.canProvide()) {
            providers.add(provider);
        }
        Collections.sort(providers, fuelSorter);
        return providers;
    }

    private boolean cook() {
        ++this.furnaceCookTime;

        if (this.furnaceCookTime >= getSpeedMultiplier()) {
            this.furnaceCookTime = 0;
            this.smeltItem();
            return true;
        } else {
            return false;
        }
    }

    private boolean canSmelt(ItemStack input, ItemStack result, ItemStack output) {
        if (input == null || result == null) {
            return false;
        } else if (output == null) {
            return true;
        } else if (!output.isItemEqual(result)) {
            return false;
        } else {
            //The size below would be if the smeltingMultiplier = 1
            //If the smelting multiplier is > 1,
            //there is no guarantee that all potential operations will be completed.
            int minStackSize = output.stackSize + result.stackSize;
            return (minStackSize <= getInventoryStackLimit() && minStackSize <= result.getMaxStackSize());
        }
    }

    private void smeltItem() {
            values.checkInventorySlots();
            Doublet<Integer, Integer> smeltCount = smeltCountAndSmeltSize();
            if (smeltCount != null && smeltCount.getSecond() > 0) {
                ItemStack recipeResult = recipe(values.getInput());
                values.getInput().stackSize -= smeltCount.getFirst();
                if (values.getOutput() == null) {
                    recipeResult = recipeResult.copy();
                    recipeResult.stackSize = smeltCount.getSecond();
                    values.setOutput(recipeResult);
                } else {
                    values.getOutput().stackSize += smeltCount.getSecond();
                }
            }
            values.checkInventorySlots();
    }

    private Doublet<Integer, Integer> smeltCountAndSmeltSize() {
        ItemStack input = values.getInput();
        if (input == null) {
            return null;
        }
        ItemStack output = values.getOutput();
        ItemStack recipeResult = recipe(input);
        if (recipeResult == null) {
            return null;
        } else if (output != null && !output.isItemEqual(recipeResult)) {
            return null;
        } else if (output == null) {
            output = recipeResult.copy();
            output.stackSize = 0;
        }

        input = input.copy();

        int recipeStackSize = recipeResult.stackSize > 0 ? recipeResult.stackSize : 1;

        int outMax =
                getInventoryStackLimit() < output.getMaxStackSize()
                        ? output.getMaxStackSize()
                        : getInventoryStackLimit();
        int outAvailable = outMax - output.stackSize;
        int smeltAvailable = values.getSmeltingMultiplier() * recipeStackSize;
        int inAvailable = input.stackSize * recipeStackSize;

        int avail;
        int count;

        if (smeltAvailable < inAvailable) {
            avail = smeltAvailable;
            count = values.getSmeltingMultiplier();
        } else {
            avail = inAvailable;
            count = input.stackSize;
        }

        if (avail > outAvailable) {
            //If there is a remainder, this results in a difference from just outputting outAvailable
            count = outAvailable / recipeStackSize;
            avail = count * recipeStackSize;
        }

        values.checkInventorySlots();

        return new Doublet(count, avail);
    }

    public int scaledBurnTime(double fuelValue) {
        if (values.getEfficiency() > 1) {
            return (int) Math.round(fuelValue * values.getEfficiency());
        } else if (values.getEfficiency() > -1) {
            return 1;
        } else {
            return (int) Math.round(fuelValue / Math.abs(values.getEfficiency()));
        }
    }

    public boolean isBurning() {
        return furnaceBurnTime > 0;
    }

    private double getSpeedMultiplier() {
        double speed = values.getSpeed();
        if (speed > 1) {
            return this.cookSpeed / speed;
        } else if (speed > -1) {
            return cookSpeed * 2;
        } else {
            return this.cookSpeed -1 * speed;
        }
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int scaleVal) {
        double scale = getSpeedMultiplier();
        return (int) (this.furnaceCookTime * scaleVal / scale);
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int scaleVal) {
        if (currentItemBurnTime == 0) {
            currentItemBurnTime = (int) getSpeedMultiplier();
        }
        return furnaceBurnTime * scaleVal / this.currentItemBurnTime;
    }
    /**
     * ***************************************************************************************************************
     * *************************************************  Tile Methods  ************************************************
     * ****************************************************************************************************************
     */

    //Furnace stuff
    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            updateMultiblock();
        }
        doFurnaceWork();
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        values.resetInventory();
        values.unsetValues();
        super.readFromNBT(tagCompound);

        NBTTagList itemsTag = tagCompound.getTagList("Items", 10);

        for (int i = 0; i < itemsTag.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = itemsTag.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < getSizeInventory()) {
                values.setStackInSlot(b0, ItemStack.loadItemStackFromNBT(nbttagcompound1));
            }
        }

        this.isDirty = tagCompound.getBoolean("isDirty");
        this.furnaceBurnTime = tagCompound.getShort("BurnTime");
        this.furnaceCookTime = tagCompound.getShort("CookTime");
        values.readFromNBT(tagCompound);
//        this.currentItemBurnTime = this.scaledBurnTime();
        cube.readFromNBT(tagCompound);
        if (tagCompound.hasKey("CustomName")) {
            this.custom_name = tagCompound.getString("CustomName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setBoolean("isDirty", isDirty);
        tagCompound.setShort("BurnTime", (short) this.furnaceBurnTime);
        tagCompound.setShort("CookTime", (short) this.furnaceCookTime);

        NBTTagList itemsList = new NBTTagList();

        for (int i = 0; i < getSizeInventory(); ++i) {
            ItemStack is = getStackInSlot(i);
            if (is != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                is.writeToNBT(nbttagcompound1);
                itemsList.appendTag(nbttagcompound1);
            }
        }
        tagCompound.setTag("Items", itemsList);
        values.writeToNBT(tagCompound);
        cube.writeToNBT(tagCompound);
        if (this.hasCustomInventoryName()) {
            tagCompound.setString("CustomName", this.custom_name);
        }
    }

    /**
     * **************************************************************************************************************
     * ********************************************** Inventory methods ***********************************************
     * ***************************************************************************************************************
     */

    @Override
    public int getSizeInventory() {
        return 3;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        ItemStack temp = null;
        if (slot == 0) {
            temp = values.getInput();
        } else if (slot == 1) {
            temp = values.getFuel();
        } else if (slot == 2) {
            temp = values.getOutput();
        }
        return temp;
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        ItemStack is = getStackInSlot(slot);
        if (is != null) {
            is = is.splitStack(count);
            values.checkInventorySlots();
            return is;
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack is = getStackInSlot(slot);
        values.setStackInSlot(slot, null);
        return is;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        if (slot == 0) {
            values.setInput(itemStack);
        } else if (slot == 1) {
            values.setFuel(itemStack);
        } else if (slot == 2) {
            values.setOutput(itemStack);
        }

        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
            itemStack.stackSize = getInventoryStackLimit();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }

    //Re-writen for the I/O block
    @Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
        if (par1 == 2)
            return false;
        if (par1 == 1 && isItemFuel(par2ItemStack))
            return true;
        return par1 == 0;
    }

    @Override
    public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3) {
        return this.isItemValidForSlot(par1, par2ItemStack);
    }

    @Override
    public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3) {
        return par3 != 0 || par1 != 1 || par2ItemStack.getItem() == Items.bucket;
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    public boolean hasCustomInventoryName() {
        return this.custom_name != null && this.custom_name.length() > 0;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    public void func_145951_a(String p_145951_1_) {
        this.custom_name = p_145951_1_;
    }

    /**
     * ***************************************************************************************************************
     * ************************************************  Helper Methods  ***********************************************
     * ****************************************************************************************************************
     */

    protected boolean isItemFuel(ItemStack par0ItemStack) {
        return getItemBurnTime(par0ItemStack) > 0;
    }

    public void setGuiDisplayName(String par1Str) {
        this.custom_name = par1Str;
    }

    public void expelItems() {
        expelItem(values.getInput());
        values.setInput(null);
        expelItem(values.getOutput());
        values.setOutput(null);
        expelItem(values.getFuel());
        values.setFuel(null);
    }

    private void expelItem(ItemStack itemstack) {
        if (itemstack != null) {
            EntityItem entityitem =
                    new EntityItem(
                            worldObj,
                            (double) xCoord + random.nextFloat() * 0.8F + 0.1F,
                            (double) yCoord + random.nextFloat() * 0.8F + 0.1F,
                            (double) zCoord + random.nextFloat() * 0.8F + 0.1F,
                            itemstack
                    );

            if (itemstack.hasTagCompound()) {
                entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
            }

            float f3 = 0.05F;
            entityitem.motionX = (double) ((float) this.random.nextGaussian() * f3);
            entityitem.motionY = (double) ((float) this.random.nextGaussian() * f3 + 0.2F);
            entityitem.motionZ = (double) ((float) this.random.nextGaussian() * f3);
            worldObj.spawnEntityInWorld(entityitem);
        }
    }

    /*
     * Called to force update of block
     */
    public void update() {
        worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    /**
     * Sees if there is a crafting upgrade
     *
     * @return True if upgrade is present
     */
    public boolean hasCraftingUpgrade() {
        WorldFunction func = new HasCraftingUpgrade();
        LocalBlockCollections.searchCuboidMultiBlock(worldObj, xCoord, yCoord, zCoord, func, getMaxSize());
        return !func.shouldContinue();
    }

    public int getFurnaceBurnTime() {
        return furnaceBurnTime;
    }

    public int getCurrentItemBurnTime() {
        return currentItemBurnTime;
    }

    public int getFurnaceCookTime() {
        return furnaceCookTime;
    }

    public int getCookSpeed() {
        return cookSpeed;
    }

    public void setDirty() {
        isDirty = true;
    }

    public FueledRecipeTile getCore() {
        return this;
    }

    @Override
    public void setCore(FueledRecipeTile tile) {
    }

    public void setFurnaceBurnTime(int furnaceBurnTime) {
        this.furnaceBurnTime = furnaceBurnTime;
    }

    public void setCurrentItemBurnTime(int currentItemBurnTime) {
        this.currentItemBurnTime = currentItemBurnTime;
    }

    public void setFurnaceCookTime(int furnaceCookTime) {
        this.furnaceCookTime = furnaceCookTime;
    }


    public double getSpeed() {
        return values.getSpeed();
    }

    public double getGuiSpeed() {
        return Math.max(0.01, getSpeed());
    }

    public double getEfficiency() {
        return values.getEfficiency();
    }

    public double getGuiEfficiency() {
        return Math.max(0.01, getEfficiency());
    }

    public int getSmeltingMultiplier() {
        return values.getSmeltingMultiplier();
    }

    private static final class FuelSorter implements Comparator<FuelProvider> {
        @Override
        public int compare(FuelProvider o1, FuelProvider o2) {
            if (o1.canProvide() && !o2.canProvide()) {
                return 1;
            } else if (o2.canProvide() && !o1.canProvide()) {
                return -1;
            } else if (o1.type() == o2.type()) {
                return Double.compare(o1.fuelProvided(), o2.fuelProvided());
            } else {
                return Integer.compare(o1.type().sortValue, o2.type().sortValue);
            }
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }
    }
}
