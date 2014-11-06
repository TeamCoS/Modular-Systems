package com.teamcos.modularsystems.utilities.tiles;

import com.teamcos.modularsystems.fuelprovider.FuelProvider;
import com.teamcos.modularsystems.helpers.LiquidHelper;
import com.teamcos.modularsystems.helpers.LocalBlockCollections;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TankLogic extends DummyTile implements IFluidHandler, FuelProvider
{
    public FluidTank tank;
    public Fluid lastFluid;
    public int renderOffset;
    public double transferOffset;
    private ForgeDirection fillingFrom = ForgeDirection.UNKNOWN;

    public TankLogic()
    {
        tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 4);
    }

    @Override
    public int fill (ForgeDirection from, FluidStack resource, boolean doFill)
    {
        int amount = tank.fill(resource, doFill);
        fillingFrom = from;
        lastFluid = resource.getFluid();
        if (amount > 0 && doFill)
        {
            renderOffset = resource.amount;
            transferOffset = resource.amount;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }

        return amount;
    }

    @Override
    public FluidStack drain (ForgeDirection from, int maxDrain, boolean doDrain)
    {
        FluidStack amount = tank.drain(maxDrain, doDrain);
        if (amount != null && doDrain)
        {
            renderOffset = -maxDrain;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
        return amount;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (tank.getFluidAmount() == 0)
            return null;
        if (tank.getFluid().getFluid() != resource.getFluid())
            return null;

        // same fluid, k
        return this.drain(from, resource.amount, doDrain);
    }

    @Override
    public boolean canFill (ForgeDirection from, Fluid fluid)
    {
        return tank.getFluid() == null || tank.getFluid().getFluid() == fluid;
    }

    @Override
    public boolean canDrain (ForgeDirection from, Fluid fluid)
    {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo (ForgeDirection from)
    {
        FluidStack fluid = null;
        if (tank.getFluid() != null)
            fluid = tank.getFluid().copy();
        return new FluidTankInfo[] { new FluidTankInfo(fluid, tank.getCapacity()) };
    }

    public ForgeDirection getDirectionFillingFrom()
    {
        return fillingFrom;
    }

    public boolean isFilling()
    {
        return renderOffset > 0 || transferOffset > 0;
    }

    public Fluid getFillingLiquid() {
        return lastFluid;
    }

    public float getFluidAmountScaled ()
    {
        return (float) (tank.getFluid().amount - renderOffset) / (float) (tank.getCapacity() * 1.01F);
    }

    public double getTransferAmountScaled() {
        return Math.min(0.3, (transferOffset / 10) * 0.3);
    }

    public boolean containsFluid ()
    {
        return tank.getFluid() != null;
    }

    public int getBrightness ()
    {
        if (containsFluid())
        {
            return (tank.getFluid().getFluid().getLuminosity() * tank.getFluidAmount()) / tank.getCapacity();
        }
        return 0;
    }

    @Override
    public void readFromNBT (NBTTagCompound tags)
    {
        super.readFromNBT(tags);
        readCustomNBT(tags);
    }

    @Override
    public void writeToNBT (NBTTagCompound tags)
    {
        super.writeToNBT(tags);
        writeCustomNBT(tags);
    }

    public void readCustomNBT (NBTTagCompound tags)
    {
        if (tags.getBoolean("hasFluid"))
        {
            if (tags.getInteger("itemID") != 0)
            {
                tank.setFluid(new FluidStack(tags.getInteger("itemID"), tags.getInteger("amount")));
            }
            else
            {
                tank.setFluid(FluidRegistry.getFluidStack(tags.getString("fluidName"), tags.getInteger("amount")));
            }
        }
        else
            tank.setFluid(null);
    }

    public void writeCustomNBT (NBTTagCompound tags)
    {
        FluidStack liquid = tank.getFluid();
        tags.setBoolean("hasFluid", liquid != null);
        if (liquid != null)
        {
            tags.setString("fluidName", liquid.getFluid().getName());
            tags.setInteger("amount", liquid.amount);
        }
    }

    /* Packets */
    @Override
    public Packet getDescriptionPacket ()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeCustomNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket (NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        readCustomNBT(packet.func_148857_g());
        worldObj.func_147479_m(xCoord, yCoord, zCoord);
    }

    /* Updating */
    @Override
    public boolean canUpdate ()
    {
        return true;
    }

    @Override
    public void updateEntity ()
    {
        if (renderOffset > 0)
        {
            renderOffset -= 8;
            worldObj.func_147479_m(xCoord, yCoord, zCoord);
        }

        if(transferOffset > 0) {
            transferOffset -= 0.2;
            worldObj.func_147479_m(xCoord, yCoord, zCoord);
        }

        distributeFluids();
    }

    public void distributeFluids() {
        if (containsFluid()) {
            if (getTileInDirection(ForgeDirection.DOWN) instanceof TankLogic) {
                TankLogic otherTank = (TankLogic) getTileInDirection(ForgeDirection.DOWN);
                int capacity = getCapacity(otherTank);
                int drainAmount = drainAmount();
                if (canFill(otherTank) && capacity > 0) {
                    FluidStack drained = new FluidStack(tank.getFluid().getFluid(), drainAmount);
                    drain(ForgeDirection.UNKNOWN, drained, true);
                    otherTank.fill(ForgeDirection.UP, drained, true);
                    return;
                }
            }
            for (ForgeDirection dir : LocalBlockCollections.getNeighborDirections()) {
                TileEntity tile = getTileInDirection(dir);
                if (tile instanceof TankLogic && containsFluid()) {
                    TankLogic otherTank = (TankLogic) tile;
                    int capacity = getCapacity(otherTank);
                    int drainAmount = drainAmount(otherTank);
                    if (canFill(otherTank) && capacity > 0 && drainAmount > 0) {
                        FluidStack drained = new FluidStack(tank.getFluid().getFluid(), drainAmount);
                        otherTank.fill(dir, drained, true);
                        drain(ForgeDirection.UNKNOWN, drained, true);
                    }
                }
            }
        }
    }

    public boolean canFill(TankLogic otherTank) {
        return otherTank.canFill(ForgeDirection.UNKNOWN, tank.getFluid().getFluid());
    }

    public static boolean hasCapacity(TankLogic otherTank) {
        return otherTank.tank.getFluidAmount() < otherTank.tank.getCapacity();
    }

    public int drainRate() {
        return Math.max(10000 / tank.getFluid().getFluid().getViscosity(), 5);
    }

    private int drainAmount() {
        return Math.min(drainRate(), tank.getFluidAmount());
    }

    public static int getCapacity(TankLogic otherTank) {
        return otherTank.tank.getCapacity() - otherTank.tank.getFluidAmount();
    }

    public int drainAmount(TankLogic otherTank) {
        return Math.min(drainAmount(), this.tank.getFluidAmount() - otherTank.tank.getFluidAmount());
    }


    public int comparatorStrength ()
    {
        return 15 * tank.getFluidAmount() / tank.getCapacity();
    }

    @Override
    public boolean canProvide() {
        return containsFluid() && fuelProvided() > 0 && tank.getFluidAmount() >= 400;
    }

    @Override
    public double fuelProvided() {
        return LiquidHelper.getLiquidBurnTime(tank.getFluid());
    }

    @Override
    public double consume() {
        if (canProvide()) {
            drain(ForgeDirection.UNKNOWN, 400, true);
            return LiquidHelper.getLiquidBurnTime(tank.getFluid());
        } else {
            return 0;
        }
    }

    @Override
    public FuelProviderType type() {
        return FuelProviderType.LIQUID;
    }
}
