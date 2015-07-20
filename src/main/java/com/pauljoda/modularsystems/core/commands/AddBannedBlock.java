package com.pauljoda.modularsystems.core.commands;

import com.pauljoda.modularsystems.core.registries.FurnaceBannedBlocks;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

/**
 * This is our command to add a banned block to the furnace. Or whatever structure calls this
 */
public class AddBannedBlock extends CommandBase {

    /**
     * The command Name
     */
    @Override
    public String getCommandName() {
        return "addBannedFurnaceBlock";
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    /**
     * Returns the usage, this will help those learn how to use it
     */
    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "commands.addBannedFurnaceBlock.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length < 1)
            sender.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("commands.addBannedFurnaceBlock.failure")));
        else {
            if (args[0].equalsIgnoreCase("this")) {
                GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(getCommandSenderAsPlayer(sender).getHeldItem().getItem());
                if (id == null) {
                    sender.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("commands.addBannedFurnaceBlock.failure")));
                    return;
                }
                int meta = getCommandSenderAsPlayer(sender).getHeldItem().getItemDamage();
                FurnaceBannedBlocks.INSTANCE.addBannedBlock(id.modId + ":" + id.name + ":" + meta);
                sender.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("commands.addBannedFurnaceBlock.success")));
            } else {
                FurnaceBannedBlocks.INSTANCE.addBannedBlock(args[0]);
                sender.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("commands.addBannedFurnaceBlock.success")));
            }
        }
    }
}
