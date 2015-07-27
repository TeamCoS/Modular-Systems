package com.pauljoda.modularsystems.power.tiles;

import com.pauljoda.modularsystems.core.tiles.DummyTile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Modular-Systems
 * Created by Dyonovan on 24/07/15
 */
public class TileSupplierBase extends DummyTile {

    /*
     * Waila Methods
     */
    @Override
    public NBTTagCompound returnNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
        if (tag.hasKey("Energy")) {
            tag.removeTag("Energy");
            tag.removeTag("MaxStorage");
        }
        return tag;
    }
}
