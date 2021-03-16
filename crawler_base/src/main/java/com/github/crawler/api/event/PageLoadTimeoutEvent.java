package com.github.crawler.api.event;

import com.github.crawler.api.CrawlCandidate;
import com.github.crawler.api.PartialCrawlResponse;
import com.github.crawler.internal.CrawlEvent;


/**
 * Event which gets delivered when a page does not load in the client within the timeout period.
 */
public final class PageLoadTimeoutEvent extends CrawlEvent {

    private final PartialCrawlResponse partialCrawlResponse;

    /**
     * Creates a {@link PageLoadTimeoutEvent} instance.
     *
     * @param crawlCandidate       the current crawl candidate
     * @param partialCrawlResponse the partial crawl response
     */
    public PageLoadTimeoutEvent(
            final CrawlCandidate crawlCandidate,
            final PartialCrawlResponse partialCrawlResponse) {
        super(crawlCandidate);

        this.partialCrawlResponse = partialCrawlResponse;
    }

    /**
     * Returns the partial crawl response.
     *
     * @return the partial crawl response
     */
    public PartialCrawlResponse getPartialCrawlResponse() {
        return partialCrawlResponse;
    }
}

