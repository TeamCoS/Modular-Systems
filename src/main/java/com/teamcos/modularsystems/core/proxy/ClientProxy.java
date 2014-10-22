package com.teamcos.modularsystems.core.proxy;

import com.teamcos.modularsystems.core.lib.Reference;
import com.teamcos.modularsystems.enchanting.gui.GuiEnchantmentUpgrades;
import com.teamcos.modularsystems.enchanting.gui.GuiModularEnchanting;
import com.teamcos.modularsystems.enchanting.tiles.TileEntityEnchantmentAlter;
import com.teamcos.modularsystems.furnace.containers.ContainerModularFurnace;
import com.teamcos.modularsystems.furnace.containers.ContainerModularFurnaceCrafter;
import com.teamcos.modularsystems.furnace.gui.GuiModularFurnace;
import com.teamcos.modularsystems.furnace.gui.GuiModularFurnaceEnabled;
import com.teamcos.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import com.teamcos.modularsystems.oreprocessing.container.ContainerModularOreProcessing;
import com.teamcos.modularsystems.oreprocessing.gui.GuiModularOreProcessing;
import com.teamcos.modularsystems.oreprocessing.renderer.ModularSystemsRenderer;
import com.teamcos.modularsystems.oreprocessing.tiles.TileEntitySmelteryCore;
import com.teamcos.modularsystems.renderers.ApiRenderers;
import com.teamcos.modularsystems.storage.gui.GuiModularStorage;
import com.teamcos.modularsystems.storage.tiles.TileEntityStorageCore;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {

    public static int renderPass;
    public static int msRenderId;

    public static void setCustomRenderers() {

        ApiRenderers.init();
        msRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new ModularSystemsRenderer());
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (tileEntity != null) {
            //Furnace
            if (tileEntity instanceof TileEntityFurnaceCore) {
                TileEntityFurnaceCore tileEntity1 = (TileEntityFurnaceCore) world.getTileEntity(x, y, z);

                if (tileEntity1.hasCraftingUpgrade()) {
                    return new GuiModularFurnaceEnabled(new ContainerModularFurnaceCrafter(player.inventory, tileEntity1), tileEntity1);
                } else {
                    return new GuiModularFurnace(new ContainerModularFurnace(player.inventory, tileEntity1), tileEntity1);
                }
            }

            //Storage
            else if (tileEntity instanceof TileEntityStorageCore) {
                TileEntityStorageCore storageCore = (TileEntityStorageCore) world.getTileEntity(x, y, z);
                return new GuiModularStorage(player.inventory, storageCore, player, storageCore.hasSpecificUpgrade(Reference.ARMOR_STORAGE_EXPANSION), storageCore.hasSpecificUpgrade(Reference.CRAFTING_STORAGE_EXPANSION));
            }

            //Enchanting
            else if (tileEntity instanceof TileEntityEnchantmentAlter) {
                TileEntityEnchantmentAlter alter = (TileEntityEnchantmentAlter) world.getTileEntity(x, y, z);
                if (id == 0)
                    return new GuiModularEnchanting(player.inventory, alter, player);
                else if (id == 1)
                    return new GuiEnchantmentUpgrades(player.inventory, alter, player);
            }

            //OreProcessing
            else if (tileEntity instanceof TileEntitySmelteryCore) {
                TileEntitySmelteryCore smelterCore = (TileEntitySmelteryCore) world.getTileEntity(x, y, z);
                return new GuiModularOreProcessing(new ContainerModularOreProcessing(player.inventory, smelterCore), smelterCore);
            }
        }
        return null;
    }
}
