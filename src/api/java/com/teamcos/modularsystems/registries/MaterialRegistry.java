package com.teamcos.modularsystems.registries;

import net.minecraft.block.material.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class MaterialRegistry {
    private static Map<String, Material> materials = new LinkedHashMap<String, Material>();

    static {
        registerMaterial("air", Material.air);
        registerMaterial("grass", Material.grass);
        registerMaterial("ground", Material.ground);
        registerMaterial("wood", Material.wood);
        registerMaterial("rock", Material.rock);
        registerMaterial("iron", Material.iron);
        registerMaterial("piston", Material.piston);
        registerMaterial("web", Material.web);
        registerMaterial("cake", Material.cake);
        registerMaterial("portal", Material.portal);
        registerMaterial("dragonEgg", Material.dragonEgg);
        registerMaterial("gourd", Material.gourd);
        registerMaterial("clay", Material.clay);
        registerMaterial("cactus", Material.cactus);
        registerMaterial("craftedSnow", Material.craftedSnow);
    }

    private MaterialRegistry() {    }

    public static void registerMaterial(String name, Material material) {
        materials.put(name, material);
    }

    public static Material retrieveMaterial(String name) {
        return materials.get(name);
    }
}
