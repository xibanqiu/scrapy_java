package com.github.crawler.entity;


import com.github.crawler.mapper.provider.bean.TableName;

import java.util.Date;

@TableName("board")
public class PremiumBoard extends BaseEntity {
    private Long id;

    private Long dzb_id;

    private Long user_id;

    private String id_number;

    private String id_string;

    private String industry;

    private String key_words;

    private String original_name;

    private String url;

    private String name;

    private String original_des;

    private String des;

    private Integer pins;

    private Integer followers;

    private Integer category_id;

    private Integer source;

    private Integer status;

    private Integer excellent;

    private Date create_time;

    private Date update_time;

    private Integer collaborators;

    private String privacy;

    private String reason;

    private String image_url;

    private Integer image_width;

    private Integer image_height;

    private Integer rank;

    private String industry_words;
    private Date taken_at_timestamp;

    private String negative_words;
    private Integer submit;
    private Integer flag_first_scrape;
    
    private Integer merge_board_id;
    private Integer flag_topic;
    private Integer add_desc;
    
	public Integer getFlag_topic() {
		return flag_topic;
	}
	public void setFlag_topic(Integer flag_topic) {
		this.flag_topic = flag_topic;
	}
	public Integer getAdd_desc() {
		return add_desc;
	}
	public void setAdd_desc(Integer add_desc) {
		this.add_desc = add_desc;
	}
	public Integer getMerge_board_id() {
		return merge_board_id;
	}
	public void setMerge_board_id(Integer merge_board_id) {
		this.merge_board_id = merge_board_id;
	}
	public Date getTaken_at_timestamp() {
		return taken_at_timestamp;
	}
	public void setTaken_at_timestamp(Date taken_at_timestamp) {
		this.taken_at_timestamp = taken_at_timestamp;
	}
	public Integer getFlag_first_scrape() {
		if (null==flag_first_scrape) {
			return 0;
		}
		return flag_first_scrape;
	}
	public void setFlag_first_scrape(Integer flag_first_scrape) {
		this.flag_first_scrape = flag_first_scrape;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDzb_id() {
		return dzb_id;
	}
	public void setDzb_id(Long dzb_id) {
		this.dzb_id = dzb_id;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getId_number() {
		return id_number;
	}
	public void setId_number(String id_number) {
		this.id_number = id_number;
	}
	public String getId_string() {
		return id_string;
	}
	public void setId_string(String id_string) {
		this.id_string = id_string;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getKey_words() {
		return key_words;
	}
	public void setKey_words(String key_words) {
		this.key_words = key_words;
	}
	public String getOriginal_name() {
		return original_name;
	}
	public void setOriginal_name(String original_name) {
		this.original_name = original_name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOriginal_des() {
		return original_des;
	}
	public void setOriginal_des(String original_des) {
		this.original_des = original_des;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public Integer getPins() {
		return pins;
	}
	public void setPins(Integer pins) {
		this.pins = pins;
	}
	public Integer getFollowers() {
		return followers;
	}
	public void setFollowers(Integer followers) {
		this.followers = followers;
	}
	public Integer getCategory_id() {
		return category_id;
	}
	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getExcellent() {
		return excellent;
	}
	public void setExcellent(Integer excellent) {
		this.excellent = excellent;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public Integer getCollaborators() {
		return collaborators;
	}
	public void setCollaborators(Integer collaborators) {
		this.collaborators = collaborators;
	}
	public String getPrivacy() {
		return privacy;
	}
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	public Integer getImage_width() {
		return image_width;
	}
	public void setImage_width(Integer image_width) {
		this.image_width = image_width;
	}
	public Integer getImage_height() {
		return image_height;
	}
	public void setImage_height(Integer image_height) {
		this.image_height = image_height;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public String getIndustry_words() {
		return industry_words;
	}
	public void setIndustry_words(String industry_words) {
		this.industry_words = industry_words;
	}
	public String getNegative_words() {
		return negative_words;
	}
	public void setNegative_words(String negative_words) {
		this.negative_words = negative_words;
	}
	public Integer getSubmit() {
		return submit;
	}
	public void setSubmit(Integer submit) {
		this.submit = submit;
	}
  

   
}