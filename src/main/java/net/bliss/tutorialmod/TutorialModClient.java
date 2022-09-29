package net.bliss.tutorialmod;

import net.bliss.tutorialmod.block.ModBlocks;
import net.bliss.tutorialmod.event.KeyInputHandler;
import net.bliss.tutorialmod.networking.ModMessages;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class TutorialModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.EGGPLAND_CROP, RenderLayer.getCutout());
        KeyInputHandler.register();
        ModMessages.registerS2CPackets();
    }
}
