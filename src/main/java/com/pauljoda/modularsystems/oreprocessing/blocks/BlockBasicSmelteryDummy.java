package com.pauljoda.modularsystems.oreprocessing.blocks;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.proxy.ClientProxy;
import com.pauljoda.modularsystems.oreprocessing.tiles.TileEntitySmelteryCore;
import com.pauljoda.modularsystems.oreprocessing.tiles.TileEntitySmelteryDummy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBasicSmelteryDummy extends BlockContainer
{

    public BlockBasicSmelteryDummy(Material material, boolean inTab)
    {
        super(material);
        if(inTab)
            setCreativeTab(ModularSystems.tabModularSystems);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i)
    {
        return new TileEntitySmelteryDummy();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
    {
        TileEntitySmelteryDummy dummy = (TileEntitySmelteryDummy)world.getTileEntity(x, y, z);

        if(dummy.getCore() != null)
            dummy.getCore().isDirty = true;

        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if(player.isSneaking())
            return false;

        TileEntitySmelteryDummy dummy = (TileEntitySmelteryDummy)world.getTileEntity(x, y, z);

        if(dummy != null && dummy.getCore() != null)
        {
            TileEntitySmelteryCore core = dummy.getCore();
            return core.getBlockType().onBlockActivated(world, core.xCoord, core.yCoord, core.zCoord, player, par6, par7, par8, par9);
        }

        return true;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    @Override
    public int getRenderType()
    {
        return ClientProxy.smelteryDummyRenderType;
    }

    @Override
    public boolean canRenderInPass(int pass)
    {
        //Set the static var in the client proxy
        ClientProxy.renderPass = pass;
        //the block can render in both passes, so return true always
        return true;
    }
    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }
}