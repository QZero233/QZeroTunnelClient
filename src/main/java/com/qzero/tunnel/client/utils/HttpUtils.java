package com.qzero.tunnel.client.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

    private String tokenId;
    private String username;

    public HttpUtils(String tokenId, String username) {
        this.tokenId = tokenId;
        this.username = username;
    }

    public String doGet(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        httpGet.setHeader("token_id", tokenId);
        httpGet.setHeader("username", username);

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

    public String doPost(String url, String parameter) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader("token_id", tokenId);
        httpPost.setHeader("username", username);

        httpPost.setEntity(new StringEntity(parameter));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
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


    public String doPut(String url, String parameter) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);

        httpPut.setHeader("token_id", tokenId);
        httpPut.setHeader("username", username);

        httpPut.setEntity(new StringEntity(parameter));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPut);
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

    public String doDelete(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url);

        httpDelete.setHeader("token_id", tokenId);
        httpDelete.setHeader("username", username);

        CloseableHttpResponse httpResponse = httpClient.execute(httpDelete);
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
