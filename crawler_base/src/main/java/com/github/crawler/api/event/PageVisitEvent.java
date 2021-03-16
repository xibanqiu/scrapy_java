package com.github.crawler.api.event;

import com.github.crawler.api.CompleteCrawlResponse;
import com.github.crawler.api.CrawlCandidate;
import com.github.crawler.api.model.Page;
import com.github.crawler.internal.CrawlEvent;


public final class PageVisitEvent extends CrawlEvent {

    private final CompleteCrawlResponse completeCrawlResponse;
    private final Page page;

    /**
     * Creates a {@link PageVisitEvent} instance.
     *
     * @param crawlCandidate        the current crawl candidate
     * @param completeCrawlResponse the complete crawl response
     */
    public PageVisitEvent(
            final CrawlCandidate crawlCandidate,
            final CompleteCrawlResponse completeCrawlResponse,
            final Page page) {
        super(crawlCandidate);

        this.completeCrawlResponse = completeCrawlResponse;
        this.page = page;
    }

    /**
     * Returns the complete crawl response.
     *
     * @return the complete crawl response
     */
    public CompleteCrawlResponse getCompleteCrawlResponse() {
        return completeCrawlResponse;
    }

    public Page getPage() {
        return page;
    }
}

