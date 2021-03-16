package com.github.crawler.http;

import org.apache.http.Header;
import org.apache.http.entity.ContentType;

import java.nio.charset.Charset;
import java.util.List;

public class HttpEntity {

    private byte[] content;
    private String url;
    private String location;
    private int statusCode;
    private String statusText;
    private List<Header> headers;
    private ContentType contentType;

    public HttpEntity(String url) {
        this.url = url;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getContentAsString(){
        return new String(content, Charset.defaultCharset());
    }

    public String getContentAsString(Charset charset){
        return new String(content, charset);
    }
}
