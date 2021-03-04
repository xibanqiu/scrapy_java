package com.github.crawler.api;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.google.common.net.InternetDomainName;
import com.alibaba.fastjson;

import java.io.Serializable;
import java.net.URI;

public final class CrawlRequest implements Serializable {


    private final URI requestUrl;

    private final int priority;

    private JSONPObject extInfo;


    @JsonIgnore
    private final Serializable  metadata;


    @JsonIgnore
    private final InternetDomainName domain;


    public CrawlRequest(final CrawlRequestBuilder builder) {

        requestUrl = builder.requestUrl;
        domain = builder.domain;
        priority = builder.priority;
        metadata = builder.metadata;
        extInfo = builder.extInfo;
    }



    /**
     * Builds {@link CrawlRequest} instances.
     */
    public static final class CrawlRequestBuilder {

        private static final int DEFAULT_PRIORITY = 0;

        private final URI requestUrl;
        private final InternetDomainName domain;

        private int priority;
        private Serializable metadata;
        private JSONObject extInfo;



    }



}
