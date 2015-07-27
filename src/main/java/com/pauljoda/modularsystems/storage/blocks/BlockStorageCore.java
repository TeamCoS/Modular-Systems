package com.pauljoda.modularsystems.storage.blocks;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.storage.tiles.TileStorageCore;
import com.teambr.bookshelf.collections.BlockTextures;
import com.teambr.bookshelf.common.blocks.BaseBlock;
import com.teambr.bookshelf.common.blocks.rotation.FourWayRotation;
import com.teambr.bookshelf.common.blocks.rotation.IRotation;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/23/2015
 */
public class BlockStorageCore extends BaseBlock {

    public BlockStorageCore() {
        super(Material.wood, Reference.MOD_ID + ":storageCore", TileStorageCore.class);
    }

    @Override
    public IRotation getDefaultRotation() {
        return new FourWayRotation();
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return ModularSystems.tabModularSystems;
    }

    @Override
    public void generateDefaultTextures(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(Reference.MOD_ID + ":storageCoreSide");
        textures = new BlockTextures(iconRegister, Reference.MOD_ID + ":storageCoreSide");
        textures.setFront(iconRegister.registerIcon(Reference.MOD_ID + ":storageCoreFront"));
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase livingBase, ItemStack itemStack) {
        super.onBlockPlacedBy(world, x, y, z, livingBase, itemStack);
        TileStorageCore tile = (TileStorageCore)world.getTileEntity(x, y, z);

        if(itemStack.hasDisplayName()) {
            tile.setCustomName(itemStack.getDisplayName());
        }
        if(livingBase instanceof EntityPlayer)
            tile.setOwner((EntityPlayer)livingBase);
    }

    /**
     * Empty Contents of an Inventory into world on Block Break
     * @param world
     * @param x X-Coord in world
     * @param y Y-Coord in world
     * @param z Z-Coord in world
     */
    public void dropItems(World world, int x, int y, int z) {
        TileStorageCore tile = (TileStorageCore)world.getTileEntity(x, y, z);
        tile.destroyNetwork();
        Random rand = new Random();

        for (int i = 0; i < tile.getCraftingGrid().getSizeInventory(); i++) {
            ItemStack itemStack = tile.getCraftingGrid().getStackInSlot(i);

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
        super.dropItems(world, x, y, z);
    }
}