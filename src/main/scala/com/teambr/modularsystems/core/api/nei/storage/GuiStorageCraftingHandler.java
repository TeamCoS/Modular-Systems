package com.teambr.modularsystems.core.api.nei.storage;

import codechicken.lib.inventory.InventoryUtils;
import codechicken.nei.FastTransferManager;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.recipe.IRecipeHandler;
import com.teambr.modularsystems.storage.gui.GuiStorageCore;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.*;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/26/2015
 */

public class GuiStorageCraftingHandler implements IOverlayHandler {

    public static class DistributedIngredients {
        public DistributedIngredients(ItemStack item) {
            stack = InventoryUtils.copyStack(item, 1);
        }

        public ItemStack stack;
        public int invAmount;
        public int distributed;
        public int numSlots;
        public int recipeAmount;
    }

    public static class IngredientDistribution {
        public IngredientDistribution(DistributedIngredients distrib, ItemStack permutation) {
            this.distrib = distrib;
            this.permutation = permutation;
        }

        public DistributedIngredients distrib;
        public ItemStack permutation;
        public Slot[] slots;
    }

    public GuiStorageCraftingHandler(int x, int y) {
        offsetX = x;
        offsetY = y;
    }

    public GuiStorageCraftingHandler() {
        this(0, 0);
    }

    public int offsetX;
    public int offsetY;

    @Override
    public void overlayRecipe(GuiContainer gui, IRecipeHandler recipe, int recipeIndex, boolean shift) {
        List<PositionedStack> ingredients = recipe.getIngredientStacks(recipeIndex);

        for(PositionedStack stack : ingredients) {
            stack.relx -= 25;
            stack.rely -= 6;
        }

        List<DistributedIngredients> ingredStacks = getPermutationIngredients(ingredients);

        if(!clearIngredients(gui, ingredients))
            return;

        findInventoryQuantities(gui, ingredStacks);

        List<IngredientDistribution> assignedIngredients = assignIngredients(ingredients, ingredStacks);
        if(assignedIngredients == null)
            return;

        assignIngredSlots(gui, ingredients, assignedIngredients);
        int quantity = calculateRecipeQuantity(assignedIngredients);

        if(quantity != 0)
            moveIngredients(gui, assignedIngredients, quantity);

    }

    @SuppressWarnings("unchecked")
    private boolean clearIngredients(GuiContainer gui, List<PositionedStack> ingreds) {
        ((GuiStorageCore)gui).clearCraftingGrid();
        for(PositionedStack pstack : ingreds)
            for(Slot slot : (List<Slot>)gui.inventorySlots.inventorySlots)
                if(slot.xDisplayPosition == pstack.relx + offsetX && slot.yDisplayPosition == pstack.rely + offsetY) {
                    if(!slot.getHasStack())
                        continue;

                    FastTransferManager.clickSlot(gui, slot.slotNumber, 0, 1);
                    if(slot.getHasStack())
                        return false;
                }

        return true;
    }

    @SuppressWarnings("unchecked")
    private void moveIngredients(GuiContainer gui, List<IngredientDistribution> assignedIngredients, int quantity) {
        for(IngredientDistribution distrib : assignedIngredients) {
            ItemStack pstack = distrib.permutation;
            int transferCap = quantity*pstack.stackSize;
            int transferred = 0;

            int destSlotIndex = 0;
            Slot dest = distrib.slots[0];
            int slotTransferred = 0;
            int slotTransferCap = pstack.getMaxStackSize();

            for(Slot slot : (List<Slot>)gui.inventorySlots.inventorySlots) {
                if(!slot.getHasStack() || !canMoveFrom(slot, gui))
                    continue;

                ItemStack stack = slot.getStack();
                if(!InventoryUtils.canStack(stack, pstack))
                    continue;

                FastTransferManager.clickSlot(gui, slot.slotNumber);
                int amount = Math.min(transferCap-transferred, stack.stackSize);
                for(int c = 0; c < amount; c++) {
                    FastTransferManager.clickSlot(gui, dest.slotNumber, 1);
                    transferred++;
                    slotTransferred++;
                    if(slotTransferred >= slotTransferCap) {
                        destSlotIndex++;
                        if(destSlotIndex == distrib.slots.length) {
                            dest = null;
                            break;
                        }
                        dest = distrib.slots[destSlotIndex];
                        slotTransferred = 0;
                    }
                }
                FastTransferManager.clickSlot(gui, slot.slotNumber);
                if(transferred >= transferCap || dest == null)
                    break;
            }
        }
    }

    private int calculateRecipeQuantity(List<IngredientDistribution> assignedIngredients) {
        int quantity = Integer.MAX_VALUE;
        for(IngredientDistribution distrib : assignedIngredients) {
            DistributedIngredients istack = distrib.distrib;
            if(istack.numSlots == 0)
                return 0;

            int allSlots = istack.invAmount;
            if(allSlots/istack.numSlots > istack.stack.getMaxStackSize())
                allSlots = istack.numSlots*istack.stack.getMaxStackSize();

            quantity = Math.min(quantity, allSlots/istack.distributed);
        }
        return quantity;
    }

    private Slot[][] assignIngredSlots(GuiContainer gui, List<PositionedStack> ingredients, List<IngredientDistribution> assignedIngredients) {
        Slot[][] recipeSlots = mapIngredSlots(gui, ingredients);//setup the slot map

        HashMap<Slot, Integer> distribution = new HashMap<Slot, Integer>();
        for(int i = 0; i < recipeSlots.length; i++)
            for(Slot slot : recipeSlots[i])
                if(!distribution.containsKey(slot))
                    distribution.put(slot, -1);

        HashSet<Slot> avaliableSlots = new HashSet<Slot>(distribution.keySet());
        HashSet<Integer> remainingIngreds = new HashSet<Integer>();
        ArrayList<LinkedList<Slot>> assignedSlots = new ArrayList<LinkedList<Slot>>();
        for(int i = 0; i < ingredients.size(); i++)
        {
            remainingIngreds.add(i);
            assignedSlots.add(new LinkedList<Slot>());
        }

        while(avaliableSlots.size() > 0 && remainingIngreds.size() > 0)
        {
            for(Iterator<Integer> iterator = remainingIngreds.iterator(); iterator.hasNext();)
            {
                int i = iterator.next();
                boolean assigned = false;
                DistributedIngredients istack = assignedIngredients.get(i).distrib;

                for(Slot slot : recipeSlots[i])
                {
                    if(avaliableSlots.contains(slot))
                    {
                        avaliableSlots.remove(slot);
                        if(slot.getHasStack())
                            continue;

                        istack.numSlots++;
                        assignedSlots.get(i).add(slot);
                        assigned = true;
                        break;
                    }
                }

                if(!assigned || istack.numSlots*istack.stack.getMaxStackSize() >= istack.invAmount)
                    iterator.remove();
            }
        }

        for(int i = 0; i < ingredients.size(); i++)
            assignedIngredients.get(i).slots = assignedSlots.get(i).toArray(new Slot[0]);
        return recipeSlots;
    }

    private List<IngredientDistribution> assignIngredients(List<PositionedStack> ingredients, List<DistributedIngredients> ingredStacks) {
        ArrayList<IngredientDistribution> assignedIngredients = new ArrayList<>();
        for(PositionedStack posstack : ingredients) {
            DistributedIngredients biggestIngred = null;
            ItemStack permutation = null;
            int biggestSize = 0;
            for(ItemStack pstack : posstack.items) {
                for(int j = 0; j < ingredStacks.size(); j++) {
                    DistributedIngredients istack = ingredStacks.get(j);
                    if(!InventoryUtils.canStack(pstack, istack.stack) || istack.invAmount-istack.distributed < pstack.stackSize)
                        continue;

                    int relsize = (istack.invAmount-istack.invAmount/istack.recipeAmount*istack.distributed)/pstack.stackSize;
                    if(relsize > biggestSize) {
                        biggestSize = relsize;
                        biggestIngred = istack;
                        permutation = pstack;
                        break;
                    }
                }
            }

            if(biggestIngred == null)//not enough ingreds
                return null;

            biggestIngred.distributed+=permutation.stackSize;
            assignedIngredients.add(new IngredientDistribution(biggestIngred, permutation));
        }

        return assignedIngredients;
    }

    @SuppressWarnings("unchecked")
    private void findInventoryQuantities(GuiContainer gui, List<DistributedIngredients> ingredStacks) {
        for(Slot slot : (List<Slot>)gui.inventorySlots.inventorySlots) {
            if(slot.getHasStack() && canMoveFrom(slot, gui)) {
                ItemStack pstack = slot.getStack();
                DistributedIngredients istack = findIngred(ingredStacks, pstack);
                if(istack != null)
                    istack.invAmount+=pstack.stackSize;
            }
        }
    }

    private List<DistributedIngredients> getPermutationIngredients(List<PositionedStack> ingredients) {
        ArrayList<DistributedIngredients> stacks = new ArrayList<>();
        for(PositionedStack positionedStack : ingredients){
            for(ItemStack pstack : positionedStack.items) {
                DistributedIngredients istack = findIngred(stacks, pstack);
                if(istack == null)
                    stacks.add(istack = new DistributedIngredients(pstack));
                istack.recipeAmount+=pstack.stackSize;
            }
        }
        return stacks;
    }

    public boolean canMoveFrom(Slot slot, GuiContainer gui) {
        return true;
    }

    @SuppressWarnings("unchecked")
    public Slot[][] mapIngredSlots(GuiContainer gui, List<PositionedStack> ingredients) {
        Slot[][] recipeSlotList = new Slot[ingredients.size()][];
        for(int i = 0; i < ingredients.size(); i++) {
            LinkedList<Slot> recipeSlots = new LinkedList<>();
            PositionedStack pstack = ingredients.get(i);
            for(Slot slot : (List<Slot>)gui.inventorySlots.inventorySlots) {
                if(slot.xDisplayPosition == pstack.relx + offsetX && slot.yDisplayPosition == pstack.rely + offsetY) {
                    recipeSlots.add(slot);
                    break;
                }
            }
            recipeSlotList[i] = recipeSlots.toArray(new Slot[0]);
        }
        return recipeSlotList;
    }

    public DistributedIngredients findIngred(List<DistributedIngredients> ingredStacks, ItemStack pstack) {
        for(DistributedIngredients istack : ingredStacks)
            if(InventoryUtils.canStack(pstack, istack.stack))
                return istack;
        return null;
    }
}

