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

/**
 * Represents a snapshot of the stats counter values.
 */
public final class StatsCounterSnapshot {

    private final int remainingCrawlCandidateCount;
    private final int processedCrawlCandidateCount;
    private final int responseSuccessCount;
    private final int pageLoadTimeoutCount;
    private final int requestRedirectCount;
    private final int nonHtmlResponseCount;
    private final int responseErrorCount;
    private final int networkErrorCount;
    private final int parseErrorCount;
    private final int parseItemCount;
    private final int filteredDuplicateRequestCount;
    private final int filteredOffsiteRequestCount;
    private final int filteredCrawlDepthLimitExceedingRequestCount;
    private final int filteredDuplicateItemCount;
    private final int filteredItemCount;
    private final int parserRelationItemCount;

    /**
     * Creates a {@link StatsCounterSnapshot} instance.
     *
     * @param statsCounter the stats counter object to create the snapshot from
     */
    public StatsCounterSnapshot(final StatsCounter statsCounter) {
        remainingCrawlCandidateCount = statsCounter.getRemainingCrawlCandidateCount();
        processedCrawlCandidateCount = statsCounter.getProcessedCrawlCandidateCount();
        responseSuccessCount = statsCounter.getResponseSuccessCount();
        pageLoadTimeoutCount = statsCounter.getPageLoadTimeoutCount();
        requestRedirectCount = statsCounter.getRequestRedirectCount();
        nonHtmlResponseCount = statsCounter.getNonHtmlResponseCount();
        responseErrorCount = statsCounter.getResponseErrorCount();
        networkErrorCount = statsCounter.getNetworkErrorCount();
        parseErrorCount = statsCounter.getParseErrorCount();
        parseItemCount = statsCounter.getParseItemCount();
        filteredDuplicateRequestCount = statsCounter.getFilteredDuplicateRequestCount();
        filteredOffsiteRequestCount = statsCounter.getFilteredOffsiteRequestCount();
        filteredCrawlDepthLimitExceedingRequestCount =
                statsCounter.getFilteredCrawlDepthLimitExceedingRequestCount();
        filteredDuplicateItemCount = statsCounter.getFilteredDuplicateItemCount();
        filteredItemCount = statsCounter.getFilteredItemCount();
        parserRelationItemCount = statsCounter.getParserRelationItemCount();
    }

    /**
     * Returns the number of remaining crawl candidates.
     *
     * @return the number of remaining crawl candidates
     */
    public int getRemainingCrawlCandidateCount() {
        return remainingCrawlCandidateCount;
    }

    /**
     * Returns the number of processed crawl candidates.
     *
     * @return the number of processed crawl candidates
     */
    public int getProcessedCrawlCandidateCount() {
        return processedCrawlCandidateCount;
    }

    /**
     * Returns the number of responses received during the crawl, whose HTTP status code indicated
     * success (2xx).
     *
     * @return the number of responses received during the crawl, whose HTTP status code indicated
     *         success (2xx)
     */
    public int getResponseSuccessCount() {
        return responseSuccessCount;
    }

    /**
     * Returns the number of page load timeouts that occurred during the crawl.
     *
     * @return the number of page load timeouts that occurred during the crawl
     */
    public int getPageLoadTimeoutCount() {
        return pageLoadTimeoutCount;
    }

    /**
     * Returns the number of request redirects that occurred during the crawl.
     *
     * @return the number of request redirects that occurred during the crawl.
     */
    public int getRequestRedirectCount() {
        return requestRedirectCount;
    }

    /**
     * Returns the number of responses received with non-HTML content.
     *
     * @return the number of responses received with non-HTML content
     */
    public int getNonHtmlResponseCount() {
        return nonHtmlResponseCount;
    }

    /**
     * Returns the number of responses received during the crawl, whose HTTP status code indicated
     * error (4xx or 5xx).
     *
     * @return the number of responses received during the crawl, whose HTTP status code indicated
     *         error (4xx or 5xx)
     */
    public int getResponseErrorCount() {
        return responseErrorCount;
    }

    /**
     * Returns the number of network errors that occurred during the crawl.
     *
     * @return the number of network errors that occurred during the crawl
     */
    public int getNetworkErrorCount() {
        return networkErrorCount;
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
     * Returns the number of parse error that occurred during the parse.
     *
     * @return the number of parse error that occurred during the parse
     */
    public int getParseErrorCount() {
        return parseErrorCount;
    }


    /**
     * Returns the number of filtered duplicate requests.
     *
     * @return the number of filtered duplicate requests
     */
    public int getFilteredDuplicateRequestCount() {
        return filteredDuplicateRequestCount;
    }

    /**
     * Returns the number of filtered duplicate items.
     *
     * @return the number of filtered duplicate items
     */
    public int getFilteredDuplicateItemCount() {
        return filteredDuplicateItemCount;
    }

    public int getParserRelationItemCount() {
        return parserRelationItemCount;
    }


    /**
     * Returns the number of filtered items.
     *
     * @return the number of filtered items.
     */
    public int getFilteredItemCount() {
        return filteredItemCount;
    }


    /**
     * Returns the number of filtered offsite requests.
     *
     * @return the number of filtered offsite requests
     */
    public int getFilteredOffsiteRequestCount() {
        return filteredOffsiteRequestCount;
    }

    /**
     * Returns the number of filtered crawl depth limit exceeding requests.
     *
     * @return the number of filtered crawl depth limit exceeding requests
     */
    public int getFilteredCrawlDepthLimitExceedingRequestCount() {
        return filteredCrawlDepthLimitExceedingRequestCount;
    }
}
