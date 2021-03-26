package com.github.crawler.plugin.raw;


import com.github.crawler.api.CrawlCandidate;
import com.github.crawler.crawldb.Generator;

import java.util.Iterator;
import java.util.Map;

public class RamGenerator extends Generator {

    RamDB ramDB;

    public RamGenerator(RamDB ramDB) {
        this.ramDB = ramDB;
        iterator = ramDB.crawlDB.entrySet().iterator();
    }

    Iterator<Map.Entry<String, CrawlCandidate>> iterator;

    @Override
    public CrawlCandidate nextWithoutFilter() throws Exception {
        if(iterator.hasNext()){
            return iterator.next().getValue();
        }else{
            return null;
        }
    }

    @Override
    public void close() throws Exception {

    }


}
