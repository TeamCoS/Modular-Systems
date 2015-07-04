package com.pauljoda.modularsystems.furnace.gui;

import com.dyonovan.brlib.client.gui.GuiBase;
import com.pauljoda.modularsystems.furnace.container.ContainerModularFurnace;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiModularFurnace extends GuiBase<ContainerModularFurnace> {
    private static final ResourceLocation background = new ResourceLocation("textures/gui/container/furnace.png");
    protected TileEntityFurnaceCore core;

    public GuiModularFurnace(InventoryPlayer player, TileEntityFurnaceCore tile) {
        super(new ContainerModularFurnace(player, tile), 175, 165, "inventory.furnace.title");
        this.core = tile;
    }

    @Override
    public void addComponents() {}

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);

        this.mc.getTextureManager().bindTexture(background);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        int i1;
        if(core.isBurning()) {
            i1 = core.getBurnTimeRemainingScaled(12);
            drawTexturedModalRect(x + 56, y + 36 + 12 - i1, 176, 13 - i1, 14, i1 + 2);

        }
        i1 = core.getCookProgressScaled(24);
        drawTexturedModalRect(x + 79, y + 34, 176, 14, i1 + 1, 16);
    }
}
