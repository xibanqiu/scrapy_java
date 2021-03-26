package com.github.crawler.crawldb;


import com.github.crawler.api.CrawlCandidate;

/**
 * 爬取过程中，写入爬取历史、网页Content、解析信息的Writer
 *
 */
public interface SegmentWriter {

    void initSegmentWriter() throws Exception;

    void writeFetchSegment(CrawlCandidate fetchRequest) throws Exception;

    //void writeParseSegment(CrawlCandidate parseRequest) throws Exception;

    void closeSegmentWriter() throws Exception;

}
