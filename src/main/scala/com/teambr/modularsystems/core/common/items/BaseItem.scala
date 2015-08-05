package com.teambr.modularsystems.core.common.items

import com.teambr.modularsystems.core.ModularSystems
import com.teambr.modularsystems.core.lib.Reference
import net.minecraft.item.Item

/**
 * Modular-Systems
 * Created by Dyonovan on 05/08/15
 */
class BaseItem(name: String, maxStackSize: Int) extends Item {

    setCreativeTab(ModularSystems.tabModularSystems)
    setMaxStackSize(maxStackSize)
    setUnlocalizedName(Reference.MOD_ID + ":" + name)

    def getName: String = { name }

}
