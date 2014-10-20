package com.teamcos.modularsystems.furnace.tiles;

import com.teamcos.modularsystems.core.lib.Reference;
import com.teamcos.modularsystems.core.managers.BlockManager;
import com.teamcos.modularsystems.functions.BlockCountFunction;
import com.teamcos.modularsystems.functions.WorldFunction;
import com.teamcos.modularsystems.furnace.blocks.BlockFurnaceCore;
import com.teamcos.modularsystems.manager.ApiBlockManager;
import com.teamcos.modularsystems.utilities.tiles.FueledRecipeTile;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.LinkedHashMap;
import java.util.Map;

public class TileEntityFurnaceCore extends FueledRecipeTile {
    //Automation related
    private static final int[] slots_top = new int[]{0};
    private static final int[] slots_bottom = new int[]{2, 1};
    private static final int[] slots_sides = new int[]{1};
    private static final int[] slots_output_only = new int[]{2};

    //Empty Constructor
    public TileEntityFurnaceCore() {
        super(new BlockCountWorldFunction());
    }

    @Override
    protected void updateBlockState(boolean positiveBurnTime, World world, int x, int y, int z) {
        BlockFurnaceCore.updateFurnaceBlockState(positiveBurnTime, world, x, y, z);
    }

    @Override
    protected WorldFunction convertDummiesFunction() {
        return new ConvertDummiesWorldFunction(this);
    }

    @Override
    protected WorldFunction revertDummiesFunction() {
        return new RevertDummiesWorldFunction();
    }

    @Override
    protected WorldFunction craftingUpgradeFunction() {
        return new HasCraftingUpgrade();
    }

    protected int getItemBurnTime(ItemStack is) {
        return is == null ? 0 : GameRegistry.getFuelValue(is);
    }

    public void update() {
        worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    protected ItemStack recipe(ItemStack is) {
        return FurnaceRecipes.smelting().getSmeltingResult(is);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
    }

    //Reworked for I/O
    @Override
    public int[] getAccessibleSlotsFromSide(int par1) {
        switch (par1) {
            case 0:
                return slots_bottom;
            case 1:
                return slots_top;
            case 2:
                return slots_output_only;
            default:
                return slots_sides;
        }
    }

    @Override
    public World getWorld() {
        return worldObj;
    }

    @Override
    public int getX() {
        return xCoord;
    }

    @Override
    public int getY() {
        return yCoord;
    }

    @Override
    public int getZ() {
        return zCoord;
    }

    public static class BlockCountWorldFunction implements BlockCountFunction {
        private Map<Block, Integer> blocks = new LinkedHashMap<Block, Integer>();

        @Override
        public void outerBlock(World world, int x, int y, int z) {
            Block block = world.getBlock(x, y, z);
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity instanceof TileEntityFurnaceDummy) {
                TileEntityFurnaceDummy dummyTE = (TileEntityFurnaceDummy) tileEntity;
                block = dummyTE.getBlock();
            }
            GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(block);
            block = GameRegistry.findBlock(id.modId, id.name);
            Integer count = blocks.get(block);

            if (count == null) {
                count = 1;
            } else {
                count += 1;
            }
            blocks.put(block, count);
        }

        @Override
        public void innerBlock(World world, int x, int y, int z) {

        }

        @Override
        public boolean shouldContinue() {
            return true;
        }

        public Map<Block, Integer> getBlockCounts() {
            return blocks;
        }

        @Override
        public void clear() {
            blocks = new LinkedHashMap<Block, Integer>();
        }

        @Override
        public BlockCountFunction copy() {
            return new BlockCountWorldFunction();
        }
    }

    public static class RevertDummiesWorldFunction implements WorldFunction {
        @Override
        public void outerBlock(World world, int x, int y, int z) {
            Block blockId = world.getBlock(x, y, z);
            if (blockId == ApiBlockManager.dummyBlock) {
                TileEntityFurnaceDummy dummyTE = (TileEntityFurnaceDummy) world.getTileEntity(x, y, z);
                Block block = dummyTE.getBlock();
                world.setBlock(x, y, z, block);
                world.setBlockMetadataWithNotify(x, y, z, dummyTE.metadata, 2);
            } else if (blockId == BlockManager.furnaceCraftingUpgrade) {
                TileEntityFurnaceDummy dummyTE = (TileEntityFurnaceDummy) world.getTileEntity(x, y, z);
                dummyTE.coreY = -100;
            } else if (blockId == BlockManager.furnaceAddition) {
                TileEntityFurnaceDummy dummyTE = (TileEntityFurnaceDummy) world.getTileEntity(x, y, z);
                dummyTE.coreY = -100;
            } else if (blockId == ApiBlockManager.dummyIOBlock) {
                TileEntityFurnaceDummy dummyTE = (TileEntityFurnaceDummy) world.getTileEntity(x, y, z);
                dummyTE.coreY = -100;
            }

            world.markBlockForUpdate(x, y, z);
        }

        @Override
        public void innerBlock(World world, int x, int y, int z) {
        }

        @Override
        public boolean shouldContinue() {
            return true;
        }

        @Override
        public void clear() {}

        @Override
        public WorldFunction copy() {
            return new RevertDummiesWorldFunction();
        }
    }

    public static class ConvertDummiesWorldFunction implements WorldFunction {
        private BlockCountWorldFunction bcFunc = new BlockCountWorldFunction();
        private final TileEntityFurnaceCore core;

        public ConvertDummiesWorldFunction(TileEntityFurnaceCore core) {
            this.core = core;
        }

        @Override
        public void outerBlock(World world, int x, int y, int z) {

            bcFunc.outerBlock(world, x, y, z);

            if (!Reference.isModularTile(world.getBlock(x, y, z).getUnlocalizedName()) &&
                    world.getBlock(x, y, z) != null &&
                    world.getBlock(x, y, z) != Blocks.air) {

                Block icon = world.getBlock(x, y, z);
                int metadata = world.getBlockMetadata(x, y, z);
                world.setBlock(x, y, z, ApiBlockManager.dummyBlock);

                world.markBlockForUpdate(x, y, z);
                TileEntityFurnaceDummy dummyTE = (TileEntityFurnaceDummy) world.getTileEntity(x, y, z);

                if (icon == ApiBlockManager.dummyBlock) {
                    icon = Block.getBlockById(dummyTE.icon);
                    metadata = dummyTE.metadata;
                } else {
                    dummyTE.icon = Block.getIdFromBlock(icon);
                    dummyTE.metadata = metadata;
                }
                world.markBlockForUpdate(x, y, z);
                dummyTE.setCore(core);
            }

        }

        @Override
        public void innerBlock(World world, int x, int y, int z) {
            bcFunc.innerBlock(world, x, y, z);
        }

        @Override
        public boolean shouldContinue() {
            return true;
        }

        @Override
        public void clear() {
            this.bcFunc.clear();
        }

        @Override
        public WorldFunction copy() {
            return new ConvertDummiesWorldFunction(this.core);
        }

        public Map<Block, Integer> blockCounts() {
            return bcFunc.getBlockCounts();
        }
    }

    private static class HasCraftingUpgrade implements WorldFunction {

        private boolean hasCrafting = false;

        @Override
        public void outerBlock(World world, int x, int y, int z) {
            Block blockId = world.getBlock(x, y, z);
            hasCrafting = blockId == BlockManager.furnaceCraftingUpgrade;
        }

        @Override
        public void innerBlock(World world, int x, int y, int z) {

        }

        @Override
        public boolean shouldContinue() {
            return !hasCrafting;
        }

        @Override
        public void clear() {
            hasCrafting = false;
        }

        @Override
        public WorldFunction copy() {
            return new HasCraftingUpgrade();
        }
    }

    public double getSpeed() {
        return values.getSpeed();
    }

    public double getEfficiency() {
        return values.getEfficiency();
    }

    public int getSmeltingMultiplier() {
        return values.getSmeltingMultiplier();
    }

    public void checkInventorySlots() {
        values.checkInventorySlots();
    }
}
