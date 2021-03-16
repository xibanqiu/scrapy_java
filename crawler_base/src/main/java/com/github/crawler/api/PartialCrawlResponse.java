package com.github.crawler.api;


import com.github.crawler.http.HttpEntity;
import org.apache.http.Header;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents a partial response that only contains HTTP header information.
 */
public class PartialCrawlResponse {

    private final HttpEntity httpEntity;

    /**
     * Creates a {@link PartialCrawlResponse} instance from an HTTP response message.
     *
     * @param httpEntity the HTTP response message
     */
    public PartialCrawlResponse(final HttpEntity httpEntity) {
        this.httpEntity = httpEntity;
    }


    /**
     * Returns the HTTP status code of the response.
     *
     * @return the HTTP status code
     */
    public int getStatusCode() {
        return httpEntity.getStatusCode();
    }

    /**
     * Returns the status message corresponding to the status code of the response.
     *
     * @return the status message
     */
    public String getStatusText() {
        return httpEntity.getStatusText();
    }

    /**
     * Returns all the headers of the response.
     *
     * @return all the headers
     */
    public List<Header> getAllHeaders() {
        return httpEntity.getHeaders();
    }

    /**
     * Returns all the headers with the specified name of the response.
     *
     * @param name the name of the headers
     *
     * @return all the headers with the specified name
     */
    public List<Header> getHeaders(final String name) {
        return httpEntity.getHeaders().stream()
                .filter(header -> name.equals(header.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Returns the first header with the specified name of the response.
     *
     * @param name the name of the header
     *
     * @return the first header with the specified name
     */
    public Optional<Header> getFirstHeader(final String name) {
        return httpEntity.getHeaders().stream()
                .filter(header -> name.equals(header.getName()))
                .findFirst();
    }

}

