package com.pvptraps.network;

import com.pvptraps.PvpTraps;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record MageCastPayload(String abilityId, int targetEntityId) implements CustomPayload {
    public static final Id<MageCastPayload> ID =
            new Id<>(Identifier.of(PvpTraps.MOD_ID, "mage_cast"));
    public static final PacketCodec<RegistryByteBuf, MageCastPayload> CODEC = PacketCodec.of(
            (payload, buf) -> {
                buf.writeString(payload.abilityId(), 64);
                buf.writeVarInt(payload.targetEntityId());
            },
            buf -> new MageCastPayload(buf.readString(64), buf.readVarInt())
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
