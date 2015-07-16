package com.pauljoda.modularsystems.core.commands;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.managers.GuiManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

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
    public void processCommand(ICommandSender sender, String[] args) {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        player.openGui(ModularSystems.instance, GuiManager.VALUE_CONFIG, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
    }
}
