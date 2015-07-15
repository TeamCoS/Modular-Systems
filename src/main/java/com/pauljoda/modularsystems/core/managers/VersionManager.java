package com.pauljoda.modularsystems.core.managers;

import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.registries.ConfigRegistry;
import com.teambr.bookshelf.collections.VersionReturn;
import com.teambr.bookshelf.util.VersionChecker;

public class VersionManager {

    private static VersionReturn results;
    // The (publicly available) remote version number authority file
    private final static String REMOTE_VERSION_XML_FILE = "https://raw.githubusercontent.com/TeamCoS/Modular-Systems/master/VersionControl.xml";

    public static void init() throws InterruptedException {
        VersionChecker version = new VersionChecker(REMOTE_VERSION_XML_FILE, Reference.VERSION, ConfigRegistry.versionRetry,
                Reference.MOD_ID);
        Thread t = new Thread(version);
        t.start();
        t.join();
        results = VersionChecker.getResults();

        if (!Reference.VERSION.equalsIgnoreCase(results.newVersion)) {
            ConfigRegistry.set(Reference.VERSIONCHECK, Reference.REMOTE_VERSION, results.newVersion);
            ConfigRegistry.set(Reference.VERSIONCHECK, Reference.UPDATE_URL, results.updateLoc);
            if (ConfigRegistry.versionNotify < 2)
                ConfigRegistry.set(Reference.VERSIONCHECK, Reference.UPDATE_TAB, false);
        } else if (Reference.VERSION.equalsIgnoreCase(results.newVersion))
            ConfigRegistry.set(Reference.VERSIONCHECK, Reference.UPDATE_TAB, true);
    }
}
