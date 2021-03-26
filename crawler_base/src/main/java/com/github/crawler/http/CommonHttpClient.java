package com.github.crawler.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CommonHttpClient implements CustomHttpClient {
    private static final int timeout = (int) TimeUnit.SECONDS.toMillis(20);
    private CloseableHttpClient httpClient;

    public CommonHttpClient(){
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout).setSocketTimeout(timeout).build();
        BasicCookieStore cookieStore = new BasicCookieStore();
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .disableRedirectHandling()
                .setDefaultCookieStore(cookieStore);
        httpClient = httpClientBuilder.build();
    }

    @Override
    public HttpEntity doGet(String url, Map<String, String> headers) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                httpGet.setHeader(header.getKey(), header.getValue());
            }
        }

        try(CloseableHttpResponse httpResponse = httpClient.execute(httpGet)){
            return getHttpEntity(httpResponse, url);
        }

    }

    private HttpEntity getHttpEntity(CloseableHttpResponse httpResponse, String url) throws IOException {
        HttpEntity httpEntity = new HttpEntity(url);

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        httpEntity.setStatusCode(statusCode);
        Header locationHeader = httpResponse.getFirstHeader(HttpHeaders.LOCATION);
        if(null != locationHeader){
            String location = locationHeader.getValue();
            httpEntity.setLocation(location);
        }

        String statusText = httpResponse.getStatusLine().getReasonPhrase();
        List<Header> headers = Arrays.asList(httpResponse.getAllHeaders());
        httpEntity.setStatusText(statusText);
        httpEntity.setHeaders(headers);
        org.apache.http.HttpEntity entity = httpResponse.getEntity();
        ContentType contentType = ContentType.get(entity);
        httpEntity.setContentType(contentType);

        httpEntity.setContent(EntityUtils.toByteArray(entity));
        return httpEntity;
    }

    @Override
    public HttpEntity doPost(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                httpPost.setHeader(header.getKey(), header.getValue());
            }
        }

        if(params!=null){
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (String name : params.keySet()) {
                nvps.add(new BasicNameValuePair(name, params.get(name)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
        }

        try(CloseableHttpResponse httpResponse = httpClient.execute(httpPost)){
            return getHttpEntity(httpResponse, url);
        }

    }

    @Override
    public HttpEntity doGetByProxy(JSONObject headers, String url) throws IOException {

        String url_ = "http://cs.lookxiai.com/proxy/data";
        HashMap<String, String> param = new HashMap<>();
        param.put("sign", "jhkpoikl1930847");
        param.put("url", url);
        if (headers != null) {
            param.put("headers", headers.toJSONString());
        }
        return doPost(url_, param, null);
    }

    @Override
    public HttpEntity doPostByProxy(JSONObject headers, JSONObject body, String url) throws IOException {
        String url_ = "http://cs.lookxiai.com/proxy/post/data";
        HashMap<String, String> param = new HashMap<>();
        param.put("sign", "jhkpoikl1930847");
        param.put("url", url);
        param.put("bodyJson", body.toJSONString());
        if (headers != null) {
            param.put("headers", headers.toJSONString());
        }
        return doPost(url_, param, null);
    }

}
