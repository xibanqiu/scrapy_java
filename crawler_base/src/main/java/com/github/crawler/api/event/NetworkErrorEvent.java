package com.github.crawler.api.event;

import com.github.crawler.api.CrawlCandidate;
import com.github.crawler.internal.CrawlEvent;

public class NetworkErrorEvent  extends CrawlEvent {

    private final String errorMessage;

    /**
     * Creates a {@link NetworkErrorEvent} instance.
     *
     * @param crawlCandidate the current crawl candidate
     * @param errorMessage   the network error message
     */
    public NetworkErrorEvent(final CrawlCandidate crawlCandidate, final String errorMessage) {
        super(crawlCandidate);

        this.errorMessage = errorMessage;
    }

    /**
     * Returns the network error message.
     *
     * @return the network error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }


}
