package com.teamcos.modularsystems.oreprocessing.blocks;

import com.teamcos.modularsystems.oreprocessing.tiles.TileEntitySmelteryDummy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class BlockSmelteryDummyIO extends BlockBasicSmelteryDummy
{
    int lastSlot = 4;
    public BlockSmelteryDummyIO()
    {
        super(Material.rock, true);

        setBlockName("modularsystems:blockSmelteryIO");
        setStepSound(Block.soundTypeStone);
        setHardness(3.5f);

    }
    public int meta = 0;

    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("dispenser_front_vertical");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        TileEntitySmelteryDummy dummy = (TileEntitySmelteryDummy)world.getTileEntity(x, y, z);
        if(player.isSneaking())
        {
            if(dummy.slot == 4)
                dummy.slot = 0;

            dummy.slot = dummy.slot + 1;
            if(dummy.slot == 3)
                dummy.slot = 0;

            this.displaySlot(world, x, y, z, player);

            return false;
        }

        return super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
    }

    private void displaySlot(World world, int x, int y, int z, EntityPlayer player)
    {
        TileEntitySmelteryDummy dummy = (TileEntitySmelteryDummy)world.getTileEntity(x, y, z);

        if(lastSlot == 4)
            lastSlot = dummy.slot;

        if(lastSlot != dummy.slot)
        {
            switch(dummy.slot)
            {
                case 0 :  player.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Slot is Fuel"));
                    lastSlot = dummy.slot;
                    break;

                case 1 :  player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_AQUA + "Slot is Input"));
                    lastSlot = dummy.slot;
                    break;

                case 2 : player.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "Slot is Output"));
                    lastSlot = dummy.slot;
                    break;

                default : break;
            }
        }
    }
}
