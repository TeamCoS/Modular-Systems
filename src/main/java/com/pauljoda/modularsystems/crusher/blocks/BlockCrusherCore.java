package com.pauljoda.modularsystems.crusher.blocks;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.pauljoda.modularsystems.crusher.tiles.TileCrusherCore;
import com.teambr.bookshelf.Bookshelf;
import com.teambr.bookshelf.collections.BlockTextures;
import com.teambr.bookshelf.common.blocks.BaseBlock;
import com.teambr.bookshelf.common.blocks.rotation.FourWayRotation;
import com.teambr.bookshelf.common.blocks.rotation.IRotation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class BlockCrusherCore extends BaseBlock {

    private final boolean active;
    private static boolean isChanging;

    public BlockCrusherCore(boolean isActive) {
        super(Material.rock, Reference.MOD_ID + ":crusherCore" + (isActive ? "Active" : ""), TileCrusherCore.class);
        active = isActive;
        setCreativeTab(getCreativeTab());
    }

    @Override
    public IRotation getDefaultRotation() {
        return new FourWayRotation();
    }

    @Override
    public void generateDefaultTextures(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("minecraft:furnace_side");
        textures = new BlockTextures(iconRegister, "minecraft:furnace_side");
        textures.setFront(iconRegister.registerIcon(active ? Reference.MOD_ID + ":crusherFront_On" : Reference.MOD_ID + ":crusherFront_off"));
        textures.setTop(iconRegister.registerIcon("minecraft:furnace_top"));
        textures.setBottom(iconRegister.registerIcon("minecraft:furnace_top"));
        textures.setOverlay(iconRegister.registerIcon("minecraft:hopper_top"));
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return active ? null : ModularSystems.tabModularSystems;
    }

    /**
     * Update which blocks the crusher is using depending on whether or not it is burning
     */
    public static void updateCrusherBlockState(boolean active, World world, int x, int y, int z) {
        int l = world.getBlockMetadata(x, y, z);
        TileEntity tileentity = world.getTileEntity(x, y, z);
        isChanging = true;

        if (active) {
            world.setBlock(x, y, z, BlockManager.crusherCoreActive);
        } else {
            world.setBlock(x, y, z, BlockManager.crusherCore);
        }

        isChanging = false;
        world.setBlockMetadataWithNotify(x, y, z, l, 2);

        if (tileentity != null) {
            tileentity.validate();
            world.setTileEntity(x, y, z, tileentity);
            world.markBlockForUpdate(x, y, z);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        TileCrusherCore tileEntity = (TileCrusherCore) world.getTileEntity(x, y, z);
        if (player.isSneaking()) {
            return false;
        }
        if (tileEntity != null) {
            tileEntity.setDirty();
            if (tileEntity.wellFormed) {
                player.openGui(Bookshelf.instance, 0, world, x, y, z);
            } else
                tileEntity.markDirty();
        }
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        if(!isChanging) {
            TileCrusherCore core = (TileCrusherCore)world.getTileEntity(x, y, z);

            if(core != null) {
                core.expelItems();
                core.breakMultiBlock();
            }
            world.func_147453_f(x, y, z, par5);
        }
    }

    @Override
    public void dropItems(World world, int x, int y, int z) {}

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        if (this.active) {
            ForgeDirection l = getDefaultRotation().convertMetaToDirection(world.getBlockMetadata(x, y, z));
            float f = (float) x + 0.5F;
            float f1 = (float) y + 0.3F + rand.nextFloat() * 6.0F / 16.0F;
            float f2 = (float) z + 0.5F;
            float f3 = 0.52F;
            float f4 = rand.nextFloat() * 0.6F - 0.3F;

            if (l == ForgeDirection.WEST) {
                world.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == ForgeDirection.EAST) {
                world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == ForgeDirection.NORTH) {
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
            } else if (l == ForgeDirection.SOUTH) {
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public Item getItemDropped(int i, Random rand, int j) {
        return Item.getItemFromBlock(BlockManager.crusherCore);
    }

    public boolean hasComparatorInputOverride() {
        return true;
    }

    public int getComparatorInputOverride(World world, int x, int y, int z, int meta) {
        return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(x, y, z));
    }
}
