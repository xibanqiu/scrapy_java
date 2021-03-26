
package com.github.crawler.plugin.mapdb;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.util.Map;


public class MapDB {
    protected DB db;
    protected Map<String, String> crawlDB;
    protected Map<String, String> fetchDB;
    protected Map<String, String> linkDB;

    public MapDB(String fileName) {
        db = DBMaker.fileDB(fileName).fileMmapEnable().checksumHeaderBypass().closeOnJvmShutdown().make();

        crawlDB = db.hashMap("crawlDB")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.STRING)
                .counterEnable()
                .createOrOpen();


        fetchDB = db.hashMap("fetchDB")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.STRING)
                .counterEnable()
                .createOrOpen();

        linkDB = db.hashMap("linkDB")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.STRING)
                .counterEnable()
                .createOrOpen();
    }

}
