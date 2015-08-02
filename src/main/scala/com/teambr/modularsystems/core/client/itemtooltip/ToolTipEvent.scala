package com.teambr.modularsystems.core.client.itemtooltip

import net.minecraft.block.Block
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * This file was created for the Modular-Systems
 *
 * Modular-Systems if licensed under the is licensed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since August 02, 2015
 */
class ToolTipEvent {
    @SubscribeEvent def onToolTip(event: ItemTooltipEvent) {
        var item: IItemTooltip = null

        //Extract the item/block
        Block.getBlockFromItem(event.itemStack.getItem) match {
            case isABlock: IItemTooltip => item = isABlock
            case _ => event.itemStack.getItem match {
                case theItem: IItemTooltip => item = theItem
                case _ =>
            }
        }

        //Add the tip should it need
        if (item != null) {
            val tips: java.util.List[String] = item.returnTooltip
            if (tips != null) for (tip <- tips) event.toolTip.add(tip)
        }
    }
}
