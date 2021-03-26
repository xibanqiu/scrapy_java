package com.github.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.crawler.api.CrawlStats;
import com.github.crawler.entity.PremiumBoard;
import com.github.crawler.entity.SpiderNewImgStat;
import com.github.crawler.service.PremiumBoardService;
import com.github.crawler.service.UserBoardFavouriteService;
import com.github.crawler.tools.HttpClientTools;
import com.github.db.ObjectFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;


@Component
public abstract class BaseTask {
	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
	protected final Logger STATS_LOG = LoggerFactory.getLogger("statsLog");

	public abstract void start();

	@Autowired
	private PremiumBoardService premiumBoardService;

	protected Integer siteId;

	private  int Flag_Recursive = 0;
	

	protected Integer flagFirstScrape; //
	@Autowired
	private UserBoardFavouriteService userBoardFavouriteService;


	@Value("${jhk.stats.url}")
	protected String jhk_stats;

	static final String statApiPath = "pupb/newIncreaseImgs/stats";

	@Value("${es.stats.crawler2}")
	String es_stats_crawler;
	@Value("${es.stats.url}")
	protected String es_stats_url;
	protected Supplier<String> formatYMD = () -> LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);


	protected void newImgStat(Long  id ,Integer flag_first_scrape,Integer origin_type) {
		Thread thread = new Thread(() -> {
			SpiderNewImgStat pinNewImgStat = new SpiderNewImgStat();
			pinNewImgStat.setId(id+"");
			pinNewImgStat.setOrigin_type(origin_type);
			pinNewImgStat.setFlag_first_scrape(flag_first_scrape);
			HttpClientTools.doPost(jhk_stats + statApiPath, JSON.toJSONString(pinNewImgStat));
		});
		thread.start();
	}

	/**
	 * get Board id list
	 * @param list
	 * @return
	 */
	protected List<Long> getIdList(List<IdSet> list){
		return list.stream().map(IdSet::getBoardId).collect(Collectors.toList());
	}


	protected void crawlerStat(Long boardId, Long dzbId, Integer flagFirstScrape, String type, CrawlStats crawlStats) {
		new Thread(() -> {
			JSONObject json = (JSONObject) JSONObject.toJSON(crawlStats);
			json.put("type", type);
			json.put("timestamp", System.currentTimeMillis());
			json.put("dzbId", dzbId);
			json.put("flagFirstScrape", flagFirstScrape);
			json.put("id", boardId);

			try {
				String url = es_stats_url + es_stats_crawler + "/" + formatYMD.get();
				HttpClientTools.doPost(url, json.toJSONString());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	/**
	 *
	 * @param idList
	 */
	protected void updateFlagFirstScrape(List<Long> idList){
		idList.forEach(premiumBoardService::updateFlagFirstScrape);
	}

	/**
	 *
	 * @param boardId
	 */
	protected void updateFlagFirstScrape(Long boardId){
		premiumBoardService.updateFlagFirstScrape(boardId);
	}
	protected void updateFlagTopic(Long boardId){
		premiumBoardService.updateFlagTopic(boardId);
	}

	@PostConstruct
	public void init(){
		if(flagFirstScrape == 1){
			File file = new File(siteId + ".bin");
			try{
				long id = ObjectFileUtil.readLong(file);
				LOG.info("加载的 id{}",id);
				premiumBoardService.setNextId(id);
			} catch (IOException e) {
				LOG.error("read id error:", e);
			}
		}

	}

	public void store(long id) {
		if (flagFirstScrape == 1) {
			File file = new File(siteId + ".bin");
			try {
				ObjectFileUtil.writeLong(file, id);
			} catch (IOException e) {
				LOG.error("store id error:", e);
			}
		}
	}


	protected List<IdSet> getIds() {
		List<IdSet> rs = new ArrayList<>();
		List<PremiumBoard> list = premiumBoardService.pageBySite(siteId,flagFirstScrape);
		if (list == null || list.size() == 0) {
			store(0); //重置ID
			return rs;
		}else{
			store(list.get(list.size() -1).getId()); //记录next id
		}

		for (PremiumBoard board : list) {
			try {
				Long id = board.getId();
				Long dzbId = board.getDzb_id();
				Long dzId = userBoardFavouriteService.selectUidById(dzbId);

//				if (null == dzId) {
//					LOG.info("board_id :{}没有同步用户pass", id);
//					continue;
//				}

				String idString = board.getId_string();
				if (StringUtils.isBlank(idString)) {
					LOG.info("id_string为空pass");
					continue;
				}
				LOG.info("boardId={}----dzId={}----dzbId={}", id, dzId, dzbId);
				IdSet idSet = new IdSet();
				idSet.setBoardId(id);
				idSet.setDzId(dzId);
				idSet.setDzbId(dzbId);
				idSet.setIdString(idString);
				idSet.setName(board.getName());
				idSet.setAddDesc(board.getAdd_desc());
				idSet.setFlagTopic(board.getFlag_topic());
				
				idSet.setIdNumber(board.getId_number());
				rs.add(idSet);
			} catch (Exception e) {
				LOG.error("board初始化爬取异常！   boardMsg = {}, error ={}", JSON.toJSONString(board), e);
				e.printStackTrace();
			}
		}
		return rs;

	}

	static class IdSet{
		private Long boardId;
		private Long dzId;
		private Long dzbId;
		private String idString;
		private String name;
		private String idNumber;
		private Integer flagTopic;
		private Integer addDesc;
		private Long baseId;


		public Long getBaseId() {
			return baseId;
		}

		public void setBaseId(Long baseId) {
			this.baseId = baseId;
		}

		public Integer getFlagTopic() {
			return flagTopic;
		}

		public void setFlagTopic(Integer flagTopic) {
			this.flagTopic = flagTopic;
		}

		public Integer getAddDesc() {
			return addDesc;
		}

		public void setAddDesc(Integer addDesc) {
			this.addDesc = addDesc;
		}

		public String getName() {
			return name;
		}

		public String getIdNumber() {
			return idNumber;
		}

		public void setIdNumber(String idNumber) {
			this.idNumber = idNumber;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Long getBoardId() {
			return boardId;
		}

		public void setBoardId(Long boardId) {
			this.boardId = boardId;
		}

		public Long getDzId() {
			return dzId;
		}

		public void setDzId(Long dzId) {
			this.dzId = dzId;
		}

		public Long getDzbId() {
			return dzbId;
		}

		public void setDzbId(Long dzbId) {
			this.dzbId = dzbId;
		}

		public String getIdString() {
			return idString;
		}

		public void setIdString(String idString) {
			this.idString = idString;
		}

		@Override
		public String toString() {
			return "IdSet{" +
					"boardId=" + boardId +
					", dzId=" + dzId +
					", dzbId=" + dzbId +
					", idString='" + idString + '\'' +
					", flagTopic='" + flagTopic + '\'' +
					", addDesc='" + addDesc + '\'' +
					'}';
		}

	}

	public static void main(String[] args) {
		HttpClientTools.doPost("http://116.62.62.220:9100/stats_screenshot/20200924", "{\"shop_id\":49,\"shop_board_id\":93,\"id\":11,\"timestamp\":1600936297303}");
	}

}
