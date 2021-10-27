package com.qzero.tunnel.client.crypto.modules;


import com.qzero.tunnel.client.crypto.CryptoModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PlainModule implements CryptoModule {

    @Override
    public byte[] encrypt(byte[] data) {
        return data;
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return data;
    }

    @Override
    public void doHandshakeAsClient(InputStream inputStream, OutputStream outputStream) throws IOException {
        CryptoModule.super.doHandshakeAsClient(inputStream, outputStream);
    }

    @Override
    public void doHandshakeAsServer(InputStream inputStream, OutputStream outputStream) throws IOException {
        CryptoModule.super.doHandshakeAsServer(inputStream, outputStream);
    }
}
