package com.pauljoda.modularsystems.core.collections;

import com.pauljoda.modularsystems.core.lib.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

/**
 * Used as a collection of textures. Makes it easier than manually finding the textures
 */
@SideOnly(Side.CLIENT)
public class BlockTextures {
    public IIcon front, back, left, right, up, down;

    /**
     * Used to register and store icons for blocks that have no specific differences
     * @param register The IIconRegister
     * @param allTextures The string representation of the icon
     */
    public BlockTextures(IIconRegister register, String allTextures) {
        front = back = left = right = up = down = register.registerIcon(allTextures);
    }
}
