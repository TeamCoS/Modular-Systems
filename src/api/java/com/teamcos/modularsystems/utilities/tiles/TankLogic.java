package com.teamcos.modularsystems.utilities.tiles;

import com.teamcos.modularsystems.fuelprovider.FuelProvider;
import com.teamcos.modularsystems.helpers.LiquidHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
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

    public void distributeFluids()
    {
        if(containsFluid()) {
            int drainRate = 10000 / tank.getFluid().getFluid().getViscosity();
            if (drainRate < 5)
                drainRate = 5;
            if (getTileInDirection(ForgeDirection.DOWN) instanceof TankLogic) {
                TankLogic otherTank = (TankLogic) getTileInDirection(ForgeDirection.DOWN);
                FluidStack drained = tank.getFluid().amount < drainRate ? tank.getFluid() : new FluidStack(tank.getFluid().getFluid(), drainRate);
                if (otherTank.canFill(ForgeDirection.UNKNOWN, tank.getFluid().getFluid()) && otherTank.tank.getFluidAmount() < otherTank.tank.getCapacity()) {
                    otherTank.fill(ForgeDirection.UP, drained, true);
                    drain(ForgeDirection.UNKNOWN, drained, true);
                    return;
                }
            }
            if(getTileInDirection(ForgeDirection.NORTH) instanceof TankLogic && containsFluid()) {
                TankLogic logic = (TankLogic) getTileInDirection(ForgeDirection.NORTH);
                FluidStack drained = tank.getFluid().amount < drainRate ? tank.getFluid() : new FluidStack(tank.getFluid().getFluid(), drainRate);
                if (logic.canFill(ForgeDirection.UNKNOWN, tank.getFluid().getFluid()) && logic.tank.getFluidAmount() < logic.tank.getCapacity() && logic.tank.getFluidAmount() + drainRate < tank.getFluidAmount()) {
                    logic.fill(ForgeDirection.NORTH, drained, true);
                    drain(ForgeDirection.UNKNOWN, drained, true);
                    return;
                }
            }
            if(getTileInDirection(ForgeDirection.SOUTH) instanceof TankLogic && containsFluid()) {
                TankLogic logic = (TankLogic) getTileInDirection(ForgeDirection.SOUTH);
                FluidStack drained = tank.getFluid().amount < drainRate ? tank.getFluid() : new FluidStack(tank.getFluid().getFluid(), drainRate);
                if (logic.canFill(ForgeDirection.UNKNOWN, tank.getFluid().getFluid()) && logic.tank.getFluidAmount() < logic.tank.getCapacity() && logic.tank.getFluidAmount() + drainRate < tank.getFluidAmount()) {
                    logic.fill(ForgeDirection.SOUTH, drained, true);
                    drain(ForgeDirection.UNKNOWN, drained, true);
                    return;
                }
            }
            if(getTileInDirection(ForgeDirection.EAST) instanceof TankLogic && containsFluid()) {
                TankLogic logic = (TankLogic) getTileInDirection(ForgeDirection.EAST);
                FluidStack drained = tank.getFluid().amount < drainRate ? tank.getFluid() : new FluidStack(tank.getFluid().getFluid(), drainRate);
                if (logic.canFill(ForgeDirection.UNKNOWN, tank.getFluid().getFluid()) && logic.tank.getFluidAmount() < logic.tank.getCapacity() && logic.tank.getFluidAmount() + drainRate < tank.getFluidAmount()) {
                    logic.fill(ForgeDirection.EAST, drained, true);
                    drain(ForgeDirection.UNKNOWN, drained, true);
                    return;
                }
            }
            if(getTileInDirection(ForgeDirection.WEST) instanceof TankLogic && containsFluid()) {
                TankLogic logic = (TankLogic) getTileInDirection(ForgeDirection.WEST);
                FluidStack drained = tank.getFluid().amount < drainRate ? tank.getFluid() : new FluidStack(tank.getFluid().getFluid(), drainRate);
                if (logic.canFill(ForgeDirection.UNKNOWN, tank.getFluid().getFluid()) && logic.tank.getFluidAmount() < logic.tank.getCapacity() && logic.tank.getFluidAmount() + drainRate < tank.getFluidAmount()) {
                    logic.fill(ForgeDirection.WEST, drained, true);
                    drain(ForgeDirection.UNKNOWN, drained, true);
                    return;
                }
            }
        }
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
