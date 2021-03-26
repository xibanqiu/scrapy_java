package com.github.crawler.plugin.mapdb;

import com.alibaba.fastjson.JSONObject;
import com.github.crawler.api.CrawlCandidate;
import com.github.crawler.crawldb.Generator;

import java.util.Iterator;
import java.util.Map.Entry;

public class MapDBGenerator extends Generator {

    MapDB mapDB;

    public MapDBGenerator(MapDB mapDB) {
        this.mapDB = mapDB;
        iterator = mapDB.crawlDB.entrySet().iterator();
    }

    Iterator<Entry<String, String>> iterator;

    @Override
    public CrawlCandidate nextWithoutFilter() throws Exception {
        if(iterator.hasNext()){

            Entry<String, String> next = iterator.next();
            String jsonStr = next.getValue();
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);

            return CrawlCandidate.fromJsonObject(next.getKey(),jsonObject);
        }else{
            return null;
        }
    }

    @Override
    public void close() throws Exception {

    }


}
