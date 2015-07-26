package com.pauljoda.modularsystems.storage.gui;

import com.pauljoda.modularsystems.storage.tiles.TileStorageSecurity;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentCheckBox;
import com.teambr.bookshelf.inventory.ContainerGeneric;
import com.teambr.bookshelf.manager.PacketManager;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/25/2015
 */
public class GuiStorageSecurity extends GuiBase<ContainerGeneric> {
    protected TileStorageSecurity securityTile;
    protected float currentScroll;
    List<String> matchingPlayers = new ArrayList<>();
    int position = 0;
    protected GuiComponentEditableList<String> editField;

    public GuiStorageSecurity(TileStorageSecurity tile) {
        super(new ContainerGeneric(), 175, 175, "tile.modularsystems:storageSecurity.name");
        securityTile = tile;
        addComponents();
    }

    @Override
    public void addComponents() {
        if(securityTile != null && securityTile.getCore() != null) {
            components.add(new GuiComponentCheckBox(10, 20, "inventory.storageSecurity.lock", securityTile.getCore().isSecured()) {
                @Override
                public void setValue(boolean bool) {
                    securityTile.getCore().setIsSecured(bool);
                    PacketManager.updateTileWithClientInfo(securityTile.getCore());
                }
            });

            components.add(editField = new GuiComponentEditableList<String>(20, 40, securityTile.getCore().getAllowedPlayers()) {
                @Override
                public void valueSaved(List<String> listToSaveTo, String valuePassed) {

                }

                @Override
                public String convertObjectToString(String object) {
                    return null;
                }
            });
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        int scrollDirection = Mouse.getEventDWheel();
        if (scrollDirection != 0 && securityTile.getCore().getAllowedPlayers().size() > 5) {

            int rowsAboveDisplay = securityTile.getCore().getAllowedPlayers().size() - 5;
            if (scrollDirection > 0)
                scrollDirection = 1;
            else
                scrollDirection = -1;

            this.currentScroll = (float) ((double) this.currentScroll - (double) scrollDirection / (double) rowsAboveDisplay);

            if (currentScroll < 0.0F)
                currentScroll = 0.0F;
            else if (currentScroll > 1.0F)
                currentScroll = 1.0F;

            this.editField.scrollBar.setPosition(this.currentScroll);
            updateScreen();
        }
        if(this.editField.getInputBox() != null && this.editField.getInputBox().getTextField() != null && this.editField.getInputBox().getTextField().isFocused())
            Keyboard.enableRepeatEvents(true);
        else
            Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped(char letter, int keyCode) {
        if(keyCode == 15) {
            completePlayers();
        }
        else {
            matchingPlayers.clear();
        }
        if(this.editField.getInputBox() != null && this.editField.getInputBox().getTextField() != null && !this.editField.getInputBox().getTextField().textboxKeyTyped(letter, keyCode)) {
            super.keyTyped(letter, keyCode);
        }
    }

    public void completePlayers() {
        String starting;
        if(this.editField.getInputBox().getValue() != null) {
            starting = this.editField.getInputBox().getValue();
            if ((!this.mc.isIntegratedServerRunning() || this.mc.thePlayer.sendQueue.playerInfoList.size() > 1)) {
                this.mc.mcProfiler.startSection("playerList");
                NetHandlerPlayClient netclienthandler = this.mc.thePlayer.sendQueue;
                List list = netclienthandler.playerInfoList;

                for (Object aList : list) {
                    GuiPlayerInfo guiplayerinfo = (GuiPlayerInfo) aList;
                    if (guiplayerinfo.name.toLowerCase().contains(starting.toLowerCase())) {
                        matchingPlayers.add(guiplayerinfo.name);
                    }
                }
            }
        }

        if(matchingPlayers.size() > 0) {
            ++position;
            if(position >= matchingPlayers.size())
                position = 0;
            this.editField.getInputBox().getTextField().setText(matchingPlayers.get(position));
        }
    }
}
