package net.bliss.tutorialmod.block;

import net.bliss.tutorialmod.TutorialMod;
import net.bliss.tutorialmod.block.custom.JumpyBlock;
import net.bliss.tutorialmod.block.custom.RubidiumLampBlock;
import net.bliss.tutorialmod.item.ModItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;

public class ModBlocks {


    public static final Block RUBIDIUM_BLOCK = registerBlock("rubidium_block",
            new Block(FabricBlockSettings.of(Material.AMETHYST).strength(4f).requiresTool()), ModItemGroup.RUBIDIUM);


    public static final Block RUBIDIUM_ORE = registerBlock("rubidium_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool(),
                    UniformIntProvider.create(3, 7)), ModItemGroup.RUBIDIUM);


    public static final Block DEEPSLATE_RUBIDIUM_ORE = registerBlock("deepslate_rubidium_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool(),
                    UniformIntProvider.create(3, 7)), ModItemGroup.RUBIDIUM);

    public static final Block JUMPY_BLOCK = registerBlock("jumpy_block",
            new JumpyBlock(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool()), ModItemGroup.RUBIDIUM);

    public static final Block RUBIDIUM_LAMP = registerBlock("rubidium_lamp",
            new RubidiumLampBlock(FabricBlockSettings.of(Material.REDSTONE_LAMP).strength(4f).requiresTool()
                    .luminance(state -> state.get(RubidiumLampBlock.LIT) ? 15 : 0)), ModItemGroup.RUBIDIUM);

    private static Block registerBlock(String name, Block block, ItemGroup tab){
        registerblockItem(name, block, tab);
        return Registry.register(Registry.BLOCK, new Identifier(TutorialMod.MOD_ID, name), block);
    }



    private static void registerblockItem(String name, Block block, ItemGroup tab) {
        Registry.register(Registry.ITEM, new Identifier(TutorialMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(tab)));
    }

    public static void registerModBlocks() {
        TutorialMod.LOGGER.debug("Registering ModBlocks for " + TutorialMod.MOD_ID);
    }
}
