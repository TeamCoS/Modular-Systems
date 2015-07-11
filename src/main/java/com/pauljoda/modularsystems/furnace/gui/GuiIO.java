package com.pauljoda.modularsystems.furnace.gui;

import com.pauljoda.modularsystems.core.tiles.DummyIO;
import com.pauljoda.modularsystems.furnace.container.ContainerGeneric;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.manager.PacketManager;
import com.teambr.bookshelf.network.ClientTileUpdate;
import net.minecraft.nbt.NBTTagCompound;

public class GuiIO extends GuiBase<ContainerGeneric> {
    protected DummyIO dummy;
    public GuiIO(DummyIO d, ContainerGeneric container, int width, int height, String name) {
        super(container, width, height, name);
        dummy = d;
        addComponents();
    }

    @Override
    public void addComponents() {
        if (dummy != null) {
            components.add(new GuiComponentCheckBox(10, 40, "Hello", dummy.input) {
                @Override
                public void setValue(boolean bool) {
                    dummy.setInput(bool);
                    NBTTagCompound tag = new NBTTagCompound();
                    dummy.writeToNBT(tag);
                    PacketManager.net.sendToServer(new ClientTileUpdate.Message(dummy.xCoord, dummy.yCoord, dummy.zCoord, tag));
                }
            });
        }
    }
}
