package com.pauljoda.modularsystems.furnace.blocks;

import com.dyonovan.brlib.collections.BlockTextures;
import com.dyonovan.brlib.common.blocks.BaseBlock;
import com.dyonovan.brlib.common.blocks.rotation.FourWayRotation;
import com.dyonovan.brlib.common.blocks.rotation.IRotation;
import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class BlockFurnaceCore extends BaseBlock {

    private static boolean isChanging;
    private final boolean active;

    public BlockFurnaceCore(boolean isActive) {
        super(Material.rock, Reference.MOD_ID + ":furnaceCore" + (isActive ? "Active" : ""), TileEntityFurnaceCore.class);
        active = isActive;
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
     * Update which block the furnace is using depending on whether or not it is burning
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
        }
    }

    public Item getItemDropped(int i, Random rand, int j) {
        return Item.getItemFromBlock(BlockManager.furnaceCore);
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        if (this.active) {
            int l = world.getBlockMetadata(x, y, z);
            float f = (float) x + 0.5F;
            float f1 = (float) y + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
            float f2 = (float) z + 0.5F;
            float f3 = 0.52F;
            float f4 = rand.nextFloat() * 0.6F - 0.3F;

            if (l == 4) {
                world.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == 5) {
                world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == 2) {
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
            } else if (l == 3) {
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    /**
     * If this returns true, then comparators facing away from this block will use the value from
     * getComparatorInputOverride instead of the actual redstone signal strength.
     */
    public boolean hasComparatorInputOverride() {
        return true;
    }

    /**
     * If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
     * strength when this block inputs to a comparator.
     */
    public int getComparatorInputOverride(World world, int x, int y, int z, int meta) {
        return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(x, y, z));
    }
}
