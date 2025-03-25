package com.blakebr0.ironjetpacks.config;

import com.blakebr0.cucumber.util.FeatureFlag;
import com.blakebr0.cucumber.util.FeatureFlags;
import com.blakebr0.ironjetpacks.IronJetpacks;

@FeatureFlags
public final class ModFeatureFlags {
    public static final FeatureFlag CAPACITOR_RECIPES = FeatureFlag.create(IronJetpacks.resource("capacitor_recipes"), ModConfigs.ENABLE_CAPACITOR_RECIPES);
    public static final FeatureFlag CELL_RECIPES = FeatureFlag.create(IronJetpacks.resource("cell_recipes"), ModConfigs.ENABLE_CELL_RECIPES);
    public static final FeatureFlag CURIOS_INTEGRATION = FeatureFlag.create(IronJetpacks.resource("curios_integration"), ModConfigs.ENABLE_CURIOS_INTEGRATION);
    public static final FeatureFlag ENCHANTABLE_JETPACKS = FeatureFlag.create(IronJetpacks.resource("enchantable_jetpacks"), ModConfigs.ENCHANTABLE_JETPACKS);
    public static final FeatureFlag HUD = FeatureFlag.create(IronJetpacks.resource("hud"), ModConfigs.ENABLE_HUD);
    public static final FeatureFlag JETPACK_PARTICLES = FeatureFlag.create(IronJetpacks.resource("jetpack_particles"), ModConfigs.ENABLE_JETPACK_PARTICLES);
    public static final FeatureFlag JETPACK_RECIPES = FeatureFlag.create(IronJetpacks.resource("jetpack_recipes"), ModConfigs.ENABLE_JETPACK_RECIPES);
    public static final FeatureFlag JETPACK_SOUNDS = FeatureFlag.create(IronJetpacks.resource("jetpack_sounds"), ModConfigs.ENABLE_JETPACK_SOUNDS);
    public static final FeatureFlag THRUSTER_RECIPES = FeatureFlag.create(IronJetpacks.resource("thruster_recipes"), ModConfigs.ENABLE_THRUSTER_RECIPES);
}
