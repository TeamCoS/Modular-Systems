package com.pauljoda.modularsystems.power.blocks;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.blocks.BlockDummy;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.renderers.SpecialDummyRenderer;
import com.pauljoda.modularsystems.core.tiles.AbstractCore;
import com.pauljoda.modularsystems.core.tiles.DummyTile;
import com.pauljoda.modularsystems.power.tiles.TileBankBase;
import com.teambr.bookshelf.Bookshelf;
import com.teambr.bookshelf.client.itemtooltip.IItemTooltip;
import com.teambr.bookshelf.collections.BlockTextures;
import com.teambr.bookshelf.helpers.GuiHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BlockPower extends BlockDummy implements IItemTooltip {
    String name;
    @SideOnly(Side.CLIENT)
    public IIcon providerIcon, bankIcon;

    public BlockPower(String name, Class<? extends TileEntity> tile) {
        super(Material.rock, name, tile);

        setCreativeTab(ModularSystems.tabModularSystems);
        setHardness(3.5f);
        this.name = name;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (!player.isSneaking()) {
            DummyTile us = (DummyTile) world.getTileEntity(x, y, z);
            if (us.getCore() != null)
                player.openGui(Bookshelf.instance, 0, world, us.getCore().xCoord, us.getCore().yCoord, us.getCore().zCoord);
            return true;
        } else {
            TileEntity us = world.getTileEntity(x, y, z);
            if (us != null && us instanceof TileBankBase) {
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
     * <p/>
     * Initialize the {@link BlockTextures} object here
     *
     * @param iconRegister Icon Registry
     */
    public void generateDefaultTextures(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("minecraft:iron_block");
        textures = new BlockTextures(iconRegister, "minecraft:iron_block");
        textures.setOverlay(iconRegister.registerIcon("minecraft:hopper_top"));
        this.providerIcon = iconRegister.registerIcon(Reference.MOD_ID + ":supplierCenter");
        this.bankIcon = iconRegister.registerIcon(Reference.MOD_ID + ":bankCenter");
    }

    @Override
    public int getRenderType() {
        return SpecialDummyRenderer.renderID;
    }

    /*
     * Item Tooltips
     */
    @Override
    public List<String> returnTooltip() {
        List<String> list = new ArrayList<>();
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            list.add(GuiHelper.GuiColor.CYAN + GuiHelper.GuiTextFormat.ITALICS.toString() + "Useable In:");
            String type = Objects.equals(name.substring(15, 20), "power") ? "power" : Objects.equals(name.substring(15, 23), "supplier") ? "supplier" : "none";
            switch (type) {
                case "power":
                    list.add(GuiHelper.GuiColor.GREEN + "Modular Furnace");
                    list.add(GuiHelper.GuiColor.GREEN + "Modular Crusher");
                    list.add(GuiHelper.GuiColor.GREEN + "Modular Generator");
                    break;
                case "supplier":
                    list.add(GuiHelper.GuiColor.GREEN + "Modular Generator");
                    break;
                default:
            }
        } else
            list.add(GuiHelper.GuiColor.CYAN + GuiHelper.GuiTextFormat.ITALICS.toString() + "Press Shift for Usage Cores");
        return list;
    }
}
