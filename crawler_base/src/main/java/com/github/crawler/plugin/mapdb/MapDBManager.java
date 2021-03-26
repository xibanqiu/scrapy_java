package com.github.crawler.plugin.mapdb;


import com.github.crawler.api.CrawlCandidate;
import com.github.crawler.crawldb.DBManager;
import com.github.crawler.crawldb.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map.Entry;
import java.util.Objects;


public class MapDBManager extends DBManager {

    Logger LOG = LoggerFactory.getLogger(DBManager.class);

    public MapDB mapDB;
    public MapDBGenerator generator=null;

    public MapDBManager(MapDB mapDB) {
        this.mapDB = mapDB;
        this.generator=new MapDBGenerator(mapDB);
    }

    @Override
    public boolean isDBExists() {
        return true;
    }

    @Override
    public void clear() throws Exception {
        mapDB.crawlDB.clear();
        mapDB.fetchDB.clear();
        mapDB.linkDB.clear();
    }

    @Override
    protected Generator createGenerator() {
        return new MapDBGenerator(mapDB);
    }


    @Override
    public void open() throws Exception {
    }

    @Override
    public void close() {
        if(Objects.nonNull(mapDB) && Objects.nonNull(mapDB.db))
            mapDB.db.close();
    }

    @Override
    public void inject(CrawlCandidate crawlCandidate, boolean force) throws Exception {
        String key = crawlCandidate.key();
        if (!force) {
            if (mapDB.crawlDB.containsKey(key)) {
                return;
            }
        }
        mapDB.crawlDB.put(key, crawlCandidate.toJSONString());
    }


    @Override
    public void merge() throws Exception {
        LOG.info("start merge");

        /*合并fetch库*/
        LOG.info("merge fetch database");
        for (Entry<String, String> fetchEntry : mapDB.fetchDB.entrySet()) {
            mapDB.crawlDB.put(fetchEntry.getKey(), fetchEntry.getValue());
        }

        /*合并link库*/
        LOG.info("merge link database");
        for (String key : mapDB.linkDB.keySet()) {
            if (!mapDB.crawlDB.containsKey(key)) {
                mapDB.crawlDB.put(key, mapDB.linkDB.get(key));
            }
        }

        LOG.info("end merge");

        mapDB.fetchDB.clear();
        LOG.debug("remove fetch database");
        mapDB.linkDB.clear();
        LOG.debug("remove link database");

    }

    @Override
    public void initSegmentWriter() throws Exception {
    }

    @Override
    public synchronized void writeFetchSegment(CrawlCandidate fetchDatum) throws Exception {
        mapDB.fetchDB.put(fetchDatum.key(), fetchDatum.toJSONString());
    }


    @Override
    public void closeSegmentWriter() throws Exception {
    }
 
}
