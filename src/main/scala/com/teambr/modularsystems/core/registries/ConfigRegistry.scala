package com.teambr.modularsystems.core.registries

import java.io.File

import com.teambr.modularsystems.core.ModularSystems
import com.teambr.modularsystems.core.lib.Reference
import net.minecraftforge.common.config.Configuration

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 10, 2015
 */
object ConfigRegistry {

    var config = new Configuration(new File(ModularSystems.configFolderLocation + File.separator + "ModularSystems.cfg"))
    var rfPower: Double = 0.0

    def preInit(): Unit = {

        config.load()

        rfPower         = config.get(Reference.POWER, "RedstoneFlux (RF) to 1 BurnTime", 8.0).getDouble

        config.save()
    }
}

