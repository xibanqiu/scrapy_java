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


import com.github.crawler.api.CrawlCandidate;
import com.github.crawler.internal.CrawlEvent;

/**
 * Event which gets delivered when parse content error
 */
public final class ParseErrorEvent extends CrawlEvent {

    private final Exception exception;

    /**
     * Creates a {@link ParseErrorEvent} instance.
     *
     * @param crawlCandidate        the current crawl candidate
     * @param exception the parse exception
     */
    public ParseErrorEvent(
            final CrawlCandidate crawlCandidate,
            final Exception exception) {
        super(crawlCandidate);

        this.exception = exception;
    }

    /**
     * Returns the parse exception.
     *
     * @return the parse exception
     */
    public Exception getException() {
        return exception;
    }
}
