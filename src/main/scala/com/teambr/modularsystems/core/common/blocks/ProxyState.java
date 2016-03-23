package com.teambr.modularsystems.core.common.blocks;

import com.google.common.collect.ImmutableMap;
import com.teambr.modularsystems.core.common.tiles.TileProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since 1/17/2016
 */
public class ProxyState implements IBlockState {
    public TileProxy tile;
    public Block block;

    public ProxyState(TileProxy t, Block b) {
        tile = t;
        block = b;
    }

    @Override
    public Collection<IProperty<?>> getPropertyNames() {
        return null;
    }

    @Override
    public <T extends Comparable<T>> T getValue(IProperty<T> property) {
        return null;
    }

    @Override
    public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
        return null;
    }

    @Override
    public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> property) {
        return null;
    }

    @Override
    public ImmutableMap<IProperty<?>, Comparable<?>> getProperties() {
        return null;
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public Material getMaterial() {
        return block.getMaterial(this);
    }

    @Override
    public boolean isFullBlock() {
        return block.isFullBlock(this);
    }

    @Override
    public int getLightOpacity() {
        return block.getLightOpacity(this);
    }

    @Override
    public int getLightOpacity(IBlockAccess world, BlockPos pos) {
        return block.getLightOpacity(this, world, pos);
    }

    @Override
    public int getlightValue() {
        return block.getLightValue(this);
    }

    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos) {
        return block.getLightValue(this, world, pos);
    }

    @Override
    public boolean isTranslucent() {
        return block.isTranslucent(this);
    }

    @Override
    public boolean useNeighborBrightness() {
        return block.getUseNeighborBrightness(this);
    }

    @Override
    public MapColor getMapColor() {
        return block.getMapColor(this);
    }

    @Override
    public IBlockState withRotation(Rotation rot) {
        return block.withRotation(this, rot);
    }

    @Override
    public IBlockState withMirror(Mirror mirrorIn) {
        return block.withMirror(this, mirrorIn);
    }

    @Override
    public boolean isFullCube() {
        return block.isFullCube(this);
    }

    @Override
    public EnumBlockRenderType getRenderType() {
        return block.getRenderType(this);
    }

    @Override
    public int getPackedLightmapCoords(IBlockAccess source, BlockPos pos) {
        return block.getPackedLightmapCoords(this, source, pos);
    }

    @Override
    public float getAmbientOcclusionLightValue() {
        return block.getAmbientOcclusionLightValue(this);
    }

    @Override
    public boolean isBlockNormalCube() {
        return block.isBlockNormalCube(this);
    }

    @Override
    public boolean isNormalCube() {
        return block.isNormalCube(this);
    }

    @Override
    public boolean canProvidePower() {
        return block.canProvidePower(this);
    }

    @Override
    public int getWeakPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return block.getWeakPower(this, blockAccess, pos, side);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return block.hasComparatorInputOverride(this);
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return block.getComparatorInputOverride(this, worldIn, pos);
    }

    @Override
    public float getBlockHardness(World worldIn, BlockPos pos) {
        return block.getBlockHardness(this, worldIn, pos);
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World worldIn, BlockPos pos) {
        return block.getPlayerRelativeBlockHardness(this, player, worldIn, pos);
    }

    @Override
    public int getStrongPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return block.getStrongPower(this, blockAccess, pos, side);
    }

    @Override
    public EnumPushReaction getMobilityFlag() {
        return block.getMobilityFlag(this);
    }

    @Override
    public IBlockState getActualState(IBlockAccess blockAccess, BlockPos pos) {
        return block.getActualState(this, blockAccess, pos);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos) {
        return block.getCollisionBoundingBox(this, worldIn, pos);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, BlockPos pos, EnumFacing facing) {
        return block.shouldSideBeRendered(this, blockAccess, pos, facing);
    }

    @Override
    public boolean isOpaqueCube() {
        return block.isOpaqueCube(this);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        return block.getSelectedBoundingBox(this, worldIn, pos);
    }

    @Override
    public void addCollisionBoxToList(World worldIn, BlockPos pos, AxisAlignedBB p_185908_3_, List<AxisAlignedBB> p_185908_4_, Entity p_185908_5_) {
        block.addCollisionBoxToList(this, worldIn, pos, p_185908_3_, p_185908_4_, p_185908_5_);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockAccess blockAccess, BlockPos pos) {
        return block.getBoundingBox(this, blockAccess, pos);
    }

    @Override
    public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        return block.collisionRayTrace(this, worldIn, pos, start, end);
    }

    @Override
    public boolean isFullyOpaque() {
        return block.isFullyOpaque(this);
    }

    @Override
    public boolean doesSideBlockRendering(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return block.doesSideBlockRendering(this, world, pos, side);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return block.isSideSolid(this, world, pos, side);
    }
}
