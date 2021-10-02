package com.qzero.tunnel.client.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class NormalHttpUtils {

    public static String doGet(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse httpResponse;

        httpResponse = httpClient.execute(httpGet);
        HttpEntity entity = httpResponse.getEntity();
        String result = EntityUtils.toString(entity);

        if (httpResponse != null) {
            httpResponse.close();
        }
        if (null != httpClient) {
            httpClient.close();
        }

        return result;
    }

}
