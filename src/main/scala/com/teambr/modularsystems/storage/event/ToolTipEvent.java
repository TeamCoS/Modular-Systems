package com.teambr.modularsystems.storage.event;

import com.teambr.bookshelf.client.gui.GuiColor;
import com.teambr.modularsystems.storage.container.ContainerStorageCore;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis "pauljoda"
 * @since 3/28/2016
 */
public class ToolTipEvent {
    @SubscribeEvent
    public void onToolTip(ItemTooltipEvent event) {
        if(Minecraft.getMinecraft().thePlayer.openContainer != null &&
                Minecraft.getMinecraft().thePlayer.openContainer instanceof ContainerStorageCore && event.getItemStack().stackSize > 1000) {
            event.getToolTip().add(GuiColor.YELLOW + "Quantity");
            event.getToolTip().add("  " + NumberFormat.getNumberInstance(
                    Locale.forLanguageTag(Minecraft.getMinecraft().gameSettings.language)).format(event.getItemStack().stackSize));
            event.getToolTip().add(GuiColor.YELLOW + "Stacks");
            event.getToolTip().add("  " +  NumberFormat.getNumberInstance(
                    Locale.forLanguageTag(Minecraft.getMinecraft().gameSettings.language)).format(
                    Math.floor(event.getItemStack().stackSize / event.getItemStack().getMaxStackSize())) +
                    " + " + event.getItemStack().stackSize % event.getItemStack().getMaxStackSize());
        }
    }
}
