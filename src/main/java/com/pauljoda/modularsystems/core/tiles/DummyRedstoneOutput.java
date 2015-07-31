package com.pauljoda.modularsystems.core.tiles;

import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.teambr.bookshelf.helpers.GuiHelper;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/30/2015
 */
public class DummyRedstoneOutput extends DummyTile {

    public Block getStoredBlock() {
        return BlockManager.redstoneControlOut;
    }


    @Override
    public void updateEntity() {
        super.updateEntity();
        if(worldObj.rand.nextInt(20) == 0)
            for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
                worldObj.notifyBlockOfNeighborChange(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, getStoredBlock());
    }

    /*******************************************************************************************************************
     ************************************************ Waila ************************************************************
     *******************************************************************************************************************/

    @Override
    public void returnWailaHead(List<String> tip) {
        tip.add(GuiHelper.GuiColor.YELLOW + "Redstone Level: " + GuiHelper.GuiColor.WHITE + worldObj.getBlock(xCoord, yCoord, zCoord).isProvidingWeakPower(worldObj, xCoord, yCoord, zCoord, 0));
    }
}
