package com.pauljoda.modularsystems.furnace.blocks;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
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

public class BlockFurnaceCore extends BaseBlock {

    private static boolean isChanging;
    private final boolean active;

    public BlockFurnaceCore(boolean isActive) {
        super(Material.rock, Reference.MOD_ID + ":furnaceCore" + (isActive ? "Active" : ""), TileEntityFurnaceCore.class);
        active = isActive;
        //Need to pull this back out because active has now been set
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
        textures.setFront(iconRegister.registerIcon(active ? "minecraft:furnace_front_on" : "minecraft:furnace_front_off"));
        textures.setTop(iconRegister.registerIcon("minecraft:furnace_top"));
        textures.setBottom(iconRegister.registerIcon("minecraft:furnace_top"));
        textures.setOverlay(iconRegister.registerIcon("minecraft:hopper_top"));
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return active ? null : ModularSystems.tabModularSystems;
    }

    /**
     * Update which blocks the furnace is using depending on whether or not it is burning
     */
    public static void updateFurnaceBlockState(boolean active, World world, int x, int y, int z) {
        int l = world.getBlockMetadata(x, y, z);
        TileEntity tileentity = world.getTileEntity(x, y, z);
        isChanging = true;

        if (active) {
            world.setBlock(x, y, z, BlockManager.furnaceCoreActive);
        } else {
            world.setBlock(x, y, z, BlockManager.furnaceCore);
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
        TileEntityFurnaceCore tileEntity = (TileEntityFurnaceCore) world.getTileEntity(x, y, z);
        if (player.isSneaking()) {
            return false;
        }
        if (tileEntity != null) {

            if (tileEntity.wellFormed && !world.isRemote) {
                player.openGui(Bookshelf.instance, 0, world, x, y, z);
            } else
                tileEntity.setDirty();
        }
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        if(!isChanging) {
            TileEntityFurnaceCore core = (TileEntityFurnaceCore)world.getTileEntity(x, y, z);

            if(core != null) {
                core.expelItems();
                core.breakMultiBlock();
            }
            world.func_147453_f(x, y, z, par5);
        }
    }

    @Override
    public void dropItems(World world, int x, int y, int z) {}

    public Item getItemDropped(int i, Random rand, int j) {
        return Item.getItemFromBlock(BlockManager.furnaceCore);
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        if (this.active) {
            ForgeDirection l = getDefaultRotation().convertMetaToDirection(world.getBlockMetadata(x, y, z));
            float f = (float) x + 0.5F;
            float f1 = (float) y + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
            float f2 = (float) z + 0.5F;
            float f3 = 0.52F;
            float f4 = rand.nextFloat() * 0.6F - 0.3F;

            if (l == ForgeDirection.WEST) {
                world.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == ForgeDirection.EAST) {
                world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == ForgeDirection.NORTH) {
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
            } else if (l == ForgeDirection.SOUTH) {
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    /**
     * If this returns true, then comparators facing away from this blocks will use the value from
     * getComparatorInputOverride instead of the actual redstone signal strength.
     */
    public boolean hasComparatorInputOverride() {
        return true;
    }

    /**
     * If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
     * strength when this blocks inputs to a comparator.
     */
    public int getComparatorInputOverride(World world, int x, int y, int z, int meta) {
        return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(x, y, z));
    }
}
