package com.teamcos.modularsystems.utilities.block;

import com.teamcos.modularsystems.helpers.LiquidHelper;
import com.teamcos.modularsystems.interfaces.MSUpgradeBlock;
import com.teamcos.modularsystems.notification.GuiColor;
import com.teamcos.modularsystems.notification.Notification;
import com.teamcos.modularsystems.notification.NotificationHelper;
import com.teamcos.modularsystems.registries.FurnaceConfigHandler;
import com.teamcos.modularsystems.renderers.TankRenderer;
import com.teamcos.modularsystems.utilities.tiles.DummyTile;
import com.teamcos.modularsystems.utilities.tiles.FueledRecipeTile;
import com.teamcos.modularsystems.utilities.tiles.TankLogic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import java.util.Random;

public class BlockTank extends BlockContainer implements MSUpgradeBlock {

    public BlockTank(CreativeTabs tab)
    {
        super(Material.rock);
        setHardness(3.0F);
        setResistance(20F);
        setCreativeTab(tab);
        setBlockName("modularsystems:blockTank");
    }

    @Override
    public int getRenderBlockPass ()
    {
        return 1;
    }

    @Override
    public boolean isOpaqueCube ()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock ()
    {
        return false;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon("hopper_top");
    }

    @Override
    public boolean shouldSideBeRendered (IBlockAccess world, int x, int y, int z, int side)
    {
        Block bID = world.getBlock(x, y, z);
        return bID == this ? false : super.shouldSideBeRendered(world, x, y, z, side);
    }

    @Override
    public boolean canRenderInPass (int pass)
    {
        TankRenderer.renderPass = pass;
        return true;
    }

    @Override
    public int getLightValue (IBlockAccess world, int x, int y, int z)
    {
        TileEntity logic = world.getTileEntity(x, y, z);
        if (logic != null && logic instanceof TankLogic)
            return ((TankLogic) logic).getBrightness();
        return 0;
    }

    @Override
    public int getRenderType ()
    {
        return TankRenderer.tankModelID;
    }

    @Override
    public TileEntity createTileEntity (World world, int metadata)
    {
        return new TankLogic();
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        ItemStack current = entityplayer.inventory.getCurrentItem();
        if (current != null)
        {
            FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(current);
            TankLogic logic = (TankLogic) world.getTileEntity(x, y, z);
            if (liquid != null)
            {
                int amount = logic.fill(ForgeDirection.UNKNOWN, liquid, false);
                if (amount == liquid.amount)
                {
                    logic.fill(ForgeDirection.UNKNOWN, liquid, true);
                    if (!entityplayer.capabilities.isCreativeMode)
                        entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, consumeItem(current));
                    return true;
                }
                else
                    return true;
            }
            else if (FluidContainerRegistry.isBucket(current))
            {
                FluidTankInfo[] tanks = logic.getTankInfo(ForgeDirection.UNKNOWN);
                FluidStack fillFluid = tanks[0].fluid;// getFluid();
                ItemStack fillStack = FluidContainerRegistry.fillFluidContainer(fillFluid, current);
                if (fillStack != null)
                {
                    logic.drain(ForgeDirection.UNKNOWN, FluidContainerRegistry.getFluidForFilledItem(fillStack).amount, true);
                    if (!entityplayer.capabilities.isCreativeMode)
                    {
                        if (current.stackSize == 1)
                        {
                            entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, fillStack);
                        }
                        else
                        {
                            entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, consumeItem(current));

                            if (!entityplayer.inventory.addItemStackToInventory(fillStack))
                            {
                                entityplayer.dropPlayerItemWithRandomChoice(fillStack, false);
                            }
                        }
                    }
                    return true;
                }
            }
        } else {
            if (entityplayer.isSneaking()) {
                TankLogic tankLogic = (TankLogic)world.getTileEntity(x, y, z);
                if(world.isRemote)
                    NotificationHelper.addNotification(new Notification(new ItemStack(this), GuiColor.TURQUISE + "Fuel: " + FluidRegistry.getFluidName(tankLogic.tank.getFluid()), GuiColor.ORANGE + "" + tankLogic.tank.getFluidAmount() + " / " + tankLogic.tank.getCapacity() + "mb", Notification.DEFAULT_DURATION));
                return true;
            }

            DummyTile dummy = (DummyTile) world.getTileEntity(x, y, z);
            FueledRecipeTile core;

            if (dummy != null && (core = dummy.getCore()) != null) {
                return core.getBlockType().onBlockActivated(world, core.xCoord, core.yCoord, core.zCoord, entityplayer, par6, par7, par8, par9);
            } else {
                return true;
            }
        }

        return false;
    }

    public static ItemStack consumeItem (ItemStack stack)
    {
        if (stack.stackSize == 1)
        {
            if (stack.getItem().hasContainerItem())
                return stack.getItem().getContainerItem(stack);
            else
                return null;
        }
        else
        {
            stack.splitStack(1);

            return stack;
        }
    }

    @Override
    public TileEntity createNewTileEntity (World world, int test)
    {
        return createTileEntity(world, 0);
    }

    @Override
    public int damageDropped (int meta)
    {
        return meta;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
    {
        DummyTile dummy = (DummyTile) world.getTileEntity(x, y, z);

        FueledRecipeTile core = dummy.getCore();

        if (core != null) {
            core.setDirty();
        }

        int meta = world.getBlockMetadata(x, y, z);
        ItemStack stack = new ItemStack(this, 1, meta);
        TankLogic logic = (TankLogic) world.getTileEntity(x, y, z);
        FluidStack liquid = logic.tank.getFluid();
        if (liquid != null)
        {
            NBTTagCompound tag = new NBTTagCompound();
            NBTTagCompound liquidTag = new NBTTagCompound();
            liquid.writeToNBT(liquidTag);
            tag.setTag("Fluid", liquidTag);
            stack.setTagCompound(tag);
        }
        dropTankBlock(world, x, y, z, stack);

        world.setBlockToAir(x, y, z);
        world.markBlockForUpdate(x, y, z);
        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return null;
    }

    protected void dropTankBlock (World world, int x, int y, int z, ItemStack stack)
    {
        if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops"))
        {
            float f = 0.7F;
            double d0 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            double d1 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            double d2 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, (double) x + d0, (double) y + d1, (double) z + d2, stack);
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);
        }
    }

    @Override
    public void onBlockPlacedBy (World world, int x, int y, int z, EntityLivingBase living, ItemStack stack)
    {
        super.onBlockPlacedBy(world, x, y, z, living, stack);
        if (stack.hasTagCompound())
        {
            NBTTagCompound liquidTag = stack.getTagCompound().getCompoundTag("Fluid");
            if (liquidTag != null)
            {
                FluidStack liquid = FluidStack.loadFluidStackFromNBT(liquidTag);
                TankLogic logic = (TankLogic) world.getTileEntity(x, y, z);
                logic.tank.setFluid(liquid);
            }
        }
    }

    @Override
    public boolean hasComparatorInputOverride ()
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride (World world, int x, int y, int z, int comparatorSide)
    {
        return getTankLogic(world, x, y, z).comparatorStrength();
    }

    public static TankLogic getTankLogic (IBlockAccess blockAccess, int par1, int par2, int par3)
    {
        return (TankLogic) blockAccess.getTileEntity(par1, par2, par3);
    }

    @Override
    public double getEfficiency(World world, int x, int y, int z, int blockCount) {
        TankLogic tank = (TankLogic)world.getTileEntity(x, y, z);
        if(tank != null) {
            if (tank.tank.getFluid() != null) {
                System.out.println(LiquidHelper.getLiquidBurnTime(tank.tank.getFluid()) / tank.tank.getFluid().getFluid().getViscosity());
                return LiquidHelper.getLiquidBurnTime(tank.tank.getFluid()) / tank.tank.getFluid().getFluid().getViscosity();
            }
        }
        return 0;
    }

    @Override
    public double getSpeed(World world, int x, int y, int z, int blockCount) {
        return FurnaceConfigHandler.getSpeedMultiplierForBlock(this, blockCount);
    }

    @Override
    public int getMultiplier(World world, int x, int y, int z, int blockCount) {
        return FurnaceConfigHandler.getSmeltingMultiplierForBlock(this, blockCount);
    }

    @Override
    public boolean isCrafter() {
        return false;
    }
}
