package com.teamcos.modularsystems.registries;

import java.util.LinkedHashSet;
import java.util.Set;

public class BannedOreProcessorBlockRegistry {
    private static final Set<String> bannedBlocks = new LinkedHashSet<String>();

    public static void banBlock(String blockName) {
        bannedBlocks.add(blockName);
    }

    public static boolean isBanned(String blockName) {
        return bannedBlocks.contains(blockName);
    }
}
