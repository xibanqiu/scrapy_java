package com.github.crawler.spider;

import com.github.crawler.api.Crawler;
import com.github.crawler.api.CrawlerConfiguration;
import com.github.crawler.api.CrawlerState;
import com.github.crawler.api.event.ParseErrorEvent;
import com.github.crawler.entity.BaseEntity;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class BaseCrawler extends Crawler {

    protected Consumer<BaseEntity> consumer;
    protected Consumer<ParseErrorEvent> errorEventConsumer;
    protected Predicate<String> duplicateFilter;

    // 返回值 Integer类型的值 的定义 1、为 基础表和 中间表都插入数据  2、为 中间表都插入数据 3、为重复数据
    protected Function<BaseEntity,Integer> duplicateFilter2;

    public BaseCrawler(CrawlerConfiguration config) {
        super(config);
    }

    public BaseCrawler(CrawlerState state) {
        super(state);
    }

    public void setConsumer(Consumer<BaseEntity> consumer) {
        this.consumer = consumer;
    }

    protected Predicate<String> filter;

    public void setFilter(Predicate<String> filter) {
        this.filter = filter;
    }

    public void setDuplicateFilter(Predicate<String> duplicateFilter) {
        this.duplicateFilter = duplicateFilter;
    }

    public void setDuplicateFilter2( Function<BaseEntity,Integer>  duplicateFilter2) {
        this.duplicateFilter2 = duplicateFilter2;
    }

    public void setErrorEventConsumer(Consumer<ParseErrorEvent> errorEventConsumer) {
        this.errorEventConsumer = errorEventConsumer;
    }

    protected boolean duplicateItem(String identity) {
        if(null != duplicateFilter && duplicateFilter.test(identity)){
            this.recordDuplicateItem(); //统计重复图片
            return true;
        }
        this.recordParseItem();
        return false;
    }

    protected boolean duplicateItem2(BaseEntity baseEntity) {

        if(null == duplicateFilter2){
            this.recordParseItem();
            return false;
        };

        Integer duplicateFlag = duplicateFilter2.apply(baseEntity);

        if(duplicateFlag == 1){
            this.recordParseItem();
            return false;
        }else if( duplicateFlag == 2 ){
            this.recordParserRelationItemCount();
            return true;
        }else if( duplicateFlag == 3 ){
            this.recordDuplicateItem(); //统计重复图片
            return true;
        }else {
            throw new RuntimeException( duplicateFlag+" 为错误 的返回值类型");
        }

    }

    protected boolean filterItem(String identity) {
        if(null != filter && filter.test(identity)){
            this.recordFilteredItem(); //统计被过滤图片
            return true;
        }
        return false;
    }

    /**
     * 计算总页数
     * @param pageSize
     * @param totalSize
     * @return
     */
    protected int calculatePage(int pageSize, int totalSize){
        return totalSize % pageSize == 0 ? (totalSize / pageSize): (totalSize / pageSize + 1);
    }
}
