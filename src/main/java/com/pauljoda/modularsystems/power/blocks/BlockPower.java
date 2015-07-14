package com.pauljoda.modularsystems.power.blocks;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.blocks.BlockDummy;
import com.pauljoda.modularsystems.core.renderers.SpecialDummyRenderer;
import com.pauljoda.modularsystems.core.tiles.AbstractCore;
import com.pauljoda.modularsystems.core.tiles.DummyTile;
import com.pauljoda.modularsystems.power.tiles.TilePowerBase;
import com.teambr.bookshelf.Bookshelf;
import com.teambr.bookshelf.collections.BlockTextures;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
            TilePowerBase us = (TilePowerBase) world.getTileEntity(x, y, z);
            if (us.getCore() != null) {
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
