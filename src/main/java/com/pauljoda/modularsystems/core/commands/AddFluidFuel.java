package com.pauljoda.modularsystems.core.commands;

import com.pauljoda.modularsystems.core.registries.FluidFuelValues;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

public class AddFluidFuel extends CommandBase {
    @Override
    public String getCommandName() {
        return "addFluidFuel";
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "commands.addFluidFuel.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length < 2)
            sender.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("commands.addFluidFuel.usage")));
        else {
            FluidFuelValues.INSTANCE.addFluidFuel(args[0], Integer.getInteger(args[1]));
            sender.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("commands.addFluidFuel.success")));
        }
    }
}
