package com.teamcos.modularsystems.utilities.block;

import com.teamcos.modularsystems.core.ModularSystems;
import com.teamcos.modularsystems.core.lib.Reference;
import com.teamcos.modularsystems.core.proxy.ClientProxy;
import com.teamcos.modularsystems.utilities.tiles.DummyTile;
import com.teamcos.modularsystems.utilities.tiles.FueledRecipeTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class DummyBlock extends BlockContainer {

    public int meta = 0;

    public DummyBlock(Material material, boolean inTab) {
        super(material);
        if (inTab) setCreativeTab(ModularSystems.tabModularSystems);
        setBlockName("modularsystems:blockSmelteryDummy");
        setStepSound(Block.soundTypeStone);
        setHardness(3.5f);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        DummyTile dummy = (DummyTile) world.getTileEntity(x, y, z);

        FueledRecipeTile core = dummy.getCore();

        if (core != null) {
            core.setDirty();
        }

        Block block = dummy.getBlock();
        int meta = dummy.getMetadata();

        if (world.isAirBlock(x, y, z)) {
            float f = Reference.random.nextFloat() * 0.8F + 0.1F;
            float f1 = Reference.random.nextFloat() * 0.8F + 0.1F;
            float f2 = Reference.random.nextFloat() * 0.8F + 0.1F;

            EntityItem entityitem = new EntityItem(
                    world,
                    (double) x + f,
                    (double) y + f1,
                    (double) z + f2,
                    new ItemStack(block, 1, meta)
            );

            world.spawnEntityInWorld(entityitem);
        }
        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (player.isSneaking()) return false;

        DummyTile dummy = (DummyTile) world.getTileEntity(x, y, z);

        if (dummy != null && dummy.getCore() != null) {
            FueledRecipeTile core = dummy.getCore();
            return core.getBlockType().onBlockActivated(world, core.xCoord, core.yCoord, core.zCoord, player, par6, par7, par8, par9);
        } else {
            return true;
        }
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canRenderInPass(int pass) {
        //Set the static var in the client proxy
        ClientProxy.renderPass = pass;
        //the block can render in both passes, so return true always
        return true;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return null;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon("cobblestone");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new DummyTile();
    }
}
