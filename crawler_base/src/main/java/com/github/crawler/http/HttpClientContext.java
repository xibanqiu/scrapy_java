package com.github.crawler.http;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Map;

public class HttpClientContext {
    private CustomHttpClient customHttpClient;

    public CustomHttpClient getCustomHttpClient() {
        return customHttpClient;
    }

    public void setCustomHttpClient(CustomHttpClient customHttpClient) {
        this.customHttpClient = customHttpClient;
    }

    public HttpEntity doGet(String url) throws IOException {
        return customHttpClient.doGet(url,null);
    }

    public HttpEntity doGet(String url, Map<String,String> headers) throws IOException {
        return customHttpClient.doGet(url, headers);
    }

    public HttpEntity doPost(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        return customHttpClient.doPost(url,params,headers);
    }

    public HttpEntity doGetByProxy(JSONObject headers, String url) throws IOException {
        return customHttpClient.doGetByProxy(headers, url);
    }

    public HttpEntity doPostByProxy(JSONObject headers, JSONObject body, String url) throws IOException {
        return customHttpClient.doPostByProxy(headers, body, url);
    }

}
