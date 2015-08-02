package com.teambr.modularsystems.core.common

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
class CommonProxy {

    /**
     * Called during the preInit phase of the mod loading
     *
     * This is where you would register blocks and such
     */
    def preInit() = {}

    /**
     * Called during the init phase of the mod loading
     *
     * Now that the items and such are loaded, use this chance to use them
     */
    def init() = {}

    /**
     * Called during the postInit phase of the mod loading
     *
     * Usually used to close things opened to load and check for conditions
     */
    def postInit() = {}
}
