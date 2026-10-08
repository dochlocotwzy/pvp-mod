package com.pvptraps.network;

import com.pvptraps.PvpTraps;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record MageFeedbackPayload(int cue, int targetEntityId) implements CustomPayload {
    public static final Id<MageFeedbackPayload> ID =
            new Id<>(Identifier.of(PvpTraps.MOD_ID, "mage_feedback"));
    public static final PacketCodec<RegistryByteBuf, MageFeedbackPayload> CODEC = PacketCodec.of(
            (payload, buf) -> {
                buf.writeVarInt(payload.cue());
                buf.writeVarInt(payload.targetEntityId());
            },
            buf -> new MageFeedbackPayload(buf.readVarInt(), buf.readVarInt())
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
