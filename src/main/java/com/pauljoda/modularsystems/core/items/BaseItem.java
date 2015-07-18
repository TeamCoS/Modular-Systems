package com.pauljoda.modularsystems.core.items;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.lib.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class BaseItem extends Item {

    private String name;

    public BaseItem(String name, int maxStackSize) {
        this.setCreativeTab(ModularSystems.tabModularSystems);
        this.maxStackSize = maxStackSize;
        this.setUnlocalizedName(Reference.MOD_ID + ":" + name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon(Reference.MOD_ID + ":" + name);
    }
}
