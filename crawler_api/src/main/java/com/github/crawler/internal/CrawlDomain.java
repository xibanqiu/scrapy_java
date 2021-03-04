package com.github.crawler.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.net.InternetDomainName;

import java.io.Serializable;

/**
 *  Represent an internet domain in which crawling is allowed
 */
public class CrawlDomain implements Serializable {

    private final String domain;
    private final ImmutableList<String> parts;


    /**
     * Create a <code> CrawlDomain </code> instance
     *
     * @param domain an immutable well-formed internet domain name
     */
    public CrawlDomain(final InternetDomainName domain) {
        this.domain = domain.toString();
        this.parts = domain.parts();
    }

    /**
     * Returns the domain name,normalized ta all lower case.
     * @return the domain name
     */
    public String getDomain(){return domain;}


    @Override
    public boolean equals(Object obj) {

        if(obj == this){
            return true;
        }

        if(obj instanceof CrawlDomain){
            CrawlDomain other = (CrawlDomain) obj;

            return parts.equals(other.parts);
        }

        return false;
    }


    @Override
    public int hashCode() {
        return parts.hashCode();
    }

    public boolean contains(final InternetDomainName domain){

        ImmutableList<String> otherDomainParts = domain.parts();

        if(parts.size() > otherDomainParts.size()){
            return false;
        }

        otherDomainParts = otherDomainParts.reverse().subList(0, parts.size());

        return parts.reverse().equals(otherDomainParts);


    }

    @Override
    public String toString() {
        return domain;
    }
}
