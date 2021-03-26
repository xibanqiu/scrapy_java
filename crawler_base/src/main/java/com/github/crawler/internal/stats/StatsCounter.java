/*
 * Copyright 2019 Peter Bencze.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.crawler.internal.stats;

import com.github.crawler.internal.util.FunctionalReentrantReadWriteLock;
import org.apache.commons.lang3.Validate;

import java.io.Serializable;

/**
 * Accumulates statistics during the operation of the crawler.
 */
public final class StatsCounter implements Serializable {

    private final FunctionalReentrantReadWriteLock lock;

    private int remainingCrawlCandidateCount;
    private int processedCrawlCandidateCount;
    private int responseSuccessCount;
    private int pageLoadTimeoutCount;
    private int requestRedirectCount;
    private int nonHtmlResponseCount;
    private int responseErrorCount;
    private int networkErrorCount;
    private int parseErrorCount;
    private int parseItemCount;
    private int filteredDuplicateRequestCount;
    private int filteredOffsiteRequestCount;
    private int filteredCrawlDepthLimitExceedingRequestCount;
    private int filteredDuplicateItemCount;
    private int filteredItemCount;
    private int parserRelationItemCount;

    /**
     * Creates a {@link StatsCounter} instance.
     */
    public StatsCounter() {
        lock = new FunctionalReentrantReadWriteLock();
    }

    /**
     * Returns the number of remaining crawl candidates.
     *
     * @return the number of remaining crawl candidates
     */
    public int getRemainingCrawlCandidateCount() {
        return lock.readWithLock(() -> remainingCrawlCandidateCount);
    }

    /**
     * Records an added crawl candidate. This should be called when a crawl candidate is added to
     * the crawl frontier.
     */
    public void recordRemainingCrawlCandidate() {
        lock.writeWithLock(() -> ++remainingCrawlCandidateCount);
    }

    /**
     * Returns the number of processed crawl candidates.
     *
     * @return the number of processed crawl candidates
     */
    public int getProcessedCrawlCandidateCount() {
        return lock.readWithLock(() -> processedCrawlCandidateCount);
    }

    /**
     * Returns the number of responses received during the crawl, whose HTTP status code indicated
     * success (2xx).
     *
     * @return the number of responses received during the crawl, whose HTTP status code indicated
     *         success (2xx)
     */
    public int getResponseSuccessCount() {
        return lock.readWithLock(() -> responseSuccessCount);
    }

    /**
     * Records the receipt of a response whose HTTP status code indicates success (2xx).
     */
    public void recordResponseSuccess() {
        lock.writeWithLock(() -> {
            decrementRemainingCrawlCandidateCount();

            ++responseSuccessCount;
            incrementProcessedCrawlCandidateCount();
        });
    }

    /**
     * Returns the number of page load timeouts that occurred during the crawl.
     *
     * @return the number of page load timeouts that occurred during the crawl
     */
    public int getPageLoadTimeoutCount() {
        return lock.readWithLock(() -> pageLoadTimeoutCount);
    }

    /**
     * Records a page load timeout.
     */
    public void recordPageLoadTimeout() {
        lock.writeWithLock(() -> {
            decrementRemainingCrawlCandidateCount();

            ++pageLoadTimeoutCount;
            incrementProcessedCrawlCandidateCount();
        });
    }

    /**
     * Returns the number of request redirects that occurred during the crawl.
     *
     * @return the number of request redirects that occurred during the crawl
     */
    public int getRequestRedirectCount() {
        return lock.readWithLock(() -> requestRedirectCount);
    }

    /**
     * Records a request redirect.
     */
    public void recordRequestRedirect() {
        lock.writeWithLock(() -> {
            decrementRemainingCrawlCandidateCount();

            ++requestRedirectCount;
            incrementProcessedCrawlCandidateCount();
        });
    }

    /**
     * Returns the number of responses received with non-HTML content.
     *
     * @return the number of responses received with non-HTML content
     */
    public int getNonHtmlResponseCount() {
        return lock.readWithLock(() -> nonHtmlResponseCount);
    }

    /**
     * Records the receipt of a response with non-HTML content.
     */
    public void recordNonHtmlResponse() {
        lock.writeWithLock(() -> {
            decrementRemainingCrawlCandidateCount();

            ++nonHtmlResponseCount;
            incrementProcessedCrawlCandidateCount();
        });
    }

    /**
     * Returns the number of responses received during the crawl, whose HTTP status code indicated
     * error (4xx or 5xx).
     *
     * @return the number of responses received during the crawl, whose HTTP status code indicated
     *         error (4xx or 5xx)
     */
    public int getResponseErrorCount() {
        return lock.readWithLock(() -> responseErrorCount);
    }

    /**
     * Records the receipt of a response whose HTTP status code indicates error (4xx or 5xx).
     */
    public void recordResponseError() {
        lock.writeWithLock(() -> {
            decrementRemainingCrawlCandidateCount();

            ++responseErrorCount;
            incrementProcessedCrawlCandidateCount();
        });
    }

    /**
     * Returns the number of network errors that occurred during the crawl.
     *
     * @return the number of network errors that occurred during the crawl
     */
    public int getNetworkErrorCount() {
        return lock.readWithLock(() -> networkErrorCount);
    }

    /**
     * Records a network error. This should be called when a network error occurs while trying to
     * fulfill a request.
     */
    public void recordNetworkError() {
        lock.writeWithLock(() -> {
            decrementRemainingCrawlCandidateCount();

            ++networkErrorCount;
            incrementProcessedCrawlCandidateCount();
        });
    }


    /**
     * Records a parse error. This should be called when a parse error occurs while trying to
     * parse a content.
     */
    public void recordParseError() {
        lock.writeWithLock(() -> ++parseErrorCount);
    }

    /**
     * Returns the number of parse error that occurred during the parse.
     *
     * @return the number of parse error that occurred during the parse
     */
    public int getParseErrorCount() {
        return parseErrorCount;
    }


    /**
     * Records a only relation parse item
     */
    public void recordParserRelationItemCount() {
        lock.writeWithLock(() -> ++parserRelationItemCount);
    }

    /**
     * Returns the number of parse items.
     *
     * @return the number of parse items
     */
    public int getParserRelationItemCount() {
        return parserRelationItemCount;
    }

    /**
     * Records a parse item
     */
    public void recordParseItem() {
        lock.writeWithLock(() -> ++parseItemCount);
    }

    /**
     * Returns the number of parse items.
     *
     * @return the number of parse items
     */
    public int getParseItemCount() {
        return parseItemCount;
    }

    /**
     * Returns the number of filtered duplicate requests.
     *
     * @return the number of filtered duplicate requests
     */
    public int getFilteredDuplicateRequestCount() {
        return lock.readWithLock(() -> filteredDuplicateRequestCount);
    }

    /**
     * Records a duplicate request. This should be called when the duplicate request filter is
     * enabled and a duplicate request is encountered.
     */
    public void recordDuplicateRequest() {
        lock.writeWithLock(() -> ++filteredDuplicateRequestCount);
    }

    /**
     * Returns the number of filtered duplicate items.
     *
     * @return the number of filtered duplicate items.
     */
    public int getFilteredDuplicateItemCount() {
        return lock.readWithLock(() -> filteredDuplicateItemCount);
    }

    /**
     * Records a duplicate item.
     */
    public void recordDuplicateItem() {
        lock.writeWithLock(() -> ++filteredDuplicateItemCount);
    }

    /**
     * Returns the number of filtered items.
     *
     * @return the number of filtered items.
     */
    public int getFilteredItemCount() {
        return lock.readWithLock(() -> filteredItemCount);
    }

    /**
     * Records a filter item.
     */
    public void recordFilteredItem() {
        lock.writeWithLock(() -> ++filteredItemCount);
    }

    /**
     * Returns the number of filtered offsite requests.
     *
     * @return the number of filtered offsite requests
     */
    public int getFilteredOffsiteRequestCount() {
        return lock.readWithLock(() -> filteredOffsiteRequestCount);
    }

    /**
     * Records an offsite request. This should be called when the offsite request filter is enabled
     * and an offsite request is encountered.
     */
    public void recordOffsiteRequest() {
        lock.writeWithLock(() -> ++filteredOffsiteRequestCount);
    }

    /**
     * Returns the number of filtered crawl depth limit exceeding requests.
     *
     * @return the number of filtered crawl depth limit exceeding requests
     */
    public int getFilteredCrawlDepthLimitExceedingRequestCount() {
        return lock.readWithLock(() -> filteredCrawlDepthLimitExceedingRequestCount);
    }

    /**
     * Records a crawl depth limit exceeding request. This should be called when a crawl depth limit
     * is set and the request's crawl depth exceeds this limit.
     */
    public void recordCrawlDepthLimitExceedingRequest() {
        lock.writeWithLock(() -> ++filteredCrawlDepthLimitExceedingRequestCount);
    }

    /**
     * Returns a snapshot of this counter's values.
     *
     * @return a snapshot of this counter's values
     */
    public StatsCounterSnapshot getSnapshot() {
        return lock.readWithLock(() -> new StatsCounterSnapshot(this));
    }

    /**
     * Increments the number of processed crawl candidates.
     */
    private void incrementProcessedCrawlCandidateCount() {
        lock.writeWithLock(() -> ++processedCrawlCandidateCount);
    }

    /**
     * Decrements the number of remaining crawl candidates. This number cannot be negative.
     */
    private void decrementRemainingCrawlCandidateCount() {
        lock.writeWithLock(() -> {
            Validate.validState(remainingCrawlCandidateCount > 0,
                    "The number of remaining crawl candidates cannot be negative.");
            --remainingCrawlCandidateCount;
        });

    }

}
