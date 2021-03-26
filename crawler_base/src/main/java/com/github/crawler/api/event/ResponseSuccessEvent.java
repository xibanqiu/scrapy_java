/*
 * Copyright 2017 Peter Bencze.
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

package com.github.crawler.api.event;


import com.github.crawler.api.CompleteCrawlResponse;
import com.github.crawler.api.CrawlCandidate;
import com.github.crawler.internal.CrawlEvent;

/**
 * Event which gets delivered when the client loads the page and the HTTP status code indicates
 * success (2xx).
 */
public final class ResponseSuccessEvent extends CrawlEvent {

    private final CompleteCrawlResponse completeCrawlResponse;

    /**
     * Creates a {@link ResponseSuccessEvent} instance.
     *
     * @param crawlCandidate        the current crawl candidate
     * @param completeCrawlResponse the complete crawl response
     */
    public ResponseSuccessEvent(
            final CrawlCandidate crawlCandidate,
            final CompleteCrawlResponse completeCrawlResponse) {
        super(crawlCandidate);

        this.completeCrawlResponse = completeCrawlResponse;
    }

    /**
     * Returns the complete crawl response.
     *
     * @return the complete crawl response
     */
    public CompleteCrawlResponse getCompleteCrawlResponse() {
        return completeCrawlResponse;
    }
}
