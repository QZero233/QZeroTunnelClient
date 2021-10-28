package com.qzero.tunnel.client.crypto.modules;

import com.qzero.tunnel.client.crypto.CryptoModule;

public class TestModule implements CryptoModule {
    @Override
    public byte[] encrypt(byte[] data) {
        if(data.length>=1)
            data[0]+=3;
        return data;
    }

    @Override
    public byte[] decrypt(byte[] data) {
        if(data.length>=1)
            data[0]-=3;
        return data;
    }
}
