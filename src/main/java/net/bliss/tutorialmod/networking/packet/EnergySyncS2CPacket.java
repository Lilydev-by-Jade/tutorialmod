package net.bliss.tutorialmod.networking.packet;

import net.bliss.tutorialmod.block.entity.GemInfusingBlockEntity;
import net.bliss.tutorialmod.screen.GemInfusingScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class EnergySyncS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        long energy = buf.readLong();
        BlockPos position = buf.readBlockPos();

        if(client.world.getBlockEntity(position) instanceof GemInfusingBlockEntity blockEntity) {
            blockEntity.setEnergyLevel(energy);

            if(client.player.currentScreenHandler instanceof GemInfusingScreenHandler screenhandler &&
                    screenhandler.blockEntity.getPos().equals(position)) {
                blockEntity.setEnergyLevel(energy);
            }
        }
    }
}
