package com.teambr.modularsystems.core.achievement

import com.teambr.bookshelf.achievement.AchievementList
import com.teambr.modularsystems.core.managers.BlockManager
import net.minecraft.util.StatCollector
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since August 05, 2015
 */
object ModAchievements extends AchievementList(StatCollector.translateToLocal("achievement.modularsystems.title")) {

    def CRAFT_FURNACE : String = "craftFurnace"
    def CRAFT_CRUSHER : String = "craftCrusher"
    def CRAFT_GENERATOR : String = "craftGenerator"
    def CRAFT_STORAGE : String = "craftStorage"
    def CRAFT_IO : String = "craftIO"
    def CRAFT_REDSTONE : String = "craftRedstone"

    override def initAchievements() : Unit = {
        buildAchievement(CRAFT_STORAGE, 2, 0, BlockManager.storageCore, null)
    }

    @SubscribeEvent
    def onCrafting(event : PlayerEvent.ItemCraftedEvent): Unit = {
        val item = event.crafting.getItem
        val damage = event.crafting.getItemDamage

        for(a <- 0 until getAchievements.size()) {
            val achievement = getAchievements.get(a)
            if(item.equals(achievement.theItemStack.getItem) && damage == achievement.theItemStack.getItemDamage)
                event.player.addStat(achievement, 1)
        }
    }
}
