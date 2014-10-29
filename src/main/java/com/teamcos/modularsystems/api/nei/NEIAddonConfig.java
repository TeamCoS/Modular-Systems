package com.teamcos.modularsystems.api.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.DefaultOverlayHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.teamcos.modularsystems.core.ModularSystems;
import com.teamcos.modularsystems.helpers.OreDictionaryHelper;
import com.teamcos.modularsystems.core.lib.Reference;
import com.teamcos.modularsystems.furnace.gui.GuiModularFurnaceEnabled;
import com.teamcos.modularsystems.registries.OreProcessingRegistry;
import com.teamcos.modularsystems.storage.gui.GuiModularStorage;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class NEIAddonConfig implements IConfigureNEI
{
    @Override
    public void loadConfig()
    {
        initOreProcessingRecipes();

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

    public static void initOreProcessingRecipes()
    {
        String[] oresDefined = OreDictionary.getOreNames();
        for(int i = 0; i < oresDefined.length; i++)
        {
            if(!OreDictionary.getOres(oresDefined[i]).isEmpty() && oresDefined[i].contains("ore"))
            {
                ItemStack input = OreDictionary.getOres(oresDefined[i]).get(0);
                ItemStack output = OreDictionaryHelper.getOreOutput(input);
                if(output != null) OreProcessingRegistry.addOreProcessingRecipe(input, output);
            }
        }
    }
}
