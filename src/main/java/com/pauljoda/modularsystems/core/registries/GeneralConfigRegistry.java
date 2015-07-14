package com.pauljoda.modularsystems.core.registries;

import com.pauljoda.modularsystems.core.lib.Reference;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class GeneralConfigRegistry {

    public static int multiblockSize;
    public static boolean debug;

    public static void init(String configFolderLocation) {

        Configuration config = new Configuration(new File(configFolderLocation + File.separator + "ModularSystems.cfg"));

        config.load();

        multiblockSize      = config.get(Reference.MULTIBLOCK_CONFIG, "Max Size (Blocks From Core)", 32).getInt();

        debug               = config.get(Reference.DEBUG, "Enable Debug Mode", false).getBoolean();

        config.save();

    }

}
