package com.github.crawler.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CommonHttpClientExt implements CustomHttpClient {
    private static final int timeout = (int) TimeUnit.SECONDS.toMillis(20);
    private CloseableHttpClient httpClient;

    public CommonHttpClientExt(){
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

    public HttpEntity doGet(JSONObject headers ,String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);

        if (headers != null) {
        	Map<String,Object> jsonToMap =  JSONObject.parseObject(headers.toJSONString());
        	 for (String name :jsonToMap.keySet()) {
        		 httpGet.setHeader(name, jsonToMap.get(name).toString());
            }
        }

        try(CloseableHttpResponse httpResponse = httpClient.execute(httpGet)){
            return getHttpEntity(httpResponse, url);
        }

    }

    public HttpEntity doPost( String token , String referer ,String url,String body) throws IOException {

    	HttpPost httpPost = new HttpPost(url);

		httpPost.addHeader("accept","application/json, text/plain, */*");
		httpPost.addHeader("accept-encoding","gzip, deflate, br");
		httpPost.addHeader("accept-language","zh-CN,zh;q=0.9");
		httpPost.addHeader("authorization",token);
		httpPost.addHeader("content-type","application/json;charset=UTF-8");
		httpPost.addHeader("cookie","__stripe_mid=06986f53-e033-4b1b-8d66-a293c203fa3e; _gcl_au=1.1.1964887636.1583134501; _ga=GA1.2.1122723002.1583134501; __gads=ID=2d22c788acb05249:T=1583138393:S=ALNI_MbNYbfEpm4Fmb8SPRxoNwCmZxu_Dg; G_ENABLED_IDPS=google; _gid=GA1.2.1069042459.1583475474; __stripe_sid=0e92a30b-2195-4b17-89db-19ef91de179d");

		httpPost.addHeader("referer",referer);
		httpPost.addHeader("sec-fetch-dest","empty");
		httpPost.addHeader("sec-fetch-mode","cors");
		httpPost.addHeader("sec-fetch-site","same-site");
		httpPost.addHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36");

		//	StringEntity stringEntity = new StringEntity((str), "application/json", "utf-8");  //方法 被弃用了
		StringEntity stringEntity = new StringEntity(body, ContentType.APPLICATION_JSON);		//推荐的方法

		//设置表单的Entity对象到Post请求中
		httpPost.setEntity(stringEntity);

		CloseableHttpResponse response= null;
			//使用httpClient发起请求 获取 response
		response = httpClient.execute(httpPost);

		return getHttpEntity(response, url);

    }



    @Override
    public HttpEntity doGetByProxy(JSONObject headers, String url) throws IOException {

//    String nginxIp = "47.99.130.126:5001";   //公网
      String nginxIp = "172.16.220.188:5001";   //内网

        if(url.contains("pexels") || url.contains("pixabay")){
            url = "http://"+nginxIp+"/proxy2?url="+url;
           return doGet(headers,url);
        }

        String url_ = "http://cs.lookxiai.com/proxy/data2";

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

    private static final int TIME_OUT = 1000 * 20;
    public String doGet(String url, Map<String, String> headers,  HttpHost proxy) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIME_OUT).setConnectTimeout(TIME_OUT)
                .setProxy(proxy)
                .build();
        HttpGet httpGet = new HttpGet(url);

        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                httpGet.setHeader(header.getKey(), header.getValue());
            }
        }
        httpGet.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = httpclient.execute(httpGet);
            org.apache.http.HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, Consts.UTF_8);
        } catch (Exception e) {
           // LOG.error("url={} , headers={}", url, headers, e);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
