package com.pauljoda.modularsystems.core.api.nei;

import net.minecraft.inventory.Container;

/**
 * Used as an interface for the NEI recipes area on GUIs. This way we won't crash without NEI
 */
public interface INEICallback {
    /**
     * Call this when you want to display the NEI recipes
     * @param gui The gui called from
     */
    void onArrowClicked(Container gui);
}
