package com.qzero.tunnel.client.utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;

public class HttpRequestParam {

    private Map<String,String> paramMap=new HashMap<>();

    public HttpRequestParam() {
    }

    public HttpRequestParam(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public void add(String name,String value){
        paramMap.put(name,value);
    }

    public void remove(String name){
        paramMap.remove(name);
    }

    public List<NameValuePair> toPairList(){
        List<NameValuePair> pairList=new ArrayList<>();

        Set<Map.Entry<String,String>> entries=paramMap.entrySet();
        for(Map.Entry<String,String> entry:entries){
            pairList.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
        }

        return pairList;
    }
}
