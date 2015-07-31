package com.pauljoda.modularsystems.core.blocks;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.tiles.DummyRedstoneInput;
import com.pauljoda.modularsystems.core.tiles.DummyRedstoneOutput;
import com.pauljoda.modularsystems.core.tiles.DummyTile;
import com.teambr.bookshelf.Bookshelf;
import com.teambr.bookshelf.collections.BlockTextures;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/30/2015
 */
public class BlockRedstoneControl extends BlockDummy {
    /**
     * Creates the dummy block
     *
     * @param mat  The material of the dummy
     * @param name The unlocalized name of the dummy
     * @param tile The tile entity associated with this block
     */
    public BlockRedstoneControl(Material mat, String name, Class<? extends TileEntity> tile) {
        super(mat, name, tile);
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return ModularSystems.tabModularSystems;
    }

    /**
     * Used to add the textures for the blocks. Uses blocks name by default
     *
     * Initialize the {@link BlockTextures} object here
     * @param iconRegister Icon Registry
     */
    public void generateDefaultTextures(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("minecraft:redstone_block");
        this.furnaceOverlay = iconRegister.registerIcon(Reference.MOD_ID + ":furnaceOverlay");
        this.crusherOverlay = iconRegister.registerIcon(Reference.MOD_ID + ":crusherOverlay");

        textures = new BlockTextures(iconRegister, "minecraft:redstone_block");
        textures.setOverlay(iconRegister.registerIcon(this.blockName));
    }

    /**
     * Called when the blocks is activated. We are using it to open our GUI
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        DummyTile us = (DummyTile)world.getTileEntity(x, y, z);

        if(!player.isSneaking()) {
            if(us.getCore() != null)
                player.openGui(Bookshelf.instance, 0, world, us.getCore().xCoord, us.getCore().yCoord, us.getCore().zCoord);
            return true;
        } else {
            if(us.getCore() != null && us instanceof DummyRedstoneInput)
                player.openGui(Bookshelf.instance, 0, world, us.xCoord, us.yCoord, us.zCoord);
        }
        return true;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        if(!(world.getTileEntity(x, y, z) instanceof DummyRedstoneInput) && world.getTileEntity(x, y, z) instanceof DummyRedstoneOutput) {
            DummyRedstoneOutput output = (DummyRedstoneOutput)world.getTileEntity(x, y, z);
            if(output != null && output.getCore() != null) {
                return output.getCore().getRedstoneOutput();
            }
        }
        return 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
        if(!(world.getTileEntity(x, y, z) instanceof DummyRedstoneInput) && world.getTileEntity(x, y, z) instanceof DummyRedstoneOutput) {
            DummyRedstoneOutput output = (DummyRedstoneOutput)world.getTileEntity(x, y, z);
            if(output != null && output.getCore() != null) {
                return output.getCore().getRedstoneOutput();
            }
        }
        return 0;
    }
}
