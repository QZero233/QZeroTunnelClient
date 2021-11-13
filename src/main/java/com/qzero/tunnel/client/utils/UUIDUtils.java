package com.qzero.tunnel.client.utils;

import java.util.UUID;

public class UUIDUtils {
    public static String getRandomUUID(){
        UUID uuid=UUID.randomUUID();
        return uuid.toString();
    }
}
