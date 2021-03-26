/**
 * 
 */
package com.github.crawler.entity;

import java.util.Date;

/**
 * pin网新爬图片统计
 *@author huangwen
 *@date 2019年3月11日
 */
public class SpiderNewImgStat {
	private String id;//pin_id
	private Date timestamp;
	private Integer flag_first_scrape;//0表示新board(第一次爬取),1表示老board(新增图片)
	private Integer origin_type;//1表示pin,2表示花瓣,3表示behance,4表示ins

	public Integer getOrigin_type() {
		return origin_type;
	}

	public void setOrigin_type(Integer origin_type) {
		this.origin_type = origin_type;
	}

	public Integer getFlag_first_scrape() {
		return flag_first_scrape;
	}

	public void setFlag_first_scrape(Integer flag_first_scrape) {
		this.flag_first_scrape = flag_first_scrape;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
