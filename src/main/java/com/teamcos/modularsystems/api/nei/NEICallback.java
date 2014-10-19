package com.teamcos.modularsystems.api.nei;

import codechicken.nei.recipe.GuiCraftingRecipe;
import com.teamcos.modularsystems.furnace.containers.ContainerModularFurnace;
import com.teamcos.modularsystems.furnace.containers.ContainerModularFurnaceCrafter;
import com.teamcos.modularsystems.oreprocessing.container.ContainerModularOreProcessing;
import net.minecraft.inventory.Container;

public class NEICallback implements INEICallback
{
    @Override
    public void onArrowClicked(Container gui)
    {
        if(gui instanceof ContainerModularFurnaceCrafter)
            GuiCraftingRecipe.openRecipeGui("smelting");
        else if(gui instanceof ContainerModularFurnace)
            GuiCraftingRecipe.openRecipeGui("smelting");
        else if(gui instanceof ContainerModularOreProcessing)
            GuiCraftingRecipe.openRecipeGui("modularsystems.oreprocessing");
    }
}
