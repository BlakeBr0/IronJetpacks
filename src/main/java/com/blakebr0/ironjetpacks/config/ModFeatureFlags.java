package com.blakebr0.ironjetpacks.config;

import com.blakebr0.cucumber.util.FeatureFlag;
import com.blakebr0.ironjetpacks.IronJetpacks;
import net.minecraft.resources.ResourceLocation;

public final class ModFeatureFlags {
    public static final FeatureFlag CAPACITOR_RECIPES = FeatureFlag.create(new ResourceLocation(IronJetpacks.MOD_ID, "capacitor_recipes"), ModConfigs.ENABLE_CAPACITOR_RECIPES);
    public static final FeatureFlag CELL_RECIPES = FeatureFlag.create(new ResourceLocation(IronJetpacks.MOD_ID, "cell_recipes"), ModConfigs.ENABLE_CELL_RECIPES);
    public static final FeatureFlag CURIOS_INTEGRATION = FeatureFlag.create(new ResourceLocation(IronJetpacks.MOD_ID, "curios_integration"), ModConfigs.ENABLE_CURIOS_INTEGRATION);
    public static final FeatureFlag ENCHANTABLE_JETPACKS = FeatureFlag.create(new ResourceLocation(IronJetpacks.MOD_ID, "enchantable_jetpacks"), ModConfigs.ENCHANTABLE_JETPACKS);
    public static final FeatureFlag HUD = FeatureFlag.create(new ResourceLocation(IronJetpacks.MOD_ID, "hud"), ModConfigs.ENABLE_HUD);
    public static final FeatureFlag JETPACK_PARTICLES = FeatureFlag.create(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack_particles"), ModConfigs.ENABLE_JETPACK_PARTICLES);
    public static final FeatureFlag JETPACK_RECIPES = FeatureFlag.create(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack_recipes"), ModConfigs.ENABLE_JETPACK_RECIPES);
    public static final FeatureFlag JETPACK_SOUNDS = FeatureFlag.create(new ResourceLocation(IronJetpacks .MOD_ID, "jetpack_sounds"), ModConfigs.ENABLE_JETPACK_SOUNDS);
    public static final FeatureFlag THRUSTER_RECIPES = FeatureFlag.create(new ResourceLocation(IronJetpacks.MOD_ID, "thruster_recipes"), ModConfigs.ENABLE_THRUSTER_RECIPES);
}
