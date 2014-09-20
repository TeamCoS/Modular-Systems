package com.pauljoda.modularsystems.core.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.furnace.gui.GuiModularFurnace;
import com.pauljoda.modularsystems.furnace.gui.GuiModularFurnaceEnabled;
import com.pauljoda.modularsystems.furnace.renderer.FurnaceDummyRenderer;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import com.pauljoda.modularsystems.storage.gui.GuiModularStorage;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageCore;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	public static int renderPass;
	public static int furnaceDummyRenderType; 
	public static void setCustomRenderers()
	{
		furnaceDummyRenderType = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new FurnaceDummyRenderer());
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

					return new GuiModularFurnaceEnabled(player.inventory, world, x, y, z, tileEntity1);
				}
				else
				{
					return new GuiModularFurnace(player.inventory, tileEntity1);
				}
			}
			
			//Storage
			else if(tileEntity instanceof TileEntityStorageCore)
			{
				TileEntityStorageCore storageCore = (TileEntityStorageCore)world.getTileEntity(x, y, z);
				return new GuiModularStorage(player.inventory, storageCore, player, storageCore.hasSpecificUpgrade(Reference.ARMOR_STORAGE_EXPANSION));
			}
		}
		return null;
	}
}
