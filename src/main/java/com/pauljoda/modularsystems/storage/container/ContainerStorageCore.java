package com.pauljoda.modularsystems.storage.container;

import com.pauljoda.modularsystems.storage.tiles.TileStorageCore;
import com.teambr.bookshelf.inventory.BaseContainer;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

import java.util.*;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/23/2015
 */
public class ContainerStorageCore extends BaseContainer {
    public TileStorageCore storage;
    protected List<Slot> displaySlots;
    protected List<Slot> allSlots;
    protected String currentSearch = "";

    protected InventoryCrafting craftingGrid;
    protected IInventory craftResult = new InventoryCraftResult();

    public ContainerStorageCore(InventoryPlayer playerInventory, IInventory ownerInventory, TileStorageCore tile) {
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

        addPlayerInventorySlots(storage.hasCraftingUpgrade() ? 8 : 44, 140);

        if(storage.hasCraftingUpgrade()) {
            craftingGrid = new DummyCraftingInventory(storage, this);
            addCraftingGrid(craftingGrid, 0, 180, 162, 3, 3);
            this.addSlotToContainer(new SlotCrafting(playerInventory.player, craftingGrid, craftResult, inventorySize + 1, 198, 140));
            onCraftMatrixChanged(craftingGrid);
        }
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
        for(Iterator<Slot> i = displaySlots.iterator(); i.hasNext();) {
            Slot slot = i.next();
            if(slot.getHasStack()) {
                if(currentSearch.startsWith("@")) {
                    GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(slot.getStack().getItem());
                    if(currentSearch.length() > 1 && !id.modId.toLowerCase().contains(currentSearch.split("@")[1].toLowerCase()))
                        i.remove();
                }
                else if(!slot.getStack().getDisplayName().toLowerCase().contains(currentSearch.toLowerCase()))
                    i.remove();
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

    public void detectAndSendChanges() {
        onCraftMatrixChanged(craftingGrid);
        super.detectAndSendChanges();
    }

    public void onCraftMatrixChanged(IInventory inv) {
        if(craftingGrid != null && inv == craftingGrid)
            craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftingGrid, storage.getWorldObj()));
    }

    public void addCraftingGrid(IInventory inventory, int startSlot, int x, int y, int width, int height) {
        int i = 0;
        for(int h = 0; h < height; h++) {
            for(int w = 0; w < width; w++) {
                this.addSlotToContainer(new Slot(inventory, startSlot + i++, x + (w * 18), y + (h * 18)));
            }
        }
    }

    public void clearCraftingGrid() {
        for(int i = inventorySlots.size() - 10; i < inventorySlots.size(); i++) {
            slotClick(i, 0, 1, ((InventoryPlayer)playerInventory).player);
        }
    }

    public boolean func_94530_a(ItemStack stack, Slot slot) {
        return slot.inventory != this.craftResult && super.func_94530_a(stack, slot);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        Slot slot = (Slot)inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemToTransfer = slot.getStack();
            ItemStack copy = itemToTransfer.copy();
            if (slotId < inventorySize) {
                if (!mergeItemStackSafe(itemToTransfer, inventorySize, storage.hasCraftingUpgrade() ? inventorySlots.size() - 10 : inventorySlots.size(), true)) return null;
            }
            else if(slotId >= inventorySlots.size() - 10 && storage.hasCraftingUpgrade()) {
                if(slotId == inventorySlots.size() - 1) {
                    if(!mergeItemStackSafe(itemToTransfer, 0, storage.hasCraftingUpgrade() ? inventorySlots.size() - 10 : inventorySlots.size(), true)) return null;
                } else {
                    if(!mergeItemStackSafe(itemToTransfer, 0, storage.hasCraftingUpgrade() ? inventorySlots.size() - 10 : inventorySlots.size(), false)) return null;
                }
            }else if (!mergeItemStackSafe(itemToTransfer, 0, inventorySize, false)) return null;

            slot.onSlotChange(itemToTransfer, copy);
            slot.onPickupFromSlot(player, itemToTransfer);

            if (itemToTransfer.stackSize == 0) slot.putStack(null);
            else slot.onSlotChanged();
            onCraftMatrixChanged(craftingGrid);
            if (itemToTransfer.stackSize != copy.stackSize) return copy;
        }
        return null;

    }

    public static class DummyCraftingInventory extends InventoryCrafting {

        protected final TileStorageCore tile;
        protected final ContainerStorageCore container;
        protected ItemStack[] stacks;

        public DummyCraftingInventory(TileStorageCore tile, ContainerStorageCore container) {
            super(null, 3, 3);
            stacks = new ItemStack[9];
            this.tile = tile;
            this.container = container;
        }

        private void onCraftingChanged() {
            container.onCraftMatrixChanged(this);
        }

        @Override
        public int getSizeInventory() {
            return 9;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return slot >= getSizeInventory() ? null : tile.getCraftingGrid().getStackInSlot(slot);
        }

        @Override
        public ItemStack getStackInRowAndColumn(int row, int column) {
            if (row >= 0 && row < 3) {
                int k = row + column * 3;
                return getStackInSlot(k);
            } else
                return null;
        }

        @Override
        public ItemStack getStackInSlotOnClosing(int slot) {
            return tile.getCraftingGrid().getStackInSlot(slot);
        }

        @Override
        public ItemStack decrStackSize(int slot, int amount) {
            if(tile.getCraftingGrid().getStackInSlot(slot) != null) {
                ItemStack returnStack;
                if(tile.getCraftingGrid().getStackInSlot(slot).stackSize <= amount) {
                    returnStack = tile.getCraftingGrid().getStackInSlot(slot);
                    tile.getCraftingGrid().setStackInSlot(null, slot);
                    onCraftingChanged();
                    return returnStack;
                } else {
                    returnStack = tile.getCraftingGrid().getStackInSlot(slot).splitStack(amount);
                    if(tile.getCraftingGrid().getStackInSlot(slot).stackSize <= 0)
                        tile.getCraftingGrid().setStackInSlot(null, slot);
                    onCraftingChanged();
                    return returnStack;
                }
            }
            return null;
        }

        @Override
        public void setInventorySlotContents(int slot, ItemStack stack) {
            tile.getCraftingGrid().setStackInSlot(stack, slot);
            onCraftingChanged();
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
