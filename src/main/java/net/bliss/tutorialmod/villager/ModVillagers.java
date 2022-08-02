package net.bliss.tutorialmod.villager;

import com.google.common.collect.ImmutableSet;
import net.bliss.tutorialmod.TutorialMod;
import net.bliss.tutorialmod.block.ModBlocks;
import net.bliss.tutorialmod.item.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.function.Predicate;

public class ModVillagers {

    public static final PointOfInterestType JUMPY_POI = registerPOI("jumpy_poi", ModBlocks.JUMPY_BLOCK);
    public static final VillagerProfession JUMP_MASTER = registerProffession("jumpmaster",
            RegistryKey.of(Registry.POINT_OF_INTEREST_TYPE_KEY, new Identifier(TutorialMod.MOD_ID, "jumpy_poi")));
    public static VillagerProfession registerProffession(String name, RegistryKey<PointOfInterestType> type) {
        return Registry.register(Registry.VILLAGER_PROFESSION, new Identifier(TutorialMod.MOD_ID, name),
                VillagerProfessionBuilder.create().id(new Identifier(TutorialMod.MOD_ID, name)).workstation(type)
                        .workSound(SoundEvents.ENTITY_VILLAGER_WORK_ARMORER).build());
    }

    public static PointOfInterestType registerPOI(String name, Block block){
        return PointOfInterestHelper.register(new Identifier(TutorialMod.MOD_ID, name),
                1, 1, ImmutableSet.copyOf(block.getStateManager().getStates()));
    }
    public static void registerVillagers() {
        TutorialMod.LOGGER.debug("Registering Villagers for " + TutorialMod.MOD_ID);
    }

    public static void registerTrades() {
        TradeOfferHelper.registerVillagerOffers(JUMP_MASTER, 1,
                factories -> {
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 3),
                            new ItemStack(ModItems.EGGPLANT, 5),
                            6, 2, 0.02f
                    )));
                });
    }
}
