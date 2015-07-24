package com.pauljoda.modularsystems.storage.container;

import com.pauljoda.modularsystems.storage.tiles.TileStorageCore;
import com.teambr.bookshelf.inventory.BaseContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/23/2015
 */
public class ContainerStorageCore extends BaseContainer {
    public TileStorageCore storage;
    protected List<Slot> displaySlots;
    protected List<Slot> allSlots;
    protected String currentSearch = "";

    public ContainerStorageCore(IInventory playerInventory, IInventory ownerInventory, TileStorageCore tile) {
        super(playerInventory, ownerInventory);
        storage = tile;
        addInventoryGrid(25, 27, 11, 6);
        if(storage.getInventoryRowCount() > 6) {
            int slotId = inventorySlots.size();
            for(int i = 66; i < storage.getSizeInventory(); i++)
                addSlotToContainer(new RestrictedSlot(inventory, slotId++, -10000, -10000));
        }
        displaySlots = new ArrayList<>();
        displaySlots.addAll(inventorySlots);

        allSlots = new ArrayList<>();
        allSlots.addAll(displaySlots);

        addPlayerInventorySlots(44, 140);
    }

    protected void addInventoryGrid(int xOffset, int yOffset, int width, int rows) {
        for (int y = 0, slotId = 0; y < rows; y++) {
            for (int x = 0; x < width; x++, slotId++) {
                addSlotToContainer(new RestrictedSlot(inventory, slotId,
                        xOffset + x * 18,
                        yOffset + y * 18));
            }
        }
    }

    public void scrollTo(float index) {
        int outsideDisplaySize = ((allSlots.size() / 11) + (allSlots.size() % 11 > 0 ? 1 : 0)) - 6;
        int currentTopRow = Math.round(index * (float)outsideDisplaySize);
        if(currentTopRow < 0)
            currentTopRow = 0;

        displaySlots.clear();

        for(int y = 0; y < 6; y++) {
            for(int x = 0; x < 11; x++) {
                if((x + ((y + currentTopRow) * 11)) >= allSlots.size())
                    break;
                Slot slot = this.allSlots.get((x + ((y + currentTopRow) * 11)));
                displaySlots.add(slot);
            }
        }

        updateDisplayedSlots();
    }

    public void keyTyped(String str) {
        currentSearch = str.toLowerCase();
        displaySlots.clear();
        displaySlots.addAll(allSlots);
        updateDisplayedSlots();
    }

    public void updateDisplayedSlots() {
        //Move it all off
        for (Slot displaySlot : allSlots) {
            displaySlot.xDisplayPosition = displaySlot.yDisplayPosition = -10000;
        }

        //Trim out what doesn't match the string
        for(int i = 0; i < displaySlots.size(); i++) {
            Slot slot = displaySlots.get(i);
            if(slot.getHasStack()) {
                if(!slot.getStack().getDisplayName().toLowerCase().contains(currentSearch))
                    displaySlots.remove(i);
            }
        }

        if(!currentSearch.isEmpty() && !currentSearch.equalsIgnoreCase(""))
            Collections.sort(displaySlots, new SlotComparator());

        for(int y = 0; y < 6; y++) {
            for(int x = 0; x < 11; x++) {
                if((x + (y * 11)) >= displaySlots.size())
                    break;
                Slot slot = this.displaySlots.get((x + (y * 11)));
                slot.xDisplayPosition = 25 + x * 18;
                slot.yDisplayPosition = 27 + y * 18;
            }
        }
    }

    protected class SlotComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            Slot thisSlot = (Slot)o1;
            Slot thatSlot = (Slot)o2;

            if(!thisSlot.getHasStack() && !thatSlot.getHasStack())
                return 0;
            else if(thisSlot.getHasStack() && !thatSlot.getHasStack())
                return -1;
            else if(!thisSlot.getHasStack())
                return 1;
            else if(thisSlot.getStack().getDisplayName().startsWith(currentSearch) && !thatSlot.getStack().getDisplayName().startsWith(currentSearch))
                return -1;
            else if(!thisSlot.getStack().getDisplayName().startsWith(currentSearch) && thatSlot.getStack().getDisplayName().startsWith(currentSearch))
                return 1;
            return 0;
        }
    }
}
