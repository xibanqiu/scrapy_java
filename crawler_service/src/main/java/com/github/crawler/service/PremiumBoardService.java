package com.github.crawler.service;


import com.github.crawler.entity.PremiumBoard;
import com.github.crawler.mapper.PremiumBoardMapper;
import com.github.crawler.mapper.provider.bean.*;
import com.github.crawler.tools.RuntimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PremiumBoardService extends BaseService {
    @Autowired
    PremiumBoardMapper premiumBoardMapper;

    public volatile static int size = 100;
    private long nextId = 0;

    public void setNextId(long nextId) {
        this.nextId = nextId;
    }

    public List<PremiumBoard> pageBySite(int site, int flagFirstScrape){
        RuntimeUtils rt = new RuntimeUtils();
        WhereOrderLimit wol = new WhereOrderLimit();
        wol.addWhere(Where.equal("site",site));
        wol.addWhere(Where.equal("flag_first_scrape",flagFirstScrape));
        wol.addWhere(Where.equal("submit",1));
        wol.addWhere(Where.greater("id", nextId));
        wol.addWhere(Where.greater("dzb_id", 0));
        wol.addOrder(Order.ascById());
        wol.addLimit(Limit.toLimit(size, 1));

        List<PremiumBoard> list = premiumBoardMapper.selectSqlWOL(wol, PremiumBoard.class);
        
        if (list.isEmpty()) {
            LOG.info("list is Empty");
            return null;
        }
        LOG.info(" nextId={} , size={} , ms={}", nextId, list.size(), rt.toString());
        nextId = list.get(list.size() - 1).getId();

        return list;
    }
    
	/**
	 * 获取合并程序运行时所需的ID
	 * @return 程序运行时所需的ID
	 * @author xubenqing
	 * @date 2020/2/20
	 * 
	 */
    public List<PremiumBoard> mergePageBySite(Long merge_board_id,int flagFirstScrape){
        RuntimeUtils rt = new RuntimeUtils();
        WhereOrderLimit wol = new WhereOrderLimit();
        wol.addWhere(Where.equal("flag_first_scrape",flagFirstScrape));
        wol.addWhere(Where.equal("submit",3));
        wol.addWhere(Where.equal("merge_board_id",merge_board_id));
        wol.addWhere(Where.greater("dzb_id", 0));

        List<PremiumBoard> list = premiumBoardMapper.selectSqlWOL(wol, PremiumBoard.class);
        
        if (list.isEmpty()) {
            LOG.info(" mergePageBySite merge_board_id = {}  ,list is Empty",merge_board_id);
            return null;
        }

        return list;
    }


    public void updateFlagFirstScrape(Long id) {
        Sets sets = new Sets();
        sets.add(new Set("flag_first_scrape", 1));
        premiumBoardMapper.updateById(sets, id, PremiumBoard.class);
    }
    
    public void updateFlagTopic(Long id) {
        Sets sets = new Sets();
        sets.add(new Set("flag_topic", 1));
        premiumBoardMapper.updateById(sets, id, PremiumBoard.class);
    }

}
