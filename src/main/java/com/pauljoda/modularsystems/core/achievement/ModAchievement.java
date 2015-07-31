package com.pauljoda.modularsystems.core.achievement;

import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.teambr.bookshelf.achievement.AchievementList;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import net.minecraft.util.StatCollector;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/31/2015
 */
public class ModAchievement extends AchievementList {
    public static ModAchievement INSTANCE = new ModAchievement();

    public String CRAFT_FURNACE;
    public String CRAFT_CRUSHER;
    public String CRAFT_GENERATOR;
    public String CRAFT_STORAGE;
    public String CRAFT_IO;
    public String CRAFT_REDSTONE;

    /**
     * Used to create a new list of achievements
     */
    public ModAchievement() {
        super(StatCollector.translateToLocal("achievement.modularsystems.title"));
    }

    @Override
    public void initAchievements() {
        buildAchievement(CRAFT_FURNACE = "craftFurnace", 0, 0, BlockManager.furnaceCore, null);
        buildAchievement(CRAFT_CRUSHER = "craftCrusher", 0, -2, BlockManager.crusherCore, null);
        buildAchievement(CRAFT_GENERATOR = "craftGenerator", 0, 2, BlockManager.generatorCore, null);
        buildAchievement(CRAFT_STORAGE = "craftStorage", 2, 0, BlockManager.storageCore, null);
        buildAchievement(CRAFT_IO = "craftIO", 2, 2, BlockManager.io, null);
        buildAchievement(CRAFT_REDSTONE = "craftRedstone", 2, -2, BlockManager.redstoneControlIn, null);
    }

    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent event) {
        Item item = event.crafting.getItem();
        int damage = event.crafting.getItemDamage();

        for (Achievement a : getAchievements()) {
            if (item.equals(a.theItemStack.getItem()) && damage == a.theItemStack.getItemDamage()) {
                event.player.addStat(a, 1);
            }
        }
    }

    /**
     * Stub method to start
     */
    public void start() {}
}
