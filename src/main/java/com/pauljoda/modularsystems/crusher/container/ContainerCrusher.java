package com.pauljoda.modularsystems.crusher.container;

import com.teambr.bookshelf.inventory.BaseContainer;
import net.minecraft.inventory.IInventory;

public class ContainerCrusher extends BaseContainer {

    public ContainerCrusher(IInventory playerInventory, IInventory ownerInventory) {
        super(playerInventory, ownerInventory);
    }
}
