package com.pauljoda.modularsystems.core.proxy;

import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.enchanting.container.ContainerEnchantmentUpgrades;
import com.pauljoda.modularsystems.enchanting.container.ContainerModularEnchanting;
import com.pauljoda.modularsystems.enchanting.tiles.TileEntityEnchantmentAlter;
import com.pauljoda.modularsystems.furnace.containers.ContainerModularFurnace;
import com.pauljoda.modularsystems.furnace.containers.ContainerModularFurnaceCrafter;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceDummy;
import com.pauljoda.modularsystems.oreprocessing.container.ContainerModularOreProcessing;
import com.pauljoda.modularsystems.oreprocessing.tiles.TileEntitySmelteryCore;
import com.pauljoda.modularsystems.oreprocessing.tiles.TileEntitySmelteryDummy;
import com.pauljoda.modularsystems.storage.containers.ContainerModularStorage;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageCore;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageExpansion;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CommonProxy implements IGuiHandler
{
	public void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityFurnaceCore.class, "modularsystems:tileEntityFurnaceCore");
		GameRegistry.registerTileEntity(TileEntityFurnaceDummy.class, "modularsystems:tileEntityFurnaceDummy");
		GameRegistry.registerTileEntity(TileEntityStorageCore.class, "modularsystems:tileEntityStorageCore");
		GameRegistry.registerTileEntity(TileEntityStorageExpansion.class, "modularsystems:tileEntityStorageExpansion");
		GameRegistry.registerTileEntity(TileEntityEnchantmentAlter.class, "modularsystems:tileEntityEnchantmentAlter");
        GameRegistry.registerTileEntity(TileEntitySmelteryCore.class, "modularsystems:tileEntitySmetleryCore");
        GameRegistry.registerTileEntity(TileEntitySmelteryDummy.class, "modularsystems:tileEntitySmelteryDummy");

	}


	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
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
					return new ContainerModularFurnaceCrafter(player.inventory, tileEntity1);
				}
				else
				{
					return new ContainerModularFurnace(player.inventory, tileEntity1);
				}
			}

			//Storage
			else if(tileEntity instanceof TileEntityStorageCore)
			{
				TileEntityStorageCore storageCore = (TileEntityStorageCore)world.getTileEntity(x, y, z);
				return new ContainerModularStorage(player.inventory, storageCore, player, storageCore.hasSpecificUpgrade(Reference.ARMOR_STORAGE_EXPANSION), storageCore.hasSpecificUpgrade(Reference.CRAFTING_STORAGE_EXPANSION));
			}

			//Storage
			else if(tileEntity instanceof TileEntityEnchantmentAlter)
			{
				TileEntityEnchantmentAlter alter = (TileEntityEnchantmentAlter)world.getTileEntity(x, y, z);
				if(id == 0)
					return new ContainerModularEnchanting(player.inventory, alter, player);
				else if (id == 1)
					return new ContainerEnchantmentUpgrades(player.inventory, alter, player);
			}

            //OreProcessing
            else if(tileEntity instanceof TileEntitySmelteryCore)
            {
                TileEntitySmelteryCore core = (TileEntitySmelteryCore)world.getTileEntity(x,y,z);
                return new ContainerModularOreProcessing(player.inventory, core);
            }
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int guiID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}
}