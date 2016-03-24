package com.teambr.modularsystems.core.commands;

import com.teambr.modularsystems.core.ModularSystems;
import com.teambr.modularsystems.core.managers.GuiManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis "pauljoda"
 * @since 3/24/2016
 */
public class OpenValueConfig extends CommandBase {

    @Override
    public String getCommandName() {
        return "configureBlockValues";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.usage.configureBlockValues";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayerMP player = null;
        try {
            player = getCommandSenderAsPlayer(sender);
            player.openGui(ModularSystems.getInstance(),
                    GuiManager.VALUE_CONFIG, player.worldObj,
                    (int)player.posX, (int)player.posY, (int)player.posZ);
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
        }
    }
}
