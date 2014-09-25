package com.pauljoda.modularsystems.core.helper;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;

import com.pauljoda.modularsystems.core.lib.Colours;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.lib.Strings;

import cpw.mods.fml.common.Loader;

public class VersionHelper implements Runnable {

	private static VersionHelper instance = new VersionHelper();

	// The (publicly available) remote version number authority file
	final static String REMOTE_VERSION_XML_FILE = "https://raw.githubusercontent.com/pauljoda/Modular-Systems/master/VersionControl.xml";

	static Properties remoteVersionProperties = new Properties();

	// All possible results of the remote version number check
	public final static byte UNINITIALIZED = 0;
	public final static byte CURRENT = 1;
	public final static byte OUTDATED = 2;
	public final static byte ERROR = 3;
	public final static byte FINAL_ERROR = 4;
	public final static byte MC_VERSION_NOT_FOUND = 5;

	// Var to hold the result of the remote version check, initially set to uninitialized
	static byte result = UNINITIALIZED;
	static String remoteVersion = null;
	static String remoteUpdateLocation = null;

	/***
	 * Checks the version of the currently running instance of the mod against
	 * the remote version authority, and sets the result of the check
	 * appropriately
	 */
	public static void checkVersion() {

		InputStream remoteVersionRepoStream = null;
		result = UNINITIALIZED;

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
					result = ERROR;
				}

				if (remoteVersion != null) {
					if (!ConfigHelper.LAST_DISCOVERED_VERSION.equalsIgnoreCase(remoteVersion)) {
						ConfigHelper.set(Configuration.CATEGORY_GENERAL, ConfigHelper.LAST_DISCOVERED_VERSION_CONFIGNAME, remoteVersion);
					}

					if (remoteVersion.equalsIgnoreCase(getVersionForCheck())) 
					{
						result = CURRENT;
					}
					else {
						result = OUTDATED;
					}
				}

			}
			else {
				result = MC_VERSION_NOT_FOUND;
			}
		}
		catch (Exception e) {
		}
		finally {
			if (result == UNINITIALIZED) {
				result = ERROR;
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

	private static String getVersionForCheck() {

		String[] versionTokens = Reference.VERSION.split(" ");

		if (versionTokens.length >= 1)
			return versionTokens[0];
		else
			return Reference.VERSION;
	}

	public static void logResult() {

		if (result == CURRENT || result == OUTDATED) {
			LogHelper.info(getResultMessage());
		}
		else {
			LogHelper.warn(getResultMessage());
		}
	}

	public static String getResultMessage() {

		if (result == UNINITIALIZED) {
			return StatCollector.translateToLocal(Strings.UNINITIALIZED_MESSAGE);
		}
		else if (result == CURRENT) {
			return StatCollector.translateToLocalFormatted(Strings.CURRENT_MESSAGE, Reference.MOD_NAME, Loader.instance().getMCVersionString());
		}
		else if (result == OUTDATED && remoteVersion != null && remoteUpdateLocation != null) {
			return StatCollector.translateToLocalFormatted(Strings.OUTDATED_MESSAGE, Reference.MOD_NAME, remoteVersion, Loader.instance().getMCVersionString(), remoteUpdateLocation);
		}
		else if (result == ERROR) {
			return StatCollector.translateToLocal(Strings.GENERAL_ERROR_MESSAGE);
		}
		else if (result == FINAL_ERROR) {
			return StatCollector.translateToLocal(Strings.FINAL_ERROR_MESSAGE);
		}
		else if (result == MC_VERSION_NOT_FOUND) {
			return StatCollector.translateToLocalFormatted(Strings.MC_VERSION_NOT_FOUND, Reference.MOD_NAME, Loader.instance().getMCVersionString());
		}
		else {
			result = ERROR;
			return StatCollector.translateToLocal(Strings.GENERAL_ERROR_MESSAGE);
		}
	}

	public static String getResultMessageForClient() {

		return StatCollector.translateToLocalFormatted(Strings.OUTDATED_MESSAGE, Colours.TEXT_COLOUR_PREFIX_YELLOW + Reference.MOD_NAME + Colours.TEXT_COLOUR_PREFIX_WHITE, Colours.TEXT_COLOUR_PREFIX_YELLOW + remoteVersion + Colours.TEXT_COLOUR_PREFIX_WHITE, Colours.TEXT_COLOUR_PREFIX_YELLOW + Loader.instance().getMCVersionString() + Colours.TEXT_COLOUR_PREFIX_WHITE, Colours.TEXT_COLOUR_PREFIX_YELLOW + remoteUpdateLocation + Colours.TEXT_COLOUR_PREFIX_WHITE);
	}

	public static byte getResult() {

		return result;
	}

	@Override
	public void run() {

		int count = 0;

		LogHelper.info(StatCollector.translateToLocalFormatted(Strings.VERSION_CHECK_INIT_LOG_MESSAGE, REMOTE_VERSION_XML_FILE));

		try {
			while (count < ConfigHelper.VERSION_CHECK_ATTEMPTS - 1 && (result == UNINITIALIZED || result == ERROR)) {

				checkVersion();
				count++;
				logResult();

				if (result == UNINITIALIZED || result == ERROR) {
					Thread.sleep(10000);
				}
			}

			if (result == ERROR) {
				result = FINAL_ERROR;
				logResult();
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static void execute() {

		new Thread(instance).start();
	}
}	

