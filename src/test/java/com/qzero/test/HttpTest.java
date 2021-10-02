package com.qzero.test;

import com.qzero.tunnel.client.utils.HttpRequestParam;
import com.qzero.tunnel.client.utils.HttpUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpTest {

    private Logger log= LoggerFactory.getLogger(getClass());

    private String token="88807164-17ec-4b35-b5b8-b45858dca791";

    private HttpUtils httpUtils;

    @Before
    public void initUtils(){


    }

    @Test
    public void testGet() throws Exception {
        String result=httpUtils.doGet("http://127.0.0.1:8080/tunnel/9990/close");
        log.info(result);
    }

    @Test
    public void testPost() throws Exception {
        HttpRequestParam param=new HttpRequestParam();
        param.add("local_ip","127.0.0.1");
        param.add("local_port","8877");

        String result=httpUtils.doPost("http://127.0.0.1:8080/tunnel/9990", param);
        log.info(result);
    }

}
