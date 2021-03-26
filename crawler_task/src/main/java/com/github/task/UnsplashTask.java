package com.github.task;


import com.alibaba.fastjson.JSON;
import com.github.crawler.api.CrawlRequest;
import com.github.crawler.api.CrawlerConfiguration;
import com.github.crawler.entity.BaseEntity;
import com.github.crawler.entity.UnsplashBoardImg;
import com.github.crawler.service.SequencePinWordNextIdService;
import com.github.crawler.service.UnsplashBoardImgService;
import com.github.crawler.spider.UnsplashSpider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class UnsplashTask extends BaseTask {
	@Autowired
	private UnsplashBoardImgService unsplashBoardImgService;
	@Autowired
	private SequencePinWordNextIdService sequencePinWordNextIdService;

	@Autowired
	public UnsplashTask(@Value("0") int flagFirstScrape) {
		siteId = 15;
		this.flagFirstScrape = flagFirstScrape;
	}

	private static final String PRE_URL = "https://unsplash.com/napi/#id_string#/photos?page=1&per_page=30&order_by=latest";

	public void start() {

		while(true) {
			List<IdSet> idSets = getIds();
			if(idSets.isEmpty()) {
				LOG.info("unsplash board 爬取结束  ... !");
				return;
			}

			idSets.forEach(it ->{

				String id_string = it.getIdString();
				if (id_string.contains("@")) {
					id_string = id_string.replace("@", "users/");
				}

				String url = PRE_URL.replace("#id_string#", id_string);

				CrawlRequest.CrawlRequestBuilder builder = new CrawlRequest.CrawlRequestBuilder(url);

				Map<String,Object> map = new HashMap<>();
				map.put("dzb_id",it.getDzbId());
				map.put("dz_id",it.getDzId());
				builder.setExtInfo(map);
				process(builder.build(), it.getBoardId());
				updateFlagFirstScrape(it.getBoardId());
			});

		}
	}

	private void process(CrawlRequest request, Long boardId) {
		CrawlerConfiguration config =new CrawlerConfiguration.CrawlerConfigurationBuilder()
				.setOffsiteRequestFilterEnabled(false)     //是否对域名进行过滤
				//.addAllowedCrawlDomain("")   //设置  允许访问的域名
				.setDuplicateRequestFilterEnabled(true)  // 去重
				.setProxyEnabled(true) //配置是否使用代理
				.addCrawlSeed(request)          //将链接放到seed 中，等待爬取
				.build();

		UnsplashSpider unsplashSpider = new UnsplashSpider(config);
		unsplashSpider.setConsumer(this::insertDb);
		unsplashSpider.setDuplicateFilter(s -> {
			Long countStocksnapImageId = unsplashBoardImgService.countUnsplashImageId(s);
			if(countStocksnapImageId > 0) {
				LOG.info("unsplash图片爬取过了  originalSiteId={}", s);
				return true;
			}
			return false;
		});

		unsplashSpider.resume();
		STATS_LOG.info(" board_id = {}, crawl stats = {}", boardId, JSON.toJSONString(unsplashSpider.getCrawlStats()));
	}

	private void insertDb(BaseEntity entity) {
		UnsplashBoardImg img = (UnsplashBoardImg) entity;
		try {
			img.setFlag_download_img(0);
			img.setFlag_sync_jhk_img(0);
			img.setFlag_sync_user_img_favourite(0);

			long id = sequencePinWordNextIdService.nextId();
			img.setId(id);
			unsplashBoardImgService.insertEntity(img);
			LOG.info("unsplash图片爬取  imgId={} -- title={}--", img.getId(), img.getTitle());
//			newImgStat(id, flagFirstScrape, OriginTypeEnum.UNSPLASH.getValue());
		} catch (Exception e) {
			LOG.error("图片添加到mysql异常！ imgdata = {} , error = {}", JSON.toJSONString(img), e);
		}
	}
}
