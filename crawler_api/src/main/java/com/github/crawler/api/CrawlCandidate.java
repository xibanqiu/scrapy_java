package com.github.crawler.api;

import java.io.Serializable;
import java.net.URI;


/**
 * Represent a candidate for crawling
 */
public final class CrawlCandidate implements Serializable {

    public final static int STATUS_DB_UNEXECUTE = 0;
    public final static int STATUS_DB_FAILED = 1;
    public final static int STATUS_DB_SUCCESS = 5;


    private final URI refererUrl ;
    private final int crawlDepth ;
    private final CrawlRequest crawlRequest ;






}
