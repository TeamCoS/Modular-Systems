package com.pauljoda.modularsystems.power.blocks;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.blocks.BlockDummy;
import com.pauljoda.modularsystems.core.renderers.SpecialDummyRenderer;
import com.pauljoda.modularsystems.core.tiles.AbstractCore;
import com.pauljoda.modularsystems.core.tiles.DummyTile;
import com.pauljoda.modularsystems.power.tiles.TilePowerBankBase;
import com.teambr.bookshelf.Bookshelf;
import com.teambr.bookshelf.collections.BlockTextures;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class BlockPower extends BlockDummy {
    String name;

    public BlockPower(String name, Class<? extends TileEntity> tile) {
        super(Material.rock, name, tile);

        setCreativeTab(ModularSystems.tabModularSystems);
        setHardness(3.5f);
        this.name = name;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if(!player.isSneaking()) {
            DummyTile us = (DummyTile)world.getTileEntity(x, y, z);
            if(us.getCore() != null)
                player.openGui(Bookshelf.instance, 0, world, us.getCore().xCoord, us.getCore().yCoord, us.getCore().zCoord);
            return true;
        } else if (player.isSneaking()) {
            TilePowerBankBase us = (TilePowerBankBase) world.getTileEntity(x, y, z);
            if (us != null) {
                player.openGui(Bookshelf.instance, 0, world, x, y, z);
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        DummyTile dummy = (DummyTile) world.getTileEntity(x, y, z);
        AbstractCore core = dummy.getCore();

        if (core != null) {
            core.setDirty();
        }

        dropItems(world, x, y, z);
    }

    @Override
    public void dropItems(World world, int x, int y, int z) {
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

    @Override
    public Item getItemDropped(int i, Random rand, int j) {
        return Item.getItemFromBlock(Block.getBlockFromName(name));
    }

    /**
     * Used to add the textures for the blocks. Uses blocks name by default
     *
     * Initialize the {@link BlockTextures} object here
     * @param iconRegister Icon Registry
     */
    public void generateDefaultTextures(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("minecraft:iron_block");
        textures = new BlockTextures(iconRegister, "minecraft:iron_block");
        textures.setOverlay(iconRegister.registerIcon("minecraft:hopper_top"));
    }

    @Override
    public int getRenderType() {
        return SpecialDummyRenderer.renderID;
    }
}
