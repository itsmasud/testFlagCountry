package com.fieldnation.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 3/10/2016.
 */
public class DataUtils {
    private static final List<byte[]> PACKET_QUEUE = new LinkedList<>();

    static {
        synchronized (PACKET_QUEUE) {
            PACKET_QUEUE.add(new byte[1024]);
            PACKET_QUEUE.add(new byte[1024]);
        }
    }

    public static byte[] allocPacket() {
        synchronized (PACKET_QUEUE) {
            if (PACKET_QUEUE.size() > 0) {
                return PACKET_QUEUE.remove(0);
            }

            return new byte[1024];
        }
    }

    public static void freePacket(byte[] packet) {
        if (packet != null)
            synchronized (PACKET_QUEUE) {
                PACKET_QUEUE.add(packet);
            }
    }
}
