package com.pauljoda.modularsystems.core.registries;

import com.pauljoda.modularsystems.core.lib.Reference;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigRegistry {

    private static Configuration config;
    private static String configLocation;

    public static int multiblockSize, versionNotify, versionRetry;
    public static boolean debug, updateTab;
    public static String lastVersion, updateURL;

    public static void init(String configFolderLocation) {

        configLocation = configFolderLocation;

        config = new Configuration(new File(configFolderLocation + File.separator + "ModularSystems.cfg"));

        config.load();

        lastVersion         = config.get(Reference.VERSIONCHECK, Reference.REMOTE_VERSION, "").getString();
        versionNotify       = config.get(Reference.VERSIONCHECK, "Notify if out of Date? (0=Always, 1=Once, 2=Never)", 0).getInt();
        updateTab           = config.get(Reference.VERSIONCHECK, Reference.UPDATE_TAB, false).getBoolean();
        versionRetry        = config.get(Reference.VERSIONCHECK, "# of attempts to check for updates?", 3).getInt();
        updateURL           = config.get(Reference.VERSIONCHECK, Reference.UPDATE_URL, "").getString();

        multiblockSize      = config.get(Reference.MULTIBLOCK_CONFIG, "Max Size (Blocks From Core)", 32).getInt();

        debug               = config.get(Reference.DEBUG, "Enable Debug Mode?", false).getBoolean();

        config.save();
    }

    public static void set(String categoryName, String propertyName, String newValue) {
        config.load();
        if (config.getCategoryNames().contains(categoryName.toLowerCase())) {
            if (config.getCategory(categoryName.toLowerCase()).containsKey(propertyName)) {
                config.getCategory(categoryName.toLowerCase()).get(propertyName).set(newValue);
            }
        }
        config.save();
        init(configLocation);
    }

    public static void set(String categoryName, String propertyName, Boolean newValue) {
        config.load();
        if (config.getCategoryNames().contains(categoryName.toLowerCase())) {
            if (config.getCategory(categoryName.toLowerCase()).containsKey(propertyName)) {
                config.getCategory(categoryName.toLowerCase()).get(propertyName).set(newValue);
            }
        }
        config.save();
        init(configLocation);
    }
}
