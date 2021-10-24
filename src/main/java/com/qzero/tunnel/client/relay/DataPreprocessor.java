package com.qzero.tunnel.client.relay;

public interface DataPreprocessor {

    byte[] beforeSent(byte[] data);

    byte[] afterReceived(byte[] data);

}
