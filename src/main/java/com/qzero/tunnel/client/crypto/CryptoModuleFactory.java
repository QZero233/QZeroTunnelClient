package com.qzero.tunnel.client.crypto;

import com.qzero.tunnel.client.crypto.modules.PlainModule;
import com.qzero.tunnel.client.crypto.modules.TestModule;

public class CryptoModuleFactory {

    public static CryptoModule getModule(String name){
        switch (name){
            case "plain":
                return new PlainModule();
            case "test":
                return new TestModule();
            default:
                return null;
        }
    }

}
