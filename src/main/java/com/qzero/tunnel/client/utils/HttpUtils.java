package com.qzero.tunnel.client.utils;

import com.alibaba.fastjson.JSONObject;
import com.qzero.tunnel.client.data.UserToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;

public class HttpUtils {

    private String tokenId;
    private String username;

    private String baseUrl;

    private static HttpUtils instance;

    private HttpUtils(){

    }

    public static HttpUtils getInstance(){
        if(instance==null)
            instance=new HttpUtils();
        return instance;
    }

    /**
     *
     * @param baseUrl like http://127.0.0.1:8080
     */
    public void setBaseUrl(String baseUrl){
        this.baseUrl=baseUrl;
    }

    public void setAuthInfo(UserToken userToken){
        this.tokenId = userToken.getToken();
        this.username = userToken.getUsername();
    }

    /**
     *
     * @param path like /auth/login
     * @return
     * @throws Exception
     */
    public String doGet(String path) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(baseUrl+path);

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

    /**
     *
     * @param path like /auth/login
     * @param requestParam
     * @return
     * @throws Exception
     */
    public String doPost(String path, HttpRequestParam requestParam) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(baseUrl+path);

        httpPost.setHeader("token_id", tokenId);
        httpPost.setHeader("username", username);

        httpPost.setEntity(new UrlEncodedFormEntity(requestParam.toPairList()));

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

    /**
     *
     * @param path like /auth/login
     * @param obj The content of the post
     * @return
     * @throws Exception
     */
    public String doPost(String path,Object obj) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(baseUrl+path);

        httpPost.setHeader("Content-Type","application/json; charset=UTF-8");
        httpPost.setHeader("token_id", tokenId);
        httpPost.setHeader("username", username);

        String jsonString= JSONObject.toJSONString(obj);
        httpPost.setEntity(new StringEntity(jsonString, Charset.forName("UTF-8")));

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

    /**
     *
     * @param path like /auth/login
     * @param requestParam
     * @return
     * @throws Exception
     */
    public String doPut(String path, HttpRequestParam requestParam) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(baseUrl+path);

        httpPut.setHeader("token_id", tokenId);
        httpPut.setHeader("username", username);

        httpPut.setEntity(new UrlEncodedFormEntity(requestParam.toPairList()));
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

    /**
     *
     * @param path like /auth/login
     * @param obj The content of the put request
     * @return
     * @throws Exception
     */
    public String doPut(String path,Object obj) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(baseUrl+path);

        httpPut.setHeader("Content-Type","application/json; charset=UTF-8");
        httpPut.setHeader("token_id", tokenId);
        httpPut.setHeader("username", username);

        String jsonString=JSONObject.toJSONString(obj);
        httpPut.setEntity(new StringEntity(jsonString,Charset.forName("UTF-8")));
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

    /**
     *
     * @param path like /auth/login
     * @return
     * @throws Exception
     */
    public String doDelete(String path) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(baseUrl+path);

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
