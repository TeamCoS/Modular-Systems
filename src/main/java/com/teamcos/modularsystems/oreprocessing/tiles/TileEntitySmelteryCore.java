package com.teamcos.modularsystems.oreprocessing.tiles;

import com.teamcos.modularsystems.core.crafting.OreProcessingRecipies;
import com.teamcos.modularsystems.core.lib.Reference;
import com.teamcos.modularsystems.core.managers.BlockManager;
import com.teamcos.modularsystems.functions.BlockCountFunction;
import com.teamcos.modularsystems.functions.WorldFunction;
import com.teamcos.modularsystems.utilities.tiles.FueledRecipeTile;
import com.teamcos.modularsystems.oreprocessing.blocks.BlockSmelteryCore;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class TileEntitySmelteryCore extends FueledRecipeTile {

    //Automation related
    private static final int[] slots_top = new int[]{0};
    private static final int[] slots_bottom = new int[]{2, 1};
    private static final int[] slots_sides = new int[]{1};
    private static final int[] slots_output_only = new int[]{2};

    //Empty Constructor
    public TileEntitySmelteryCore() {
        super(new BlockCountWorldFunction());
    }

    public void updateBlockState(boolean positiveBurnTime, World world, int x, int y, int z) {
        BlockSmelteryCore.updateFurnaceBlockState(positiveBurnTime, world, x, y, z);
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

    @Override
    protected int getItemBurnTime(ItemStack is) {
        return is == null ? 0 : GameRegistry.getFuelValue(is);
    }

    @Override
    protected ItemStack recipe(ItemStack is) {
        return OreProcessingRecipies.getOutput(is);
    }

    //Reworked for I/O
    @Override
    public int[] getAccessibleSlotsFromSide(int par1)
    {
        switch (par1)
        {
            case 0:
                return Arrays.copyOf(slots_bottom, slots_bottom.length);
            case 1:
                return Arrays.copyOf(slots_top, slots_top.length);
            case 2:
                return Arrays.copyOf(slots_output_only, slots_output_only.length);
            default:
                return Arrays.copyOf(slots_sides, slots_sides.length);
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

    private static class ConvertDummiesWorldFunction implements WorldFunction {
        private BlockCountWorldFunction bcFunc = new BlockCountWorldFunction();
        private final TileEntitySmelteryCore core;

        private ConvertDummiesWorldFunction(TileEntitySmelteryCore core) {
            this.core = core;
        }

        @Override
        public void outerBlock(World world, int x, int y, int z) {

            bcFunc.outerBlock(world, x, y, z);
            if (!Reference.isModularTile(world.getBlock(x, y, z).getUnlocalizedName()) &&
                    world.getBlock(x, y, z) != null && world.getBlock(x, y, z) != Blocks.air) {
                Block icon = world.getBlock(x, y, z);
                int metadata = world.getBlockMetadata(x, y, z);

                world.setBlock(x, y, z, BlockManager.smelteryDummy);

                world.markBlockForUpdate(x, y, z);
                TileEntitySmelteryDummy dummyTE = (TileEntitySmelteryDummy) world.getTileEntity(x, y, z);

                if (icon != BlockManager.smelteryDummy)
                {
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
            bcFunc.clear();
        }

        @Override
        public WorldFunction copy() {
            return new ConvertDummiesWorldFunction(core);
        }

        public Map<Block, Integer> blockCounts() {
            return bcFunc.getBlockCounts();
        }
    }

    public static class BlockCountWorldFunction implements BlockCountFunction {
        private Map<Block, Integer> blocks = new LinkedHashMap<Block, Integer>();

        @Override
        public void outerBlock(World world, int x, int y, int z) {
            Block block = world.getBlock(x, y, z);
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity instanceof TileEntitySmelteryDummy) {
                TileEntitySmelteryDummy dummyTE = (TileEntitySmelteryDummy) tileEntity;
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

    private static class RevertDummiesWorldFunction implements WorldFunction {

        @Override
        public void outerBlock(World world, int x, int y, int z) {
            Block blockId = world.getBlock(x,y,z);
            if (blockId == BlockManager.smelteryDummy)
            {
                TileEntitySmelteryDummy dummyTE = (TileEntitySmelteryDummy) world.getTileEntity(x, y, z);

                world.setBlock(x, y, z, dummyTE.getBlock());
                world.setBlockMetadataWithNotify(x, y, z, dummyTE.metadata, 2);
            } else if (blockId == BlockManager.smelteryDummyIO) {
                TileEntitySmelteryDummy dummyTE = (TileEntitySmelteryDummy) world.getTileEntity(x, y, z);
                dummyTE.coreY = -100;
            }

            world.markBlockForUpdate(x, y, z);
        }

        @Override
        public void innerBlock(World world, int x, int y, int z) {

        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public WorldFunction copy() {
            return new RevertDummiesWorldFunction();
        }
    }

    private static class HasCraftingUpgrade implements WorldFunction {

        private boolean hasCrafting = false;

        @Override
        public void outerBlock(World world, int x, int y, int z) {}

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
