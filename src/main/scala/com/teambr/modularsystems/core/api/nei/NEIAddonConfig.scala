package com.teambr.modularsystems.core.api.nei

import codechicken.nei.api.{ API, IConfigureNEI }
import codechicken.nei.recipe.TemplateRecipeHandler
import com.teambr.modularsystems.core.ModularSystems
import com.teambr.modularsystems.core.lib.Reference
import net.minecraft.util.StatCollector

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since August 05, 2015
 */
class NEIAddonConfig extends IConfigureNEI {
    override def loadConfig() : Unit = {

        //By setting this here, we can let the rest of the mod know NEI is installed
        ModularSystems.nei = new NEICallback
    }

    override def getName : String = StatCollector.translateToLocal("modularsystems.nei.name")

    override def getVersion : String = Reference.VERSION

    /**
     * Little helper method to register the handlers
     * @param handler The handler to register
     */
    private def registerHandler(handler : TemplateRecipeHandler) {
        API.registerRecipeHandler(handler)
        API.registerUsageHandler(handler)
    }
}
