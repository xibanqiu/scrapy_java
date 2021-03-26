package com.github.crawler.crawldb;


import com.github.crawler.api.CrawlCandidate;

/**
 *
 */
public abstract class DBManager implements Injector, SegmentWriter{

    public abstract boolean isDBExists();

    public abstract void clear() throws Exception;

    protected abstract Generator createGenerator() throws Exception;

    public Generator createGenerator(GeneratorFilter generatorFilter) throws Exception {
        Generator generator = createGenerator();
        generator.setFilter(generatorFilter);
        return generator;
    }

    public abstract void open() throws Exception;

    public abstract void close() throws Exception;

    public abstract void inject(CrawlCandidate crawlCandidate, boolean force) throws Exception;


    public abstract void merge() throws Exception;

    public void inject(CrawlCandidate crawlCandidate) throws Exception {
        inject(crawlCandidate, false);
    }


}
