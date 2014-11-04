package com.teamcos.modularsystems.functions;

import com.teamcos.modularsystems.helpers.Coord;
import com.teamcos.modularsystems.helpers.LiquidHelper;
import com.teamcos.modularsystems.utilities.tiles.TankLogic;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class FindLiquidFuelFunction implements WorldFunction{
    private Block target;
    private Coord targetCoord;
    private boolean blockFound = false;
    public FindLiquidFuelFunction(Block block) { target = block; }

    @Override
    public void outerBlock(World world, int x, int y, int z) {
        if(world.getBlock(x, y, z) == target) {
            TankLogic tank = (TankLogic)world.getTileEntity(x, y, z);
            if(tank.containsFluid()) {
                if(LiquidHelper.getLiquidBurnTime(tank.tank.getFluid()) > 0) {
                    targetCoord = new Coord(x, y, z);
                    blockFound = true;
                }
            }
        }
    }

    @Override
    public void innerBlock(World world, int x, int y, int z) {

    }

    @Override
    public boolean shouldContinue() {
        return !blockFound;
    }

    public Coord getTargetCoord()
    {
        return targetCoord;
    }

    @Override
    public void reset() {

    }

    @Override
    public WorldFunction copy() {
        return null;
    }

    @Override
    public void fail() {

    }
}
