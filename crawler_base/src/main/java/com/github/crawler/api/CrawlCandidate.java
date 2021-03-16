package com.github.crawler.api;


import com.alibaba.fastjson.JSONObject;
import com.google.common.net.InternetDomainName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.net.URI;
import java.util.Optional;

/**
 * Represents a candidate for crawling.
 */
public final class CrawlCandidate implements Serializable {
    public final static int STATUS_DB_UNEXECUTED = 0;
    public final static int STATUS_DB_FAILED = 1;
    public final static int STATUS_DB_SUCCESS = 5;

    private final URI refererUrl;
    private final int crawlDepth;
    private final CrawlRequest crawlRequest;

    private int status;
    private int executeCount;
    /**
     * 根据key去重,
     * 可以通过getKey()方法获得CrawlDatum的key,如果key为null,getKey()方法会返回URL
     * 因此如果不设置key，爬虫会将URL当做key作为去重标准
     */
    private final String key;


    private CrawlCandidate(final CrawlCandidateBuilder builder) {
        this.crawlRequest = builder.crawlRequest;
        this.refererUrl = builder.refererUrl;
        this.crawlDepth = builder.crawlDepth;
        this.key = builder.key;
        this.status = builder.status;
        this.executeCount = builder.executeCount;

    }


    public static CrawlCandidate fromJsonObject(String crawlKey, JSONObject json) {
        CrawlRequest request = new CrawlRequest.CrawlRequestBuilder(json.getString("requestUrl"))
                .setPriority(json.getInteger("priority"))
                .setExtInfo(json.getJSONObject("extInfo")).build();

        CrawlCandidate.CrawlCandidateBuilder crawlRequestBuilder = new CrawlCandidate.CrawlCandidateBuilder(request)
                .setCrawlDepth(json.getInteger("crawlDepth"))
                .setExecuteCount(json.getInteger("executeCount"))
                .setStatus(json.getInteger("status"))
                .key(crawlKey);

        return crawlRequestBuilder.build();
    }

    public String toJSONString(){
        return JSONObject.toJSONString(this);
    }

    public int incrExecuteCount(int count) {
        executeCount+=count;
        return executeCount;
    }

    /**
     * Returns the referer URL.
     *
     * @return the URL of the referer
     */
    public URI getRefererUrl() {
        return refererUrl;
    }

    /**
     * Returns the request URL.
     *
     * @return the URL of the request
     */
    public URI getRequestUrl() {
        return crawlRequest.getRequestUrl();
    }

    /**
     * Returns the domain of the request URL.
     *
     * @return the domain of the request URL
     */
    public InternetDomainName getDomain() {
        return crawlRequest.getDomain();
    }

    /**
     * Returns the crawl depth of the request.
     *
     * @return the crawl depth of the request
     */
    public int getCrawlDepth() {
        return crawlDepth;
    }

    /**
     * Returns the priority of the request.
     *
     * @return the priority of the request
     */
    public int getPriority() {
        return crawlRequest.getPriority();
    }

    /**
     * Returns the extInfo.
     *
     * @return the extInfo
     */
    public JSONObject getExtInfo() {
        return crawlRequest.getExtInfo();
    }

    /**
     * Returns the metadata associated with the request.
     *
     * @return the metadata associated with the request
     */
    public Optional<Serializable> getMetadata() {
        return crawlRequest.getMetadata();
    }

    public int getExecuteCount() {
        return executeCount;
    }

    public void setExecuteCount(int executeCount) {
        this.executeCount = executeCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String key() {
        if (key == null) {
            return crawlRequest.getRequestUrl().toString();
        } else {
            return key;
        }
    }


    /**
     * Returns a string representation of this crawl candidate.
     *
     * @return a string representation of this crawl candidate
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("refererUrl", refererUrl)
                .append("requestUrl", getRequestUrl())
                .append("domain", getDomain())
                .append("crawlDepth", crawlDepth)
                .append("priority", getPriority())
                .append("extInfo", getExtInfo())
                .toString();
    }

    /**
     * Builds {@link CrawlCandidate} instances.
     */
    public static final class CrawlCandidateBuilder {

        private final CrawlRequest crawlRequest;

        private URI refererUrl;
        private int crawlDepth;
        private String key = null;
        private int status = STATUS_DB_UNEXECUTED;
        private int executeCount = 0;

        /**
         * Creates a {@link CrawlCandidateBuilder} instance.
         *
         * @param request the <code>CrawlRequest</code> instance from which this candidate is built
         */
        public CrawlCandidateBuilder(final CrawlRequest request) {
            crawlRequest = request;
        }

        public CrawlCandidateBuilder key(final String key) {
            this.key = key;
            return this;
        }

        public CrawlCandidateBuilder setStatus(final Integer status) {
            this.status = status;
            return this;
        }

        public CrawlCandidateBuilder setExecuteCount(final Integer executeCount) {
            this.executeCount = executeCount;
            return this;
        }

        public String getKey() {
            return key;
        }

        public int getStatus() {
            return status;
        }

        public int getExecuteCount() {
            return executeCount;
        }

        /**
         * Sets the referer URL.
         *
         * @param refererUrl the referer URL
         *
         * @return the <code>CrawlCandidateBuilder</code> instance
         */
        public CrawlCandidateBuilder setRefererUrl(final URI refererUrl) {
            this.refererUrl = refererUrl;
            return this;
        }

        /**
         * Sets the crawl depth of the request.
         *
         * @param crawlDepth the crawl depth of the request
         *
         * @return the <code>CrawlCandidateBuilder</code> instance
         */
        public CrawlCandidateBuilder setCrawlDepth(final int crawlDepth) {
            this.crawlDepth = crawlDepth;
            return this;
        }

        /**
         * Builds the configured <code>CrawlCandidate</code> instance.
         *
         * @return the configured <code>CrawlCandidate</code> instance
         */
        public CrawlCandidate build() {
            return new CrawlCandidate(this);
        }
    }
}
