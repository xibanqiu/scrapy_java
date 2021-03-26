package com.github.crawler.crawldb;


import com.github.crawler.api.CrawlCandidate;

/**
 *
 */
public interface Injector {
     void inject(CrawlCandidate crawlCandidate) throws Exception;
}
