package com.github.crawler.crawldb;


import com.github.crawler.api.CrawlCandidate;

public class StatusGeneratorFilter implements GeneratorFilter {

    @Override
    public CrawlCandidate filter(CrawlCandidate datum) {
        if(datum.getStatus() == CrawlCandidate.STATUS_DB_SUCCESS){
            return null;
        }else{
            return datum;
        }
    }
}
