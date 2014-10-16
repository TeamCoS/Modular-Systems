package com.pauljoda.modularsystems.storage;

import com.pauljoda.modularsystems.helpers.Locatable;
import net.minecraft.inventory.IInventory;

public interface TileEntityStorageCore extends IInventory, Locatable {
    void sortInventoryAlphabetically();

    void sortInventoryByIndex();

    boolean hasSpecificUpgrade(int upgradeId);

    int getInventoryRows();

    void setInventoryRows(int i);

    void setGuiDisplayName(String displayName);
}
