package com.blakebr0.ironjetpacks.lib;

import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.List;

public final class ModJetpacks {
    private static final Jetpack WOOD = new Jetpack("wood", 0, 0x83663C, 1, 15, "tag:minecraft:planks", 0F, 0F);
    private static final Jetpack STONE = new Jetpack("stone", 1, 0x7F7F7F, 2, 12, "tag:forge:stone", 0F, 0F);
    private static final Jetpack COPPER = new Jetpack("copper", 1, 0xEE825B, 2, 12, "minecraft:copper_ingot", 0F, 0F);
    private static final Jetpack IRON = new Jetpack("iron", 2, 0xD8D8D8, 3, 9, "tag:forge:ingots/iron", 0F, 0F);
    private static final Jetpack GOLD = new Jetpack("gold", 3, 0xF9EB59, 2, 25, "tag:forge:ingots/gold", 0F, 0F);
    private static final Jetpack DIAMOND = new Jetpack("diamond", 4, 0x4CF4E0, 4, 10, "tag:forge:gems/diamond", 0F, 0F);
    private static final Jetpack EMERALD = new Jetpack("emerald", 5, 0x4DD979, 4, 15, "tag:forge:gems/emerald", 0F, 0F);
    private static final Jetpack CREATIVE = new Jetpack("creative", 0, 0xCF1AE9, 8, 0, "null", 0F, 0F).setCreative();

    private static final Jetpack BRONZE = new Jetpack("bronze", 2, 0xDA9839, 3, 9, "tag:forge:ingots/bronze", 0F, 0F);
    private static final Jetpack SILVER = new Jetpack("silver", 2, 0x8B99A4, 3, 12, "tag:forge:ingots/silver", 0F, 0F);
    private static final Jetpack STEEL = new Jetpack("steel", 3, 0x565656, 3, 15, "tag:forge:ingots/steel", 0F, 0F);
    private static final Jetpack ELECTRUM = new Jetpack("electrum", 3, 0xF6F395, 2, 18, "tag:forge:ingots/electrum", 0F, 0F);
    private static final Jetpack INVAR = new Jetpack("invar", 3, 0xA4B2A9, 3, 15, "tag:forge:ingots/invar", 0F, 0F);
    private static final Jetpack PLATINUM = new Jetpack("platinum", 4, 0xA6E9FF, 4, 12, "tag:forge:ingots/platinum", 0F, 0F);

    static {
        WOOD.setStats(20000, 32, 0.18D, 0.10D, 0.06D, 0.16D, 0.25D, 0.14D, 1.0D, 1.0D, 1.0D);
        STONE.setStats(100000, 70, 0.25D, 0.11D, 0.08D, 0.18D, 0.25D, 0.1D, 1.0D, 1.0D, 1.0D);
        COPPER.setStats(250000, 85, 0.29D, 0.11D, 0.1D, 0.23D, 0.25D, 0.092D, 1.05D, 1.025D, 1.4D);
        IRON.setStats(800000, 120, 0.41D, 0.12D, 0.14D, 0.27D, 0.25D, 0.075D, 1.1D, 1.05D, 2.1D);
        GOLD.setStats(10000000, 300, 0.61D, 0.13D, 0.15D, 0.34D, 0.25D, 0.03D, 1.5D, 1.25D, 3.2D);
        DIAMOND.setStats(30000000, 650, 0.90D, 0.15D, 0.19D, 0.41D, 0.25D, 0.005D, 1.8D, 1.4D, 3.8D);
        EMERALD.setStats(48000000, 880, 1.03D, 0.17D, 0.21D, 0.45D, 0.25D, 0.0D, 2.0D, 1.5D, 4.0D);
        CREATIVE.setStats(0, 0, 1.03D, 0.17D, 0.21D, 0.45D, 0.25D, 0.0D, 2.0D, 1.5D, 0.0D);

        BRONZE.setStats(800000, 120, 0.41D, 0.12D, 0.14D, 0.27D, 0.25D, 0.075D, 1.1D, 1.05D, 2.1D);
        SILVER.setStats(1200000, 150, 0.48D, 0.13D, 0.15D, 0.3D, 0.25D, 0.07D, 1.3D, 1.15D, 2.7D);
        STEEL.setStats(12000000, 350, 0.67D, 0.135D, 0.155D, 0.35D, 0.25D, 0.025D, 1.5D, 1.25D, 3.2D);
        ELECTRUM.setStats(10000000, 310, 0.79D, 0.14D, 0.17D, 0.37D, 0.25D, 0.03D, 1.6D, 1.3D, 3.5D);
        INVAR.setStats(12000000, 350, 0.61D, 0.13D, 0.15D, 0.34D, 0.25D, 0.03D, 1.5D, 1.25D, 3.2D);
        PLATINUM.setStats(36000000, 720, 0.92D, 0.155D, 0.193D, 0.42D, 0.25D, 0.005D, 1.8D,  1.4D, 3.8D);
    }

    public static List<Jetpack> getDefaults() {
        var defaults = Lists.newArrayList(
                WOOD,
                STONE,
                COPPER,
                IRON,
                GOLD,
                DIAMOND,
                EMERALD,
                CREATIVE,

                BRONZE,
                SILVER,
                STEEL,
                ELECTRUM,
                INVAR,
                PLATINUM
        );

        defaults.sort(Comparator.comparingInt(Jetpack::getTier));

        return defaults;
    }
}
