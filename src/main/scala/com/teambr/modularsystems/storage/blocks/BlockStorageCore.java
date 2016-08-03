package com.teambr.modularsystems.storage.blocks;

import com.teambr.bookshelf.Bookshelf;
import com.teambr.bookshelf.common.blocks.properties.Properties;
import com.teambr.bookshelf.common.blocks.traits.DropsItems;
import com.teambr.bookshelf.common.blocks.traits.FourWayRotation;
import com.teambr.bookshelf.common.tiles.traits.Inventory;
import com.teambr.bookshelf.common.tiles.traits.OpensGui;
import com.teambr.bookshelf.util.WorldUtils;
import com.teambr.modularsystems.core.ModularSystems;
import com.teambr.modularsystems.core.lib.Reference;
import com.teambr.modularsystems.storage.container.ContainerStorageCore;
import com.teambr.modularsystems.storage.gui.GuiStorageCore;
import com.teambr.modularsystems.storage.tiles.TileStorageCore;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.asm.transformers.ItemStackTransformer;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis "pauljoda"
 * @since 3/27/2016
 */
public class BlockStorageCore extends BlockContainer implements OpensGui {

    public BlockStorageCore() {
        super(Material.WOOD);
        setUnlocalizedName(Reference.MOD_ID() + ":storageCore");
        setCreativeTab(ModularSystems.tabModularSystems());
        setHardness(1.5F);
    }

    /**
     * Called when the block is placed, we check which way the player is facing and put our value as the opposite of that
     */
    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing facing,
                                     float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        int playerFacingDirection = placer == null ? 0 : MathHelper.floor_double((placer.rotationYaw / 90.0F) + 0.5D) & 3;
        EnumFacing enumFacing = EnumFacing.getHorizontal(playerFacingDirection).getOpposite();
        return this.getDefaultState().withProperty(Properties.FOUR_WAY(), enumFacing);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileStorageCore storageCore = (TileStorageCore) worldIn.getTileEntity(pos);

        if(storageCore != null) {
            for (ItemStack stack : storageCore.getInventory().keySet()) {
                int amount = (int) storageCore.getInventory().get(stack);
                for (int i = 0; i < amount / stack.getMaxStackSize(); i++) { // Loop for all instances over one stack
                    ItemStack toDrop = stack.copy();
                    toDrop.stackSize = stack.getMaxStackSize();
                    WorldUtils.dropStack(worldIn, toDrop, pos);
                }
                ItemStack dropStack = stack.copy(); // Drop what is left
                dropStack.stackSize = amount % stack.getMaxStackSize();
                WorldUtils.dropStack(worldIn, dropStack, pos);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

    /**
     * Used to say what our block state is
     */
    @Override
    public BlockStateContainer createBlockState()  {
        BlockStateContainer.Builder stateBuilder = new BlockStateContainer.Builder(this);
        stateBuilder.add(Properties.FOUR_WAY());
        return stateBuilder.build();
    }

    /**
     * Used to convert the meta to state
     *
     * @param meta The meta
     * @return
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        if(meta == 0 || meta == 1)
            meta = EnumFacing.SOUTH.ordinal();
        return getDefaultState().withProperty(Properties.FOUR_WAY(), EnumFacing.getFront(meta));
    }

    /**
     * Called to convert state from meta
     *
     * @param state The state
     * @return
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(Properties.FOUR_WAY()).getIndex();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileStorageCore();
    }

    /**
     * Set us to use a model, BlockContainer seems to set to invisible to allow TESRS, but we just use models
     * @param state The incoming state, not needed for this
     * @return The render type
     */
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    /*******************************************************************************************************************
     * OpensGui                                                                                                        *
     *******************************************************************************************************************/

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, ItemStack heldItem, EnumFacing side,
                                    float hitX, float hitY, float hitZ) {
        playerIn.openGui(Bookshelf.getInstance(), 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerStorageCore(player.inventory, (TileStorageCore) world.getTileEntity(new BlockPos(x, y, z)));
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GuiStorageCore(player, (TileStorageCore) world.getTileEntity(new BlockPos(x, y, z)));
    }
}
