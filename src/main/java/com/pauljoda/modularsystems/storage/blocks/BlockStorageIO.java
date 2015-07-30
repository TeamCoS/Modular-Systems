package com.pauljoda.modularsystems.storage.blocks;

import com.pauljoda.modularsystems.core.lib.Reference;
import com.teambr.bookshelf.collections.BlockTextures;
import com.teambr.bookshelf.common.blocks.rotation.IRotation;
import com.teambr.bookshelf.common.blocks.rotation.SixWayRotation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/29/2015
 */
public class BlockStorageIO extends BlockStorageExpansion {
    /**
     * Used as a common class for all blocks. Makes things a bit easier
     *
     * @param mat  What material the block should be
     * @param name The unlocalized name of the block : Must be format "MODID:name"
     * @param tile Should the block have a tile, pass the class
     */
    public BlockStorageIO(Material mat, String name, Class<? extends TileEntity> tile) {
        super(mat, name, tile);
    }

    /**
     * Used to add the textures for the block. Uses block name by default
     *
     * Initialize the {@link BlockTextures} object here
     * @param iconRegister Icon Registry
     */
    public void generateDefaultTextures(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(Reference.MOD_ID + ":storageCapacity");
        textures = new BlockTextures(iconRegister, Reference.MOD_ID + ":storageCapacity");
        textures.setFront(iconRegister.registerIcon(Reference.MOD_ID + ":storageIO"));
        textures.setTop(iconRegister.registerIcon(Reference.MOD_ID + ":storageSide"));
        textures.setBottom(iconRegister.registerIcon(Reference.MOD_ID + ":storageSide"));
        textures.setLeft(iconRegister.registerIcon(Reference.MOD_ID + ":storageSide"));
        textures.setRight(iconRegister.registerIcon(Reference.MOD_ID + ":storageSide"));
        textures.setBack(iconRegister.registerIcon(Reference.MOD_ID + ":storageBack"));
        this.errorIcon = iconRegister.registerIcon(Reference.MOD_ID + ":storageNoConnection");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if(!world.isRemote && player.isSneaking()) {
            int meta = par6;

            ForgeDirection dir = getDefaultRotation().convertMetaToDirection(meta).getOpposite();
            meta = getDefaultRotation().convertDirectionToMeta(dir);

            if(world.getBlockMetadata(x, y, z) == meta)
                meta = getDefaultRotation().convertDirectionToMeta(dir.getOpposite());

            world.setBlockMetadataWithNotify(x, y, z, meta, 2);
            return true;
        }
        return super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
    }

        //The sides are: Bottom (0), Top (1), North (2), South (3), West (4), East (5).
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        switch(side) {
            case 0 :
                return textures.getDown(metadata, getDefaultRotation());
            case 1 :
                return textures.getUp(metadata, getDefaultRotation());
            case 2 :
                return textures.getNorth(metadata, getDefaultRotation());
            case 3 :
                return textures.getSouth(metadata, getDefaultRotation());
            case 4 :
                return textures.getWest(metadata, getDefaultRotation());
            case 5 :
                return textures.getEast(metadata, getDefaultRotation());
            default :
                return this.blockIcon;
        }
    }

    /**
     * Used to set the values needed for the block's rotation on placement
     * @return The rotation class needed, none by default
     */
    public IRotation getDefaultRotation() {
        return new SixWayRotation();
    }

    /**
     * Empty Contents of an Inventory into world on Block Break
     * @param world
     * @param x X-Coord in world
     * @param y Y-Coord in world
     * @param z Z-Coord in world
     */
    public void dropItems(World world, int x, int y, int z) {
        super.dropItems(world, x, y, z);
        Random rand = new Random();

        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (!(tileEntity instanceof IInventory))
            return;

        IInventory inventory = (IInventory) world.getTileEntity(x, y, z);
        if (inventory == null) return;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);

            if (itemStack != null && itemStack.stackSize > 0) {
                float rx = rand.nextFloat() * 0.8F + 0.1F;
                float ry = rand.nextFloat() * 0.8F + 0.1F;
                float rz = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world,
                        x + rx, y + ry, z + rz,
                        new ItemStack(itemStack.getItem(), itemStack.stackSize, itemStack.getItemDamage()));

                if (itemStack.hasTagCompound())
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);

                itemStack.stackSize = 0;
            }
        }
    }
}