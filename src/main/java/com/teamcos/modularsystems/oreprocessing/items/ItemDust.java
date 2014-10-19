package com.teamcos.modularsystems.oreprocessing.items;

import com.teamcos.modularsystems.core.ModularSystems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemDust extends Item
{
    public ItemDust(String unlocalizeName)
    {
        super();
        this.setUnlocalizedName(unlocalizeName);
        this.setCreativeTab(ModularSystems.tabModularSystems);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(this.getUnlocalizedName().substring(5));
    }
}
