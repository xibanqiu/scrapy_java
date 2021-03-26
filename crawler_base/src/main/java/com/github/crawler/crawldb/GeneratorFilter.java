package com.github.crawler.crawldb;


import com.github.crawler.api.CrawlCandidate;

public interface GeneratorFilter{
    /**
     * return CrawlCandidate if you want to generate datum
     * return null if you want to filter CrawlCandidate
     * @param crawlCandidate
     * @return
     */
    CrawlCandidate filter(CrawlCandidate crawlCandidate);
}
