package com.pvptraps.network;

import com.pvptraps.PvpTraps;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record MageManaPayload(int current, int maximum) implements CustomPayload {
    public static final Id<MageManaPayload> ID = new Id<>(Identifier.of(PvpTraps.MOD_ID, "mage_mana"));
    public static final PacketCodec<RegistryByteBuf, MageManaPayload> CODEC = PacketCodec.of(
            (value, buf) -> {
                buf.writeVarInt(value.current());
                buf.writeVarInt(value.maximum());
            },
            buf -> new MageManaPayload(buf.readVarInt(), buf.readVarInt())
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}