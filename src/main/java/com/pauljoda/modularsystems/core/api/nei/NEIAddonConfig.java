package com.pauljoda.modularsystems.core.api.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.api.nei.machines.RecipeHandlerCrusher;
import com.pauljoda.modularsystems.core.api.nei.machines.RecipeHandlerFurnace;
import com.pauljoda.modularsystems.core.lib.Reference;
import net.minecraft.util.StatCollector;

public class NEIAddonConfig implements IConfigureNEI {
    @Override
    public void loadConfig() {

        registerHandler(new RecipeHandlerCrusher());
        registerHandler(new RecipeHandlerFurnace());

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

    private static void registerHandler(TemplateRecipeHandler handler) {
        API.registerRecipeHandler(handler);
        API.registerUsageHandler(handler);
    }
}
