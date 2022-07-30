package net.bliss.tutorialmod.item;

import net.bliss.tutorialmod.TutorialMod;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup RUBIDIUM = FabricItemGroupBuilder.build(new Identifier(TutorialMod.MOD_ID, "rubidium"),
            () -> new ItemStack(ModItems.RUBIDIUM));
}
