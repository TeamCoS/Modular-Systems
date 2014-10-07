package com.pauljoda.modularsystems.core.proxy;

import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.enchanting.gui.GuiEnchantmentUpgrades;
import com.pauljoda.modularsystems.enchanting.gui.GuiModularEnchanting;
import com.pauljoda.modularsystems.enchanting.tiles.TileEntityEnchantmentAlter;
import com.pauljoda.modularsystems.furnace.containers.ContainerModularFurnace;
import com.pauljoda.modularsystems.furnace.containers.ContainerModularFurnaceCrafter;
import com.pauljoda.modularsystems.furnace.gui.GuiModularFurnace;
import com.pauljoda.modularsystems.furnace.gui.GuiModularFurnaceEnabled;
import com.pauljoda.modularsystems.furnace.renderer.FurnaceDummyRenderer;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import com.pauljoda.modularsystems.oreprocessing.container.ContainerModularOreProcessing;
import com.pauljoda.modularsystems.oreprocessing.gui.GuiModularOreProcessing;
import com.pauljoda.modularsystems.oreprocessing.renderer.SmelteryDummyRenderer;
import com.pauljoda.modularsystems.oreprocessing.tiles.TileEntitySmelteryCore;
import com.pauljoda.modularsystems.storage.gui.GuiModularStorage;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageCore;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {

	public static int renderPass;
	public static int furnaceDummyRenderType;
    public static int smelteryDummyRenderType;
	public static void setCustomRenderers()
	{
		furnaceDummyRenderType = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new FurnaceDummyRenderer());

        smelteryDummyRenderType = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new SmelteryDummyRenderer());
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		if(tileEntity != null)
		{
			//Furnace
			if(tileEntity instanceof TileEntityFurnaceCore)
			{
				TileEntityFurnaceCore tileEntity1 = (TileEntityFurnaceCore)world.getTileEntity(x, y, z);

				if(tileEntity1.checkIfCrafting())
				{

					return new GuiModularFurnaceEnabled(new ContainerModularFurnaceCrafter(player.inventory, tileEntity1), tileEntity1);
				}
				else
				{
					return new GuiModularFurnace(new ContainerModularFurnace(player.inventory, tileEntity1), tileEntity1);
				}
			}

			//Storage
			else if(tileEntity instanceof TileEntityStorageCore)
			{
				TileEntityStorageCore storageCore = (TileEntityStorageCore)world.getTileEntity(x, y, z);
				return new GuiModularStorage(player.inventory, storageCore, player, storageCore.hasSpecificUpgrade(Reference.ARMOR_STORAGE_EXPANSION), storageCore.hasSpecificUpgrade(Reference.CRAFTING_STORAGE_EXPANSION));
			}

			//Enchanting
			else if(tileEntity instanceof TileEntityEnchantmentAlter)
			{
				TileEntityEnchantmentAlter alter = (TileEntityEnchantmentAlter)world.getTileEntity(x, y, z);
				if(id == 0)
					return new GuiModularEnchanting(player.inventory, alter, player);
				else if(id == 1)
					return new GuiEnchantmentUpgrades(player.inventory, alter, player);
			}

            //OreProcessing
            else if(tileEntity instanceof TileEntitySmelteryCore)
            {
                TileEntitySmelteryCore smelterCore = (TileEntitySmelteryCore)world.getTileEntity(x,y,z);
                return new GuiModularOreProcessing(new ContainerModularOreProcessing(player.inventory, smelterCore), smelterCore);
            }
		}
		return null;
	}
}
