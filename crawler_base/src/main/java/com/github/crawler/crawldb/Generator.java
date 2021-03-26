package com.github.crawler.crawldb;


import com.github.crawler.api.CrawlCandidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抓取任务生成器
 *
 */
public abstract class Generator {

    public static final Logger LOG = LoggerFactory.getLogger(Generator.class);

    protected GeneratorFilter filter = null;
    protected int totalGenerate;


    public Generator() {
        this.totalGenerate = 0;
    }


    /**
     * return null if there is no CrawlCandidate to generate
     * @return
     */
    public CrawlCandidate next(){
        int topN = Integer.MAX_VALUE; //getConf().getTopN();
        int maxExecuteCount = 3;//getConf().getOrDefault(Configuration.KEY_MAX_EXECUTE_COUNT, Integer.MAX_VALUE);

        if(topN > 0 && totalGenerate >= topN){
            return null;
        }

        CrawlCandidate crawlCandidate;
        while (true) {
            try {
                crawlCandidate = nextWithoutFilter();
                if (crawlCandidate == null) {
                    return crawlCandidate;
                }
                if(filter == null || (crawlCandidate = filter.filter(crawlCandidate))!=null){
                    if (crawlCandidate.getExecuteCount() > maxExecuteCount) {
                        continue;
                    }
                    totalGenerate += 1;
                    return crawlCandidate;
                }

            } catch (Exception e) {
                LOG.info("Exception when generating", e);
                return null;
            }

        }
    }

    public abstract CrawlCandidate nextWithoutFilter() throws Exception;


//    public int getTopN() {
//        return topN;
//    }

//    public void setTopN(int topN) {
//        this.topN = topN;
//    }


//    public int getMaxExecuteCount() {
//        return maxExecuteCount;
//    }
//
//    public void setMaxExecuteCount(int maxExecuteCount) {
//        this.maxExecuteCount = maxExecuteCount;
//    }

    public int getTotalGenerate(){
        return totalGenerate;
    }

    public abstract void close() throws Exception;

    public GeneratorFilter getFilter() {
        return filter;
    }

    public void setFilter(GeneratorFilter filter) {
        this.filter = filter;
    }
}
