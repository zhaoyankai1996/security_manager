package com.inspur.labor.security.util;

import cn.hutool.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author 耿鹏
 */
public class HttpRestUtils {

    /**
     * http post
     */
    public static String post(String url, JSONObject params) throws IOException {
        return httpRestClient(url, HttpMethod.POST, params);
    }


    /**
     * HttpMethod  post/get
     */
    private static String httpRestClient(String url, HttpMethod method, JSONObject params) throws IOException {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10 * 1000);
        requestFactory.setReadTimeout(10 * 1000);
        RestTemplate client = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        headers.set("GREEN-CHANNEL-AUTH", "DzjM45NEfbH8Vjv4FwO8fRXWgiyDmB0hJShOUwHGgcyAoj5MEf8XGmKjhE2np7nlT1wopMLEAZDKDK3RMvjSWSQG3utXd58K8FfjGKBRoVUvkm40JOsYTLdhum4J6eOIfE7mAY5jDtWvzqu6MuaR1mHCFF2AeciKfqHueAVuYV6CkNVXpvWhEkTiOV1dctf4e7DULAPThCktg2Pt4mgvYbaYOtc3lNAYnzNmfAoDE2O8rrWIVQMZ2sOM7krVEbT2gCe73yFBs1miWFoa4wAMHauM8DDnwkRFX9YmVmWBH87rFabmxuqff1Rm53zJvLAaib.NnlrTuSwP24BKkXcuFsaAsFZnjdYAwXzVxZdfb6jEo2");
        HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(params, headers);
        //  执行HTTP请求
        ResponseEntity<String> response = null;
        try {
            response = client.exchange(url, HttpMethod.POST, requestEntity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("------------- 出现异常 HttpClientErrorException -------------");
            System.out.println(e.getMessage());
            System.out.println(e.getStatusText());
            System.out.println("-------------responseBody-------------");
            System.out.println(e.getResponseBodyAsString());
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            System.out.println("------------- HttpRestUtils.httpRestClient() 出现异常 Exception -------------");
            System.out.println(e.getMessage());
            return "";
        }
    }
}
