package com.teamcos.modularsystems.registries;

import net.minecraft.block.material.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class MaterialRegistry {
//    public static final Material anvil = (new Material(MapColor.ironColor)).setRequiresTool().setImmovableMobility();
//    public static final Material water = (new MaterialLiquid(MapColor.waterColor)).setNoPushMobility();
//    public static final Material lava = (new MaterialLiquid(MapColor.tntColor)).setNoPushMobility();
//    public static final Material leaves = (new Material(MapColor.foliageColor)).setBurning().setTranslucent().setNoPushMobility();
//    public static final Material plants = (new MaterialLogic(MapColor.foliageColor)).setNoPushMobility();
//    public static final Material vine = (new MaterialLogic(MapColor.foliageColor)).setBurning().setNoPushMobility().setReplaceable();
//    public static final Material sponge = new Material(MapColor.clothColor);
//    public static final Material cloth = (new Material(MapColor.clothColor)).setBurning();
//    public static final Material fire = (new MaterialTransparent(MapColor.airColor)).setNoPushMobility();
//    public static final Material sand = new Material(MapColor.sandColor);
//    public static final Material circuits = (new MaterialLogic(MapColor.airColor)).setNoPushMobility();
//    public static final Material carpet = (new MaterialLogic(MapColor.clothColor)).setBurning();
//    public static final Material glass = (new Material(MapColor.airColor)).setTranslucent().setAdventureModeExempt();
//    public static final Material redstoneLight = (new Material(MapColor.airColor)).setAdventureModeExempt();
//    public static final Material tnt = (new Material(MapColor.tntColor)).setBurning().setTranslucent();
//    public static final Material coral = (new Material(MapColor.foliageColor)).setNoPushMobility();
//    public static final Material ice = (new Material(MapColor.iceColor)).setTranslucent().setAdventureModeExempt();
//    public static final Material packedIce = (new Material(MapColor.iceColor)).setAdventureModeExempt();
//    public static final Material snow = (new MaterialLogic(MapColor.snowColor)).setReplaceable().setTranslucent().setRequiresTool().setNoPushMobility();

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
