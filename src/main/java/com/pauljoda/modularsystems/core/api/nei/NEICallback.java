package com.pauljoda.modularsystems.core.api.nei;

import codechicken.nei.recipe.GuiCraftingRecipe;
import com.pauljoda.modularsystems.crusher.container.ContainerCrusher;
import net.minecraft.inventory.Container;

public class NEICallback implements INEICallback {

    @Override
    public void onArrowClicked(Container gui) {
        if (gui instanceof ContainerCrusher)
            GuiCraftingRecipe.openRecipeGui("crusher");
    }
}
