package com.pauljoda.modularsystems.api.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.DefaultOverlayHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.helper.OreDictionaryHelper;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.furnace.gui.GuiModularFurnaceEnabled;
import com.pauljoda.modularsystems.storage.gui.GuiModularStorage;

public class NEIAddonConfig implements IConfigureNEI
{
    @Override
    public void loadConfig()
    {
        OreDictionaryHelper.initOreProcessingRecipes();

        API.registerGuiOverlay(GuiModularFurnaceEnabled.class, "crafting");
        API.registerGuiOverlayHandler(GuiModularFurnaceEnabled.class, new DefaultOverlayHandler(7,3), "crafting");

        API.registerGuiOverlay(GuiModularStorage.class, "crafting");
        API.registerGuiOverlayHandler(GuiModularStorage.class, new AllSlotsOverlay(-19, 148), "crafting");

        registerHandler(new RecipeHandlerOreProcessing());
        ModularSystems.nei = new NEICallback();
    }

    @Override
    public String getName()
    {
        return "Modular Systems";
    }

    @Override
    public String getVersion()
    {
        return Reference.VERSION;
    }

    private static void registerHandler (TemplateRecipeHandler handler)
    {
        API.registerRecipeHandler(handler);
        API.registerUsageHandler(handler);
    }
}
