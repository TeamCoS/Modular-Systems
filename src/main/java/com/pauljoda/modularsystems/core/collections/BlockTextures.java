package com.pauljoda.modularsystems.core.collections;

import com.pauljoda.modularsystems.core.blocks.rotation.IRotation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

/**
 * Used as a collection of textures. Makes it easier than manually finding the textures
 */
@SideOnly(Side.CLIENT)
public class BlockTextures {
    protected IIcon front, back, left, right, up, down;

    /**
     * Used to register and store icons for blocks that have no specific differences
     * @param register The IIconRegister
     * @param allTextures The string representation of the icon
     */
    public BlockTextures(IIconRegister register, String allTextures) {
        front = back = left = right = up = down = register.registerIcon(allTextures);
    }

    public IIcon getFront() {
        return front;
    }

    /**
     * Gets the icon for this position from the rotation
     * @param meta Block meta data
     * @param rotation Rotation object
     * @return What the Icon should be
     */
    public IIcon getNorth(int meta, IRotation rotation) {
        switch(rotation.convertMetaToDirection(meta)) {
            case UP :
                return up;
            case DOWN :
                return down;
            case WEST :
                return right;
            case SOUTH :
                return back;
            case EAST :
                return left;
            case NORTH :
            default :
                return front;
        }
    }

    public void setFront(IIcon icon) {
        front = icon;
    }

    public IIcon getBack() {
        return back;
    }

    /**
     * Gets the icon for this position from the rotation
     * @param meta Block meta data
     * @param rotation Rotation object
     * @return What the Icon should be
     */
    public IIcon getSouth(int meta, IRotation rotation) {
        switch(rotation.convertMetaToDirection(meta)) {
            case UP :
                return up;
            case DOWN :
                return down;
            case WEST :
                return right;
            case NORTH :
                return back;
            case EAST :
                return left;
            case SOUTH :
            default :
                return front;
        }
    }

    public void setBack(IIcon icon) {
        back = icon;
    }

    public IIcon getLeft() {
        return left;
    }

    /**
     * Gets the icon for this position from the rotation
     * @param meta Block meta data
     * @param rotation Rotation object
     * @return What the Icon should be
     */
    public IIcon getEast(int meta, IRotation rotation) {
        switch(rotation.convertMetaToDirection(meta)) {
            case UP :
                return up;
            case DOWN :
                return down;
            case SOUTH :
                return right;
            case WEST :
                return back;
            case NORTH :
                return left;
            case EAST :
            default :
                return front;
        }
    }

    public void setLeft(IIcon icon) {
        left = icon;
    }

    public IIcon getRight() {
        return right;
    }

    /**
     * Gets the icon for this position from the rotation
     * @param meta Block meta data
     * @param rotation Rotation object
     * @return What the Icon should be
     */
    public IIcon getWest(int meta, IRotation rotation) {
        switch(rotation.convertMetaToDirection(meta)) {
            case UP :
                return up;
            case DOWN :
                return down;
            case NORTH :
                return right;
            case EAST :
                return back;
            case SOUTH :
                return left;
            case WEST :
            default :
                return front;
        }
    }

    public void setRight(IIcon icon) {
        right = icon;
    }

    public IIcon getTop() {
        return up;
    }

    /**
     * Gets the icon for this position from the rotation
     * @param meta Block meta data
     * @param rotation Rotation object
     * @return What the Icon should be
     */
    public IIcon getUp(int meta, IRotation rotation) {
        switch(rotation.convertMetaToDirection(meta)) {
            case NORTH :
                return up;
            case EAST :
                return up;
            case SOUTH :
                return up;
            case WEST :
                return up;
            case DOWN :
                return back;
            case UP :
            default :
                return front;
        }
    }

    public void setTop(IIcon icon) {
        up = icon;
    }

    public IIcon getBottom() {
        return down;
    }

    /**
     * Gets the icon for this position from the rotation
     * @param meta Block meta data
     * @param rotation Rotation object
     * @return What the Icon should be
     */
    public IIcon getDown(int meta, IRotation rotation) {
        switch(rotation.convertMetaToDirection(meta)) {
            case NORTH :
                return down;
            case EAST :
                return down;
            case SOUTH :
                return down;
            case WEST :
                return down;
            case UP :
                return back;
            case DOWN :
            default :
                return front;
        }
    }

    public void setBottom(IIcon icon) {
        down = icon;
    }
}
