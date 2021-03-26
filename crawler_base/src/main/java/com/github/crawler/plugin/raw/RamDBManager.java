package com.github.crawler.plugin.raw;


import com.github.crawler.api.CrawlCandidate;
import com.github.crawler.crawldb.DBManager;
import com.github.crawler.crawldb.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map.Entry;


public class RamDBManager extends DBManager {

    Logger LOG = LoggerFactory.getLogger(DBManager.class);

    public RamDB ramDB;
    public RamGenerator generator=null;

    public RamDBManager(RamDB ramDB) {
        this.ramDB = ramDB;
        this.generator=new RamGenerator(ramDB);
    }

    @Override
    public boolean isDBExists() {
        return true;
    }

    @Override
    public void clear() throws Exception {
        ramDB.crawlDB.clear();
        ramDB.fetchDB.clear();
        ramDB.linkDB.clear();
        ramDB.redirectDB.clear();
    }

    @Override
    protected Generator createGenerator() {
        return new RamGenerator(ramDB);
    }


    @Override
    public void open() throws Exception {
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public void inject(CrawlCandidate crawlCandidate, boolean force) throws Exception {
        String key = crawlCandidate.key();
        if (!force) {
            if (ramDB.crawlDB.containsKey(key)) {
                return;
            }
        }
        ramDB.crawlDB.put(key, crawlCandidate);
    }


    @Override
    public void merge() throws Exception {
        LOG.info("start merge");

        /*合并fetch库*/
        LOG.info("merge fetch database");
        for (Entry<String, CrawlCandidate> fetchEntry : ramDB.fetchDB.entrySet()) {
            ramDB.crawlDB.put(fetchEntry.getKey(), fetchEntry.getValue());
        }

        /*合并link库*/
        LOG.info("merge link database");
        for (String key : ramDB.linkDB.keySet()) {
            if (!ramDB.crawlDB.containsKey(key)) {
                ramDB.crawlDB.put(key, ramDB.linkDB.get(key));
            }
        }

        LOG.info("end merge");

        ramDB.fetchDB.clear();
        LOG.debug("remove fetch database");
        ramDB.linkDB.clear();
        LOG.debug("remove link database");

    }

    @Override
    public void initSegmentWriter() throws Exception {
    }

    @Override
    public synchronized void writeFetchSegment(CrawlCandidate fetchDatum) throws Exception {
        ramDB.fetchDB.put(fetchDatum.key(), fetchDatum);
    }


    @Override
    public void closeSegmentWriter() throws Exception {
    }
 
}
