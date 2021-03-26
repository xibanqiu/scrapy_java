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

package com.github.crawler.api;


import com.github.crawler.api.event.*;
import com.github.crawler.api.model.Page;
import com.github.crawler.crawldb.DBManager;
import com.github.crawler.http.CommonHttpClient;
import com.github.crawler.http.CustomHttpClient;
import com.github.crawler.http.HttpClientContext;
import com.github.crawler.http.HttpEntity;
import com.github.crawler.internal.CrawlEvent;
import com.github.crawler.internal.CrawlFrontier;
import com.github.crawler.internal.CustomCallbackManager;
import com.github.crawler.internal.crawldelaymechanism.CrawlDelayMechanism;
import com.github.crawler.internal.crawldelaymechanism.FixedCrawlDelayMechanism;
import com.github.crawler.internal.crawldelaymechanism.RandomCrawlDelayMechanism;
import com.github.crawler.internal.stats.StatsCounter;
import com.github.crawler.internal.util.stopwatch.Stopwatch;
import org.apache.commons.lang3.Validate;
import org.apache.http.entity.ContentType;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Provides a skeletal implementation of a crawler to minimize the effort for users to implement
 * their own.
 */
public abstract class Crawler {

    protected final Logger LOG = LoggerFactory.getLogger("crawlerLog");

    protected CrawlerConfiguration config;
    private Stopwatch runTimeStopwatch;
    private StatsCounter statsCounter;
    private CrawlFrontier crawlFrontier;
    private CustomCallbackManager callbackManager;
    private CrawlDelayMechanism crawlDelayMechanism;
    private AtomicBoolean isStopped;
    private AtomicBoolean isStopInitiated;
    protected HttpClientContext httpClientContext;
    private DBManager dbManager;
    private int threadSize;

    /**
     * sets up the crawler with the provided configuration.
     *
     * @param config the configuration of the crawler
     */
    public Crawler(final CrawlerConfiguration config) {
        this(new CrawlerState(Validate.notNull(config, "The config parameter cannot be null")));
    }

    /**
     * restores the crawler to the provided state.
     *
     * @param state the state to restore the crawler to
     */
    public Crawler(final CrawlerState state) {
        Validate.notNull(state, "The state parameter cannot be null");

        config = state.getStateObject(CrawlerConfiguration.class)
                .orElseThrow(() -> new IllegalArgumentException("Invalid crawler state provided"));
        runTimeStopwatch = state.getStateObject(Stopwatch.class).orElseGet(Stopwatch::new);
        statsCounter = state.getStateObject(StatsCounter.class).orElseGet(StatsCounter::new);

        dbManager = config.getDbManager();

        threadSize = config.getThreadSize();

        crawlFrontier = state.getStateObject(CrawlFrontier.class)
                .orElseGet(() -> new CrawlFrontier(config, statsCounter));

        callbackManager = new CustomCallbackManager();

        isStopInitiated = new AtomicBoolean(false);
        isStopped = new AtomicBoolean(true);

        httpClientContext = new HttpClientContext();
        httpClientContext.setCustomHttpClient(new CommonHttpClient());
    }

    protected void setHttpClient(CustomHttpClient httpClient){
        httpClientContext.setCustomHttpClient(httpClient);
    }
    protected CustomHttpClient getCustomHttpClient(){
        return httpClientContext.getCustomHttpClient();
    }

    /**
     * Returns the configuration of the crawler. This method is thread-safe.
     *
     * @return the configuration of the crawler
     */
    public final CrawlerConfiguration getCrawlerConfiguration() {
        return config;
    }

    /**
     * Returns summary statistics about the crawl progress. This method is thread-safe.
     *
     * @return summary statistics about the crawl progress
     */
    public final CrawlStats getCrawlStats() {
        return new CrawlStats(runTimeStopwatch.getElapsedDuration(), statsCounter.getSnapshot());
    }

    protected final void recordDuplicateItem(){
        statsCounter.recordDuplicateItem();
    }

    protected final void recordParserRelationItemCount(){
        statsCounter.recordParserRelationItemCount();
    }

    protected final void recordFilteredItem(){
        statsCounter.recordFilteredItem();
    }

    protected final void recordParseItem(){
        statsCounter.recordParseItem();
    }

    /**
     * Starts the crawler. The crawler will use Chrome headless browser to visit URLs. This method
     * will block until the crawler finishes.
     */
    public final void start() {
        start(false);
    }


    /**
     * Performs initialization and runs the crawler.
     *
     * @param isResuming indicates if a previously saved state is to be resumed
     */
    private void start(final boolean isResuming) {
        try {
            Validate.validState(isStopped.get(), "The crawler is already running.");

            LOG.debug("Crawler is starting (resuming crawl: {})", isResuming);
            LOG.debug("Using configuration: {}", config);

            isStopped.set(false);
            runTimeStopwatch.start();

            // Must be created here (the adaptive crawl delay strategy depends on the WebDriver)
            crawlDelayMechanism = createCrawlDelayMechanism();

            LOG.debug("Calling onStart callback");
            onStart();

            if(threadSize > 1){
                ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
                for (int i = 0; i < threadSize; i++) {
                    executorService.execute(this::run);
                }
                executorService.shutdown();
                executorService.awaitTermination(100,TimeUnit.DAYS);
            }else{
                run();
            }
        } catch (InterruptedException e) {
            LOG.error("error:", e);
        } finally {
            LOG.debug("Crawler is stopping");

            try {
                LOG.debug("Calling onStop callback");
                onStop();
            } finally {
                runTimeStopwatch.stop();

                isStopInitiated.set(false);
                isStopped.set(true);
            }
        }
    }

    /**
     * Returns the current state of the crawler.
     *
     * @return the current state of the crawler
     */
    public final CrawlerState getState() {
        return new CrawlerState(Arrays.asList(config, crawlFrontier, runTimeStopwatch,
                statsCounter));
    }

    /**
     * Resumes the crawl. The crawler will use HtmlUnit headless browser to visit URLs. This method
     * will block until the crawler finishes.
     */
    public final void resume() {
        start( true);
    }


    /**
     * Registers an operation which is invoked when the specific event occurs and the provided
     * pattern matches the request URL.
     *
     * @param <T>        the type of the input to the operation
     * @param eventClass the runtime class of the event for which the callback should be invoked
     * @param callback   the pattern matching callback to invoke
     */
    protected final <T extends CrawlEvent> void registerCustomCallback(
            final Class<T> eventClass,
            final PatternMatchingCallback<T> callback) {
        Validate.notNull(eventClass, "The eventClass parameter cannot be null.");
        Validate.notNull(callback, "The callback parameter cannot be null.");

        callbackManager.addCustomCallback(eventClass, callback);
    }

    /**
     * Gracefully stops the crawler. This method is thread-safe.
     */
    protected final void stop() {
        Validate.validState(!isStopped.get(), "The crawler is not started.");

        LOG.debug("Initiating stop");

        // Indicate that the crawling should be stopped
        isStopInitiated.set(true);
    }

    /**
     * Feeds a crawl request to the crawler. The crawler should be running, otherwise the request
     * has to be added as a crawl seed instead.
     *
     * @param request the crawl request
     */
    protected final void crawl(final CrawlRequest request) {
        Validate.validState(!isStopped.get(),
                "The crawler is not started. Maybe you meant to add this request as a crawl seed?");
        Validate.notNull(request, "The request parameter cannot be null.");

        crawlFrontier.feedRequest(request, false);
    }

    /**
     * Feeds multiple crawl requests to the crawler. The crawler should be running, otherwise the
     * requests have to be added as crawl seeds instead.
     *
     * @param requests the list of crawl requests
     */
    protected final void crawl(final List<CrawlRequest> requests) {
        requests.forEach(this::crawl);
    }



    protected HttpEntity getResponse(CrawlCandidate candidate) throws IOException {
        if(config.isProxyEnabled()){
            return httpClientContext.doGetByProxy(null, candidate.getRequestUrl().toString());
        }else{
            return httpClientContext.doGet(candidate.getRequestUrl().toString());
        }
    }


    /**
     * Defines the workflow of the crawler.
     */
    private void run() {
        boolean shouldPerformDelay = false;

        while (!isStopInitiated.get() && crawlFrontier.hasNextCandidate()) {
            // Do not perform delay in the first iteration
            if (shouldPerformDelay) {
                performDelay();
            } else {
                shouldPerformDelay = true;
            }

            CrawlCandidate currentCandidate = crawlFrontier.getNextCandidate();
            LOG.debug("Next crawl candidate: {}", currentCandidate);

            String candidateUrl = currentCandidate.getRequestUrl().toString();


            try {
                LOG.debug("Sending HTTP head request to URL {}", candidateUrl);
                HttpEntity httpEntity;
                try {
                    httpEntity = getResponse(currentCandidate);
                } catch (IOException exception) {
                    handleNetworkError(new NetworkErrorEvent(currentCandidate,
                            exception.toString()));
                    inject(currentCandidate,CrawlCandidate.STATUS_DB_FAILED);
                    continue;
                }

                int statusCode = httpEntity.getStatusCode();

                // Check if there was an HTTP redirect
                String location = httpEntity.getLocation();
                if (HttpStatus.isRedirection(statusCode) && location != null) {
                    // Create a new crawl request for the redirected URL (HTTP redirect)
                    CrawlRequest redirectedRequest =
                            createCrawlRequestForRedirect(currentCandidate,
                                    location);

                    handleRequestRedirect(new RequestRedirectEvent(currentCandidate,
                            new PartialCrawlResponse(httpEntity), redirectedRequest));

                    continue;
                }

                if (HttpStatus.isClientError(statusCode) || HttpStatus.isServerError(statusCode)) {
                    handleResponseError(new ResponseErrorEvent(currentCandidate,
                            new CompleteCrawlResponse(httpEntity)));
                    inject(currentCandidate,CrawlCandidate.STATUS_DB_FAILED);
                    continue;
                }

                try {
                    handleResponseSuccess(new ResponseSuccessEvent(currentCandidate,
                            new CompleteCrawlResponse(httpEntity)));

                    ContentType contentType = httpEntity.getContentType();
                    Page page = new Page(currentCandidate, contentType, httpEntity.getContent());

                    handlePageVisit(new PageVisitEvent(currentCandidate,
                            new CompleteCrawlResponse(httpEntity),page));
                    handleNextPage(page);

                }catch (Exception e){
                    handleParseError(new ParseErrorEvent(currentCandidate, e));
                }



            }catch (Exception exception){
                handleNetworkError(new NetworkErrorEvent(currentCandidate,
                        exception.toString()));
                inject(currentCandidate,CrawlCandidate.STATUS_DB_FAILED);
            }
        }
    }


    /**
     * Creates the crawl delay mechanism according to the configuration.
     *
     * @return the created crawl delay mechanism
     */
    private CrawlDelayMechanism createCrawlDelayMechanism() {
        switch (config.getCrawlDelayStrategy()) {
            case FIXED:
                return new FixedCrawlDelayMechanism(config);
            case RANDOM:
                return new RandomCrawlDelayMechanism(config);
            default:
                throw new IllegalArgumentException("Unsupported crawl delay strategy");
        }
    }


    /**
     * Handles network errors.
     *
     * @param event the event which gets delivered when a network error occurs
     */
    private void handleNetworkError(final NetworkErrorEvent event) {
        LOG.debug("Network error occurred: {}", event.getErrorMessage());
        callbackManager.callCustomOrDefault(NetworkErrorEvent.class, event, this::onNetworkError);

        statsCounter.recordNetworkError();
    }

    private void inject(CrawlCandidate crawlCandidate, int status){
        crawlCandidate.setStatus(status);
        try {
            dbManager.writeFetchSegment(crawlCandidate);
        } catch (Exception e) {
            LOG.error("db inject error:",e);
        }
    }

    /**
     * Handles request redirects.
     *
     * @param event the event which gets delivered when a request is redirected
     */
    private void handleRequestRedirect(final RequestRedirectEvent event) {
        LOG.debug("Request redirected from {} to {}",
                event.getCrawlCandidate().getRequestUrl(),
                event.getRedirectedCrawlRequest().getRequestUrl());

        crawl(event.getRedirectedCrawlRequest());

        callbackManager.callCustomOrDefault(RequestRedirectEvent.class, event,
                this::onRequestRedirect);

        statsCounter.recordRequestRedirect();
    }

    /**
     * Handles responses with non-HTML content.
     *
     * @param event the event which gets delivered when the content type of the response is not
     *              text/html
     */
    private void handleNonHtmlResponse(final NonHtmlResponseEvent event) {
        LOG.debug("Received response with non-HTML content");

        callbackManager.callCustomOrDefault(NonHtmlResponseEvent.class, event,
                this::onNonHtmlResponse);

        statsCounter.recordNonHtmlResponse();
    }

    /**
     * Handles page load timeouts.
     *
     * @param event the event which gets delivered when a page does not load in the browser within
     *              the timeout period
     */
    private void handlePageLoadTimeout(final PageLoadTimeoutEvent event) {
        LOG.debug("Page did not load in the client within the timeout period");

        callbackManager.callCustomOrDefault(PageLoadTimeoutEvent.class, event,
                this::onPageLoadTimeout);

        statsCounter.recordPageLoadTimeout();
    }

    /**
     * Handles responses whose HTTP status code indicates an error.
     *
     * @param event the event which gets delivered when the browser loads the page and the HTTP
     *              status code indicates error (4xx or 5xx)
     */
    private void handleResponseError(final ResponseErrorEvent event) {
        LOG.debug("Received response whose status code ({}) indicates error",
                event.getCompleteCrawlResponse().getStatusCode());

        callbackManager.callCustomOrDefault(ResponseErrorEvent.class, event, this::onResponseError);

        statsCounter.recordResponseError();
    }


    private void handlePageVisit(PageVisitEvent event) {
        LOG.info("visit: {}", event.getPage().url());
        callbackManager.callCustomOrDefault(PageVisitEvent.class, event, this::onVisit);
    }

    private void handleNextPage(Page page){
        if (config.getCrawlStrategy() == CrawlStrategy.INCREMENTAL_FIRST
                && page.isContentHasNotChanged()) {
            return;
        }
        List<String> nextPages = page.getNextPages();
        nextPages.forEach(p -> crawl(CrawlRequest.createDefaultWithAttr(p, page.getCrawlCandidate())));
    }

    public void onVisit(PageVisitEvent event){
        try {
            visit(event.getPage());
            inject(event.getCrawlCandidate(),CrawlCandidate.STATUS_DB_SUCCESS);
        }catch (Exception e){
            handleParseError(new ParseErrorEvent(event.getCrawlCandidate(),e));
        }
    }
    public void visit(Page page) throws Exception{  }

    /**
     * Handles responses whose HTTP status code indicates success.
     *
     * @param event the event which gets delivered when the client loads the page and the HTTP
     *              status code indicates success (2xx)
     */
    private void handleResponseSuccess(final ResponseSuccessEvent event) {
        LOG.debug("Received response whose status code ({}) indicates success",
                event.getCompleteCrawlResponse().getStatusCode());

        callbackManager.callCustomOrDefault(ResponseSuccessEvent.class, event,
                this::onResponseSuccess);

        statsCounter.recordResponseSuccess();
    }

    /**
     * Handles parse error.
     *
     * @param event the event which gets delivered when the content parse error.
     */
    protected void handleParseError(final ParseErrorEvent event) {
        LOG.debug("parse content error");
        inject(event.getCrawlCandidate(),CrawlCandidate.STATUS_DB_FAILED);

        callbackManager.callCustomOrDefault(ParseErrorEvent.class, event,
                this::onParseError);

         statsCounter.recordParseError();
    }

    /**
     * Delays the next request.
     */
    private void performDelay() {
        LOG.debug("Performing delay");

        try {
            TimeUnit.MILLISECONDS.sleep(crawlDelayMechanism.getDelay());
        } catch (InterruptedException ex) {
            LOG.debug("Delay interrupted, stopping crawler");
            Thread.currentThread().interrupt();
            isStopInitiated.set(true);
        }
    }

    /**
     * Helper method that is used to create crawl requests for redirects. The newly created request
     * will have the same attributes as the redirected one.
     *
     * @param currentCandidate the current crawl candidate
     * @param redirectUrl      the redirect URL
     *
     * @return the crawl request for the redirect URL
     */
    private static CrawlRequest createCrawlRequestForRedirect(
            final CrawlCandidate currentCandidate,
            final String redirectUrl) {
        // Handle relative redirect URLs
        URI resolvedUrl = currentCandidate.getRequestUrl().resolve(redirectUrl);

        CrawlRequest.CrawlRequestBuilder builder = new CrawlRequest.CrawlRequestBuilder(resolvedUrl)
                .setPriority(currentCandidate.getPriority());
        Optional.ofNullable(currentCandidate.getExtInfo()).ifPresent(builder::setExtInfo);
        currentCandidate.getMetadata().ifPresent(builder::setMetadata);
       

        return builder.build();
    }

    /**
     * Callback which gets called when the crawler starts.
     */
    protected void onStart(){
        LOG.info("onStart");

        crawlFrontier.merge();
        crawlFrontier.feedCrawlSeeds();
    }

    /**
     * Callback which gets called when the browser loads the page and the HTTP status code of the
     * response indicates success (2xx).
     *
     * @param event the <code>ResponseSuccessEvent</code> instance
     */
    protected void onResponseSuccess(final ResponseSuccessEvent event)  {
        LOG.info("onResponseSuccess: {}", event.getCrawlCandidate().getRequestUrl());
    }

    /**
     * Callback which gets called when the content type of the response is not text/html.
     *
     * @param event the <code>NonHtmlResponseEvent</code> instance
     */
    protected void onNonHtmlResponse(final NonHtmlResponseEvent event) {
        LOG.info("onNonHtmlResponse: {}", event.getCrawlCandidate().getRequestUrl());
    }

    /**
     * Callback which gets called when a network error occurs.
     *
     * @param event the <code>NetworkErrorEvent</code> instance
     */
    protected void onNetworkError(final NetworkErrorEvent event) {
        LOG.info("onNetworkError: {}", event.getErrorMessage());
    }

    /**
     * Callback which gets called when the browser loads the page and the HTTP status code of the
     * response indicates error (4xx or 5xx).
     *
     * @param event the <code>ResponseErrorEvent</code> instance
     */
    protected void onResponseError(final ResponseErrorEvent event) {
        LOG.info("onResponseError: {}", event.getCrawlCandidate().getRequestUrl());
    }

    /**
     * Callback which gets called when the browser loads the page and the HTTP status code of the
     * response indicates error (4xx or 5xx).
     *
     * @param event the <code>ResponseErrorEvent</code> instance
     */
    protected void onParseError(final ParseErrorEvent event) {
        LOG.info("onParseError: {}", event.getCrawlCandidate().getRequestUrl());
    }

    /**
     * Callback which gets called when a request is redirected.
     *
     * @param event the <code>RequestRedirectEvent</code> instance
     */
    protected void onRequestRedirect(final RequestRedirectEvent event) {
        LOG.info("onRequestRedirect: {} -> {}", event.getCrawlCandidate().getRequestUrl(),
                event.getRedirectedCrawlRequest().getRequestUrl());
    }

    /**
     * Callback which gets called when the page does not load in the browser within the timeout
     * period.
     *
     * @param event the <code>PageLoadTimeoutEvent</code> instance
     */
    protected void onPageLoadTimeout(final PageLoadTimeoutEvent event) {
        LOG.info("onPageLoadTimeout: {}", event.getCrawlCandidate().getRequestUrl());
    }

    /**
     * Callback which gets called when the crawler stops.
     */
    protected void onStop() {
        LOG.info("onStop");
    }
}
