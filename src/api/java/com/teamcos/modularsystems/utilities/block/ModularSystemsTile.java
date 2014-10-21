package com.teamcos.modularsystems.utilities.block;

import com.teamcos.modularsystems.helpers.Locatable;
import com.teamcos.modularsystems.utilities.tiles.FueledRecipeTile;

public interface ModularSystemsTile extends Locatable {
    FueledRecipeTile getCore();
    void setCore(FueledRecipeTile tile);
}
