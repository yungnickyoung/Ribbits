package com.yungnickyoung.minecraft.ribbits.util;

import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

public class BufferUtils {
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
}
