package com.github.crawler.internal;

import com.github.crawler.api.CrawlCandidate;

/**
 * Base class from which all crawl event classes shall be derived.
 */
public abstract class CrawlEvent {

    private final CrawlCandidate crawlCandidate;

    /**
     * Base constructor of all crawl event classes.
     *
     * @param crawlCandidate the current crawl candidate
     */
    protected CrawlEvent(final CrawlCandidate crawlCandidate) {
        this.crawlCandidate = crawlCandidate;
    }

    /**
     * Returns the current crawl candidate.
     *
     * @return the current crawl candidate
     */
    public final CrawlCandidate getCrawlCandidate() {
        return crawlCandidate;
    }
}