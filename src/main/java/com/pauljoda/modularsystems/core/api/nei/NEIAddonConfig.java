package com.pauljoda.modularsystems.core.api.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.api.nei.machines.RecipeHandlerBlockValues;
import com.pauljoda.modularsystems.core.api.nei.machines.RecipeHandlerCrusher;
import com.pauljoda.modularsystems.core.api.nei.machines.RecipeHandlerFurnace;
import com.pauljoda.modularsystems.core.api.nei.storage.GuiStorageCraftingHandler;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.storage.gui.GuiStorageCore;
import net.minecraft.util.StatCollector;

/**
 * This will be called automatically when NEI is loaded. Must be called this exactly for NEI to find it
 *
 * Make sure if you create a handler to register it here
 */
public class NEIAddonConfig implements IConfigureNEI {
    @Override
    public void loadConfig() {

        registerHandler(new RecipeHandlerCrusher());
        registerHandler(new RecipeHandlerFurnace());
        registerHandler(new RecipeHandlerBlockValues());

        API.registerGuiOverlay(GuiStorageCore.class, "crafting");
        API.registerGuiOverlayHandler(GuiStorageCore.class, new GuiStorageCraftingHandler(180, 162), "crafting");

        //By setting this here, we can let the rest of the mod know NEI is installed
        ModularSystems.nei = new NEICallback();
    }

    @Override
    public String getName() {
        return StatCollector.translateToLocal("modularsystems.nei.name");
    }

    @Override
    public String getVersion() {
        return Reference.VERSION;
    }

    /**
     * Little helper method to register the handlers
     * @param handler The handler to register
     */
    private static void registerHandler(TemplateRecipeHandler handler) {
        API.registerRecipeHandler(handler);
        API.registerUsageHandler(handler);
    }
}
