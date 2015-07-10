package com.pauljoda.modularsystems.core.tiles;

import com.pauljoda.modularsystems.core.functions.BlockCountFunction;
import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.pauljoda.modularsystems.core.providers.FuelProvider;
import com.pauljoda.modularsystems.core.providers.ItemFuelProvider;
import com.pauljoda.modularsystems.furnace.collections.StandardValues;
import com.teambr.bookshelf.collections.Couplet;
import com.teambr.bookshelf.collections.Location;
import com.teambr.bookshelf.common.blocks.BaseBlock;
import com.teambr.bookshelf.common.tiles.BaseTile;
import com.teambr.bookshelf.util.WorldUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCore extends BaseTile implements ISidedInventory {
    protected StandardValues values;
    protected Couplet<Location, Location> corners;
    private static final int cookSpeed = 200;
    private boolean isDirty = true;
    public boolean wellFormed = false;
    private static final int MAX_SIZE = 50;

    protected int[] sides = new int[] { 1 };
    protected int[] top = new int[] { 0 };
    protected int[] bottom = new int[] { 2 };

    public AbstractCore() {
        values = new StandardValues();
    }

    /**
     * Used to set the blocks to its active and non-active state
     * @param positiveBurnTime True if active
     * @param world World object
     * @param x X Coord
     * @param y Y Coord
     * @param z Z Coord
     */
    protected abstract void updateBlockState(boolean positiveBurnTime, World world, int x, int y, int z);

    /**
     * Get the output of the recipe
     * @param is The input
     * @return The output
     */
    protected abstract ItemStack recipe(ItemStack is);

    /**
     * Get how long the item will burn, 0 if it won't
     * @param stack The stack to check
     * @return How many ticks it burns
     */
    protected abstract int getItemBurnTime(ItemStack stack);


    /**
     * Check if this blocks is not allowed in the structure
     * @param block The blocks to check
     * @param meta The meta data of said blocks
     * @return True if it is banned
     */
    protected abstract boolean isBlockBanned(Block block, int meta);

    /**
     * Take the blocks in this structure and generate the speed etc values
     * @param function The blocks count function
     */
    protected abstract void generateValues(BlockCountFunction function);


    /*******************************************************************************************************************
     *******************************************  Multiblock Methods  **************************************************
     *******************************************************************************************************************/

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

    protected boolean isWellFormed() {
        wellFormed = false;

        Couplet<Location, Location> test = getCorners();

        if(test == null)
            return false;

        corners = getCorners();


        List<Location> outside = corners.getFirst().getAllWithinBounds(corners.getSecond(), false, true);
        List<Location> inside = corners.getFirst().getAllWithinBounds(corners.getSecond(), true, false);

        for(Location loc : outside) {
            //Us silly
            if(loc.equals(getLocation()))
                continue;
            if(worldObj.isAirBlock(loc.x, loc.y, loc.z) ||
                    isBlockBanned(worldObj.getBlock(loc.x, loc.y, loc.z), worldObj.getBlockMetadata(loc.x, loc.y, loc.z)))
                return false;
        }

        //Tells if is against wall
        if(inside.size() <= 0)
            return false;

        for(Location loc : inside) {
            if(!worldObj.isAirBlock(loc.x, loc.y, loc.z))
                return false;
        }
        wellFormed = true;
        return wellFormed;
    }

    protected void buildMultiblock() {
        //Just to be safe, though it shouldn't ever be null
        if(corners == null)
            return;

        List<Location> outside = corners.getFirst().getAllWithinBounds(corners.getSecond(), false, true);
        BlockCountFunction blockCount = new BlockCountFunction();
        for(Location loc : outside) {
            //Don't convert us!
            if(loc.equals(getLocation()))
                continue;

            //Make sure we aren't already formed
            if(worldObj.getTileEntity(loc.x, loc.y, loc.z) instanceof DummyTile)
                continue;

            blockCount.addBlock(worldObj.getBlock(loc.x, loc.y, loc.z), worldObj.getBlockMetadata(loc.x, loc.y, loc.z));

            int id = Block.getIdFromBlock(worldObj.getBlock(loc.x, loc.y, loc.z));
            int meta = worldObj.getBlockMetadata(loc.x, loc.y, loc.z);
            worldObj.setBlock(loc.x, loc.y, loc.z, BlockManager.dummy);
            DummyTile dummy = (DummyTile)worldObj.getTileEntity(loc.x, loc.y, loc.z);
            dummy.setCore(this);
            dummy.setBlock(id);
            dummy.setMetadata(meta);
        }
        generateValues(blockCount);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void breakMultiBlock() {
        deconstructMultiblock();
    }

    protected void deconstructMultiblock() {
        //Just to be safe, though it shouldn't ever be null
        if(corners == null)
            return;
        values.resetStructureValues();

        List<Location> outside = corners.getFirst().getAllWithinBounds(corners.getSecond(), false, true);
        for(Location loc : outside) {
            //Don't convert us!
            if(loc.equals(getLocation()))
                continue;

            //Just to be safe
            if(!(worldObj.getTileEntity(loc.x, loc.y, loc.z) instanceof DummyTile))
                continue;

            DummyTile dummy = (DummyTile)worldObj.getTileEntity(loc.x, loc.y, loc.z);
            int meta = dummy.getMetadata();
            worldObj.setBlock(loc.x, loc.y, loc.z, dummy.getStoredBlock());
            worldObj.setBlockMetadataWithNotify(loc.x, loc.y, loc.z, meta, 2);
        }
        wellFormed = false;
    }

    protected Couplet<Location, Location> getCorners() {
        Location local = getLocation();
        Location firstCorner = local.createNew();
        Location secondCorner = local.createNew();

        ForgeDirection dir = ((BaseBlock)worldObj.getBlock(xCoord, yCoord, zCoord)).getDefaultRotation().convertMetaToDirection(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));

        //Move inside
        firstCorner.travel(dir.getOpposite());
        secondCorner.travel(dir.getOpposite());

        //Get our directions
        ForgeDirection right = WorldUtils.rotateRight(dir);
        ForgeDirection left = WorldUtils.rotateLeft(dir);

        //Get first corner

        //Find side
        while(worldObj.isAirBlock(firstCorner.x, firstCorner.y, firstCorner.z)) {
            firstCorner.travel(right);
            if(getLocation().findDistance(firstCorner) > MAX_SIZE)
                return null;
        }

        //Pop back inside
        firstCorner.travel(right.getOpposite());

        //Find floor
        while(worldObj.isAirBlock(firstCorner.x, firstCorner.y, firstCorner.z)) {
            firstCorner.travel(ForgeDirection.DOWN);
            if(getLocation().findDistance(firstCorner) > MAX_SIZE)
                return null;
        }

        //Found, so move to physical location
        firstCorner.travel(right);
        firstCorner.travel(dir);

        //Find side
        while(worldObj.isAirBlock(secondCorner.x, secondCorner.y, secondCorner.z)) {
            secondCorner.travel(left);
            if(getLocation().findDistance(secondCorner) > MAX_SIZE)
                return null;
        }

        //Pop back inside
        secondCorner.travel(left.getOpposite());

        //Move To back
        while(worldObj.isAirBlock(secondCorner.x, secondCorner.y, secondCorner.z)) {
            secondCorner.travel(dir.getOpposite());
            if(getLocation().findDistance(secondCorner) > MAX_SIZE)
                return null;
        }

        //Pop back inside
        secondCorner.travel(dir);

        //Move Up
        while(worldObj.isAirBlock(secondCorner.x, secondCorner.y, secondCorner.z)) {
            secondCorner.travel(ForgeDirection.UP);
            if(getLocation().findDistance(secondCorner) > MAX_SIZE)
                return null;
        }

        //Found, so move to physical location
        secondCorner.travel(left);
        secondCorner.travel(dir.getOpposite());

        return new Couplet<>(firstCorner, secondCorner);
    }

    /******************************************************************************************************************
     ************************************************  Furnace Methods  ***********************************************
     ******************************************************************************************************************/

    protected void doWork() {
        boolean didWork = false;
        if (!worldObj.isRemote) {
            if (this.values.getBurnTime() > 0) {
                this.values.setBurnTime(values.getBurnTime() - 1);
            }

            if (canSmelt(values.getInput(), recipe(values.getInput()), values.getOutput())) {

                //Check the structure to make sure we have the right stuff
                if(corners == null)
                    corners = getCorners();

                //Still null?
                if(corners == null) {
                    markDirty();
                    return;
                }

                List<FuelProvider> providers = getFuelProviders(corners.getFirst().getAllWithinBounds(corners.getSecond(), false, true));
                if (this.values.getBurnTime() <= 0 && !providers.isEmpty()) {
                    int scaledBurnTime = getAdjustedBurnTime(providers.get(0).consume());
                    values.checkInventorySlots();
                    this.values.currentItemBurnTime = this.values.burnTime = scaledBurnTime;
                    cook();
                    didWork = true;
                } else if (isBurning()) {
                    didWork = cook();
                } else {
                    this.values.cookTime = 0;
                    this.values.burnTime = 0;
                    didWork = true;
                }
            } else if(worldObj.getBlock(xCoord, yCoord, zCoord) == BlockManager.furnaceCoreActive) {
                didWork = true;
            }

            if (didWork) {
                updateBlockState(this.values.burnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                markDirty();
            }
        }
    }

    protected List<FuelProvider> getFuelProviders(List<Location> coords) {
        List<FuelProvider> providers = new ArrayList<>();
        FuelProvider provider;
        for (Location coord : coords) {
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
        Collections.sort(providers, new FuelProvider.FuelSorter());
        return providers;
    }

    private boolean cook() {
        ++this.values.cookTime;

        if (this.values.cookTime >= getAdjustedCookTime()) {
            this.values.cookTime = 0;
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
        Couplet<Integer, Integer> smeltCount = smeltCountAndSmeltSize();
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

    private Couplet<Integer, Integer> smeltCountAndSmeltSize() {
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
        int smeltAvailable = (int)values.getMultiplicity() + recipeStackSize;
        int inAvailable = input.stackSize * recipeStackSize;

        int avail;
        int count;

        if (smeltAvailable < inAvailable) {
            avail = smeltAvailable;
            count = (int) (recipeStackSize + values.getMultiplicity());
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

        return new Couplet<>(count, avail);
    }

    public boolean isBurning() {
        return values.burnTime > 0;
    }


    public int getAdjustedBurnTime(double fuelValue) {
        return (int) Math.max(fuelValue + values.getEfficiency(), 1);
    }

    private double getAdjustedCookTime() {
        return Math.max(cookSpeed + values.getSpeed(), 1);
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int scaleVal) {
        return (int) ((this.values.cookTime * scaleVal) / Math.max(getAdjustedCookTime(), 0.001));
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int scaleVal) {
        return (int) ((values.burnTime * scaleVal) / Math.max(this.values.currentItemBurnTime, 0.001));
    }

    /**
     * ***************************************************************************************************************
     * *************************************************  Tile Methods  ************************************************
     * ****************************************************************************************************************
     */

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if(updateMultiblock())
                doWork();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        values.readFromNBT(tagCompound);
        this.isDirty = tagCompound.getBoolean("isDirty");

        Location first = new Location();
        Location second = new Location();
        first.readFromNBT(tagCompound, "First");
        second.readFromNBT(tagCompound, "Second");
        corners = new Couplet<>(first, second);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        values.writeToNBT(tagCompound);
        tagCompound.setBoolean("isDirty", isDirty);

        if(corners != null) {
            corners.getFirst().writeToNBT(tagCompound, "First");
            corners.getSecond().writeToNBT(tagCompound, "Second");
        }
    }

    /**
     * ****************************************************************************************************************
     * ********************************************** Inventory methods ***********************************************
     * ****************************************************************************************************************
     */

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return side == 0 ? bottom : side == 1 ? top : sides;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return this.isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return side != 0 || slot != 1 || stack.getItem() == Items.bucket;
    }

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
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (slot == 2)
            return false;
        if (slot == 1 && isItemFuel(stack))
            return true;
        return slot == 0;
    }

    /**
     * *****************************************************************************************************************
     * ************************************************  Helper Methods  ***********************************************
     * *****************************************************************************************************************
     */

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
                            (double) xCoord + worldObj.rand.nextFloat() * 0.8F + 0.1F,
                            (double) yCoord + worldObj.rand.nextFloat() * 0.8F + 0.1F,
                            (double) zCoord + worldObj.rand.nextFloat() * 0.8F + 0.1F,
                            itemstack
                    );

            if (itemstack.hasTagCompound()) {
                entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
            }

            float f3 = 0.05F;
            entityitem.motionX = (double) ((float) this.worldObj.rand.nextGaussian() * f3);
            entityitem.motionY = (double) ((float) this.worldObj.rand.nextGaussian() * f3 + 0.2F);
            entityitem.motionZ = (double) ((float) this.worldObj.rand.nextGaussian() * f3);
            worldObj.spawnEntityInWorld(entityitem);
        }
    }

    protected boolean isItemFuel(ItemStack par0ItemStack) {
        return getItemBurnTime(par0ItemStack) > 0;
    }

    public int getFurnaceBurnTime() {
        return values.getBurnTime();
    }

    public int getCurrentItemBurnTime() {
        return values.getCurrentItemBurnTime();
    }

    public int getFurnaceCookTime() {
        return values.getCookTime();
    }

    public void setFurnaceBurnTime(int furnaceBurnTime) {
        values.setBurnTime(furnaceBurnTime);
    }

    public void setCurrentItemBurnTime(int currentItemBurnTime) {
        values.setCurrentItemBurnTime(currentItemBurnTime);
    }

    public void setFurnaceCookTime(int furnaceCookTime) {
        values.setCookTime(furnaceCookTime);
    }

    public void setDirty() {
        isDirty = true;
    }

    public StandardValues getValues() {
        return values;
    }
}
