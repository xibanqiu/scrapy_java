package com.github.crawler.tools;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpClientTools {

	protected final static Logger LOG = LoggerFactory.getLogger(HttpClientTools.class);
	private static final int TIME_OUT = 1000 * 60;

	/**
	 * 请求头，默认设置，chrome头
	 * @return
	 * @author huangkai
	 * @date 2018/08/07
	 *
	 */
	public static Map<String, String> headers(){
		Map<String, String> headers = new HashMap<>();
		headers.put("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		return headers;
	}
	
	public static String doGet(String url) {
		return doGet(url, headers());
	}
	
	public static String doDelete(String url) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIME_OUT)
				.setConnectTimeout(TIME_OUT).build();
		HttpDelete http = new HttpDelete(url);
		http.setConfig(requestConfig);
		try {
			CloseableHttpResponse response = httpclient.execute(http);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		} catch (Exception e) {
			LOG.error("",e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				LOG.error("",e);
			}
		}
		return null;
	}

	public static String upload(String url, File file){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIME_OUT)
				.setConnectTimeout(TIME_OUT).build();
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		try {
			HttpPost post = new HttpPost(url);
			// fileParamName should be replaced with parameter name your REST API expect.
			builder.addPart("file", new FileBody(file));
			post.setConfig(requestConfig);
			//builder.addPart("optionalParam", new StringBody("true", ContentType.create("text/plain", Consts.ASCII)));
			post.setEntity(builder.build());
			CloseableHttpResponse response = httpclient.execute(post);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		} catch (Exception e) {
			LOG.error("",e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				LOG.error("",e);
			}
		}
		return null;
	}

	public static String doPost(String url, String body) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIME_OUT)
				.setConnectTimeout(TIME_OUT).build();
		try {
			HttpPost post = new HttpPost(url);
			post.setConfig(requestConfig);
			post.setEntity(new StringEntity(body,"UTF-8"));
			CloseableHttpResponse response = httpclient.execute(post);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		} catch (Exception e) {
			LOG.error("",e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				LOG.error("",e);
			}
		}
		return null;
	}
	
	//对post请求  带有 request payload 形式的数据
	public static String doPost( HttpPost post) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIME_OUT)
				.setConnectTimeout(TIME_OUT).build();
		try {
			post.setConfig(requestConfig);
			
			CloseableHttpResponse response = httpclient.execute(post);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		} catch (Exception e) {
			LOG.error("",e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				LOG.error("",e);
			}
		}
		return null;
	}
	
	public static String doPostMap(String url, Map<String,String> params) {
		return doPostMap(url, params, null);
	}
	
	public static String doPostMap(String url, Map<String,String> params,Map<String, String> headers) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIME_OUT).setConnectTimeout(TIME_OUT)
				.build();
		try {
			HttpPost post = new HttpPost(url);
			post.setConfig(requestConfig);
			
			//
			if(params!=null){
				List<NameValuePair> nvps = new ArrayList<NameValuePair>(); 
	            for (String name : params.keySet()) {
	    			nvps.add(new BasicNameValuePair(name, params.get(name)));
	    		}
	            post.setEntity(new UrlEncodedFormEntity(nvps,Consts.UTF_8));
	            
			}
            
            //
			if (headers != null) {
				for (String key : headers.keySet()) {
					post.addHeader(key, headers.get(key));
				}
			}
			CloseableHttpResponse response = httpclient.execute(post);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity,Consts.UTF_8);
		} catch (Exception e) {
			LOG.error("url=" + url + " , params=" + params+" , hearders="+headers, e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}



	public static String doGet(String url, Map<String, String> headers) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIME_OUT).setConnectTimeout(TIME_OUT)
				.build();
		HttpGet httpGet = new HttpGet(url);

		if (headers != null) {
			for (Entry<String, String> header : headers.entrySet()) {
				httpGet.setHeader(header.getKey(), header.getValue());
			}
		}

		httpGet.setConfig(requestConfig);
		try {
			CloseableHttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, Consts.UTF_8);
		} catch (Exception e) {
			LOG.error("url=" + url, e);
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
