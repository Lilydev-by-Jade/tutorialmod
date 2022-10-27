package net.bliss.tutorialmod;

import net.bliss.tutorialmod.block.ModBlocks;
import net.bliss.tutorialmod.block.entity.ModBlockEntities;
import net.bliss.tutorialmod.event.PlayerTickHandler;
import net.bliss.tutorialmod.fluid.ModFluids;
import net.bliss.tutorialmod.item.ModItems;
import net.bliss.tutorialmod.networking.ModMessages;
import net.bliss.tutorialmod.painting.ModPaintings;
import net.bliss.tutorialmod.recipe.ModRecipes;
import net.bliss.tutorialmod.screen.ModScreenHandlers;
import net.bliss.tutorialmod.util.ModLootTableModifiers;
import net.bliss.tutorialmod.villager.ModVillagers;
import net.bliss.tutorialmod.world.feature.ModConfiguredFeatures;
import net.bliss.tutorialmod.world.gen.ModOreGeneration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TutorialMod implements ModInitializer {
	public static final String MOD_ID = "tutorialmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModConfiguredFeatures.registerConfiguredFeature();

		ModVillagers.registerVillagers();
		ModVillagers.registerTrades();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		ModPaintings.registerPaintings();
		ModOreGeneration.generateOres();

		ModLootTableModifiers.modifyLootTables();
		ModMessages.registerC2SPackets();

		ModFluids.register();
		ModBlockEntities.registerBlockEntities();

		ModScreenHandlers.registerAllScreenHandlers();
		ModRecipes.registerRecipes();

		ServerTickEvents.START_SERVER_TICK.register(new PlayerTickHandler());
	}
}
