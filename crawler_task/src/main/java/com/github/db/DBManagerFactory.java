package com.github.db;

import com.github.crawler.crawldb.DBManager;
import com.github.crawler.plugin.mapdb.MapDB;
import com.github.crawler.plugin.mapdb.MapDBManager;
import com.github.crawler.plugin.raw.RamDB;
import com.github.crawler.plugin.raw.RamDBManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class DBManagerFactory {
    protected static final Logger LOG = LoggerFactory.getLogger(DBManagerFactory.class);

    static DBManager dbManager = null;

    public static DBManager createDBManager(int type, String crwalPath, boolean reset){
        if(type == 0){
            return createMapDBManager(crwalPath, reset);
        }else if(type == 1){
            return createRamDBManager();
        }else {
            throw new IllegalArgumentException("wrong type");
        }
    }

    private static DBManager createRamDBManager(){
        return new RamDBManager(new RamDB());
    }
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(DBManagerFactory::beforeClose));
    }

    private static void beforeClose(){
        if(null != dbManager) {
            try {
                dbManager.close();
            } catch (Exception e) {
                LOG.error("dbManager error:", e);
            }
        }
    }


    private static DBManager createMapDBManager(String crwalPath, boolean reset){
        if(Objects.nonNull(dbManager)) beforeClose();
        dbManager = new MapDBManager(new MapDB(crwalPath));
        try {
            dbManager.open();
            if (reset) {
                dbManager.clear();
            }else{
                dbManager.merge();
            }
            dbManager.initSegmentWriter();
            return dbManager;
        }catch (Exception e){
            LOG.error("dbManager error:", e);
        }
        return null;
    }

}
