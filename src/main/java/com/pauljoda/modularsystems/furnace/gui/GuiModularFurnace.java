package com.pauljoda.modularsystems.furnace.gui;

import com.dyonovan.brlib.client.gui.GuiBase;
import com.pauljoda.modularsystems.furnace.container.ContainerModularFurnace;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiModularFurnace extends GuiBase<ContainerModularFurnace> {
    public GuiModularFurnace(InventoryPlayer player, TileEntityFurnaceCore core) {
        super(new ContainerModularFurnace(player, core), 175, 165, "inventory.furnace.title");
    }

    @Override
    public void addComponents() {}
}
