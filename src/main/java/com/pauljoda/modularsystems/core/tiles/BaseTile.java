package com.pauljoda.modularsystems.core.tiles;

import com.pauljoda.modularsystems.core.collections.Location;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BaseTile extends TileEntity {

    /**
     * Used to get the current location of the tile
     * @return A new instance of this tile's location
     */
    public Location getLocation() {
        return new Location(this.xCoord, this.yCoord, this.zCoord);
    }

    /**
     * Checks if this block is recieving redstone
     * @return True if has power
     */
    public boolean isPowered() {
        return isPoweringTo(worldObj, xCoord, yCoord + 1, zCoord, 0) ||
                isPoweringTo(worldObj, xCoord, yCoord - 1, zCoord, 1) ||
                isPoweringTo(worldObj, xCoord, yCoord, zCoord + 1, 2) ||
                isPoweringTo(worldObj, xCoord, yCoord, zCoord - 1, 3) ||
                isPoweringTo(worldObj, xCoord + 1, yCoord, zCoord, 4) ||
                isPoweringTo(worldObj, xCoord - 1, yCoord, zCoord, 5);
    }

    /**
     * Tests if the block is providing a redstone signal
     * @param world The World
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param side Which side of the block
     * @return True if is providing
     */
    public static boolean isPoweringTo(World world, int x, int y, int z, int side) {
        return world.getBlock(x, y, z).isProvidingWeakPower(world, x, y, z, side) > 0;
    }

    /**
     * Gets the tile in the direction provided
     * @param direction The {@link net.minecraftforge.common.util.ForgeDirection} of the way to check
     * @return The instance of the tile in that direction
     */
    public TileEntity getTileInDirection(ForgeDirection direction) {
        int x = xCoord + direction.offsetX;
        int y = yCoord + direction.offsetY;
        int z = zCoord + direction.offsetZ;

        if (worldObj != null && worldObj.blockExists(x, y, z)) { return worldObj.getTileEntity(x, y, z); }
        return null;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }
}
