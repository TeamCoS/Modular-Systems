package com.teambr.modularsystems.core.client

import com.teambr.modularsystems.core.common.CommonProxy
import com.teambr.modularsystems.core.managers.ItemRenderManager

/**
 * This file was created for the Modular-Systems
 *
 * Modular-Systems if licensed under the is licensed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since August 02, 2015
 */
class ClientProxy extends CommonProxy {

    override def init() = {

        //Register Inventory Renderer for Items
        ItemRenderManager.registerItemRenderer()
    }


}
