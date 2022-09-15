package net.bliss.tutorialmod.world.feature;

import net.bliss.tutorialmod.TutorialMod;
import net.bliss.tutorialmod.block.ModBlocks;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;

import java.util.List;

public class ModConfiguredFeatures {

    public static final List<OreFeatureConfig.Target> OVERWORLD_RUBIDIUM_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, ModBlocks.RUBIDIUM_ORE.getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_RUBIDIUM_ORE.getDefaultState()));

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> RUBIDIUM_ORE =
            ConfiguredFeatures.register("rubidium_ore", Feature.ORE, new OreFeatureConfig(OVERWORLD_RUBIDIUM_ORES,9));
    public static void registerConfiguredFeature() {
        TutorialMod.LOGGER.debug("Registering the ModConfiguredFeatures for " + TutorialMod.MOD_ID);
    }




}
