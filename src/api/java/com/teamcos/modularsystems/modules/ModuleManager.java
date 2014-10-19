package com.teamcos.modularsystems.modules;

public class ModuleManager
{
    private static boolean furnaceEnabled = false;
    private static boolean storageEnabled = false;
    private static boolean enchantingEnabled = false;
    private static boolean oreProcessingEnabled = false;

    public static boolean isFurnaceEnabled() {
        return furnaceEnabled;
    }

    public static boolean isStorageEnabled() {
        return storageEnabled;
    }

    public static boolean isEnchantingEnabled() {
        return enchantingEnabled;
    }

    public static boolean isOreProcessingEnabled() {
        return oreProcessingEnabled;
    }

    public static void setFurnaceEnabled() {
        furnaceEnabled = true;
    }

    public static void setStorageEnabled() {
        storageEnabled = true;
    }

    public static void setEnchantingEnabled() {
        enchantingEnabled = true;
    }

    public static void setProcessingEnabled() {
        oreProcessingEnabled = true;
    }
}
