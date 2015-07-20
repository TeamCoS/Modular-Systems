package com.pauljoda.modularsystems.core.api.nei;

import codechicken.nei.recipe.GuiCraftingRecipe;
import com.pauljoda.modularsystems.crusher.container.ContainerCrusher;
import com.pauljoda.modularsystems.furnace.container.ContainerModularFurnace;
import net.minecraft.inventory.Container;

/**
 * Our callback for NEI. Use this to open NEI recipe sections for the gui send
 */
public class NEICallback implements INEICallback {

    @Override
    public void onArrowClicked(Container gui) {
        if (gui instanceof ContainerCrusher)
            GuiCraftingRecipe.openRecipeGui("crusher");
        else if(gui instanceof ContainerModularFurnace)
            GuiCraftingRecipe.openRecipeGui("furnace");
    }
}
