package com.pauljoda.modularsystems.core.functions;

import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.registries.GeneralConfigRegistry;
import com.teambr.bookshelf.helpers.LogHelper;
import cpw.mods.fml.common.Loader;
import net.minecraft.util.StatCollector;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class VersionCheckerFunction implements Runnable {
    private static VersionCheckerFunction INSTANCE = new VersionCheckerFunction();

    // The (publicly available) remote version number authority file
    final static String REMOTE_VERSION_XML_FILE = "https://raw.githubusercontent.com/TeamCoS/Modular-Systems/master/VersionControl.xml";

    static Properties remoteVersionProperties = new Properties();

    // All possible results of the remote version number check
    enum Results {
        UNINITIALIZED,
        CURRENT,
        OUTDATED,
        ERROR,
        FINAL_ERROR,
        MC_VERSION_NOT_FOUND
    }

    // Var to hold the result of the remote version check, initially set to uninitialized
    static Results result = Results.UNINITIALIZED;
    static String remoteVersion = null;
    static String remoteUpdateLocation = null;

    public static void checkVersion() {
        InputStream remoteVersionRepoStream = null;
        result = Results.UNINITIALIZED;

        try {
            URL remoteVersionURL = new URL(REMOTE_VERSION_XML_FILE);
            remoteVersionRepoStream = remoteVersionURL.openStream();
            remoteVersionProperties.loadFromXML(remoteVersionRepoStream);

            String remoteVersionProperty = remoteVersionProperties.getProperty(Loader.instance().getMCVersionString());

            if (remoteVersionProperty != null) {
                String[] remoteVersionTokens = remoteVersionProperty.split("\\|");

                if (remoteVersionTokens.length >= 2) {
                    remoteVersion = remoteVersionTokens[0];
                    remoteUpdateLocation = remoteVersionTokens[1];
                }
                else {
                    result = Results.ERROR;
                }

                if (remoteVersion != null) {
                    if (remoteVersion.equalsIgnoreCase(GeneralConfigRegistry.lastVersion))
                    {
                        result = Results.CURRENT;
                    }
                    else {
                        result = Results.OUTDATED;
                    }

                    if (!GeneralConfigRegistry.lastVersion.equalsIgnoreCase(remoteVersion)) {
                        GeneralConfigRegistry.set(Reference.VERSIONCHECK, Reference.REMOTE_VERSION, remoteVersion);
                        if (GeneralConfigRegistry.versionNotify < 2)
                            GeneralConfigRegistry.set(Reference.VERSIONCHECK, Reference.UPDATE_TAB, false);
                    }
                }

            }
            else {
                result = Results.MC_VERSION_NOT_FOUND;
            }
        }
        catch (Exception e) {
        }
        finally {
            if (result == Results.UNINITIALIZED) {
                result = Results.ERROR;
            }

            try {
                if (remoteVersionRepoStream != null) {
                    remoteVersionRepoStream.close();
                }
            }
            catch (Exception ex) {
            }
        }
    }

    @Override
    public void run() {
        int tries = 0;
        LogHelper.info(StatCollector.translateToLocal("modularsystems.versioncheck.start"));
        try {
            while (tries < GeneralConfigRegistry.versionRetry) {
                checkVersion();
                tries++;
                if (result == Results.ERROR || result == Results.UNINITIALIZED) {
                    Thread.sleep(10000);
                }
            }
            if (result == Results.ERROR) {
                result = Results.FINAL_ERROR;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void execute() {
        new Thread(INSTANCE).start();
    }
}
