package com.teamcos.modularsystems.furnace.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.teamcos.modularsystems.core.managers.BlockManager;
import com.teamcos.modularsystems.core.proxy.ClientProxy;
import com.teamcos.modularsystems.furnace.tiles.TileEntityFurnaceDummy;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FurnaceDummyRenderer implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {

		render3DInventory(block, metadata, modelID, renderer);

	}

	public static void render3DInventory(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		Tessellator tessellator = Tessellator.instance;

		block.setBlockBoundsForItemRender();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, getOveryLay(block, renderer));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, getOveryLay(block, renderer));

		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, getOveryLay(block, renderer));

		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, getOveryLay(block, renderer));

		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, getOveryLay(block, renderer));

		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, 5));
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, getOveryLay(block, renderer));

		tessellator.draw();

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	private static IIcon getOveryLay(Block block, RenderBlocks renderer)
	{

		IIcon output;

		if(block == BlockManager.furnaceCore || block == BlockManager.furnaceCraftingUpgrade || block == BlockManager.furnaceDummyIO || block == BlockManager.furnaceAddition)
			output = renderer.getBlockIconFromSideAndMetadata(BlockManager.overLayTexture, 0, 0);
		else 
			output = renderer.getBlockIconFromSideAndMetadata(block, 0,0);

		return output;
	}
	@SideOnly(Side.CLIENT)
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {

		//which render pass are we doing?
		if(ClientProxy.renderPass == 0)
		{
			World world1 = Minecraft.getMinecraft().theWorld;
			TileEntityFurnaceDummy dummy = null;

			if(block == BlockManager.furnaceDummy)
			{
				dummy = (TileEntityFurnaceDummy)world1.getTileEntity(x, y, z);
				renderer.renderBlockUsingTexture(dummy.getBlock(), x, y, z, dummy.getBlock().getIcon(0, dummy.getMeta()));

			}

			if(block == BlockManager.furnaceDummyIO)
				renderer.renderBlockUsingTexture(Blocks.dispenser, x, y, z, Blocks.dispenser.getIcon(1, 1));

			if(block == BlockManager.furnaceCraftingUpgrade)
				renderer.renderBlockUsingTexture(Blocks.crafting_table, x, y, z, Blocks.crafting_table.getIcon(1, 0));

			if(block == BlockManager.furnaceAddition)
				renderer.renderBlockUsingTexture(Blocks.furnace, x, y, z, Blocks.furnace.getIcon(2, 0));

			if(block == BlockManager.furnaceCore)
			{
				renderer.renderBlockAllFaces(Blocks.furnace, x, y, z);   
			}

			if(block == BlockManager.furnaceCoreActive)
				renderer.renderBlockAllFaces(Blocks.lit_furnace, x, y, z);    

		}
		else                   
		{
				renderer.renderStandardBlock(BlockManager.overLayTexture, x, y, z);
		}

		return true;
	}

	@Override
	public int getRenderId() {

		return ClientProxy.furnaceDummyRenderType;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

}
