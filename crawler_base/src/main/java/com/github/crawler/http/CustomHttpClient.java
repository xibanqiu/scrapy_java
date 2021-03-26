package com.github.crawler.http;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Map;

public interface CustomHttpClient {
    public HttpEntity doGet(String url, Map<String, String> headers) throws IOException;

    public HttpEntity doPost(String url, Map<String, String> params, Map<String, String> headers) throws IOException;

    public HttpEntity doGetByProxy(JSONObject headers, String url) throws IOException;

    public HttpEntity doPostByProxy(JSONObject headers, JSONObject body, String url) throws IOException;

}
