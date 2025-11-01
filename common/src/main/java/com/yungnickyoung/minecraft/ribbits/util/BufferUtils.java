package com.yungnickyoung.minecraft.ribbits.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BufferUtils {
    //    StreamCodec<FriendlyByteBuf, UUID> UUID_STREAM_CODEC = StreamCodec.composite(
//            ByteBufCodecs.
//            (buf, uuid) -> buf.writeUUID(uuid)
//    );
    StreamCodec<FriendlyByteBuf, UUID> UUID = new StreamCodec<>() {
        public @NotNull UUID decode(FriendlyByteBuf buf) {
            return buf.readUUID();
        }

        public void encode(FriendlyByteBuf buf, @NotNull UUID uuid) {
            buf.writeUUID(uuid);
        }
    };

    public static List<Integer> readIntList(FriendlyByteBuf buf) {
        int size = buf.readInt();
        List<Integer> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(buf.readInt());
        }
        return list;
    }

    public static void writeIntList(List<Integer> list, FriendlyByteBuf buf) {
        buf.writeInt(list.size());
        for (int n : list) {
            buf.writeInt(n);
        }
    }

    public static List<UUID> readUUIDList(FriendlyByteBuf buf) {
        int size = buf.readInt();
        List<UUID> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(buf.readUUID());
        }
        return list;
    }

    public static void writeUUIDList(List<UUID> list, FriendlyByteBuf buf) {
        buf.writeInt(list.size());
        for (UUID uuid : list) {
            buf.writeUUID(uuid);
        }
    }

    public static List<ResourceLocation> readResourceLocationList(FriendlyByteBuf buf) {
        int size = buf.readInt();
        List<ResourceLocation> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(buf.readResourceLocation());
        }
        return list;
    }

    public static void writeResourceLocationList(List<ResourceLocation> list, FriendlyByteBuf buf) {
        buf.writeInt(list.size());
        for (ResourceLocation resourceLocation : list) {
            buf.writeResourceLocation(resourceLocation);
        }
    }
}
