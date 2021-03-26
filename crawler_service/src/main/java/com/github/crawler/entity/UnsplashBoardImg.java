package com.github.crawler.entity;


import com.github.crawler.mapper.provider.bean.TableName;

import java.util.Date;

@TableName("unsplash_board_img")
public class UnsplashBoardImg extends BaseEntity {

	private String unsplash_user_id;
	private Long user_id;
	private Long board_id;
	private Long group_id;
	private String original_link;
	private Integer img_width;
	private Integer img_height;
	private String img_max_url;

	private String img_url;
	private String unsplash_image_id;
	private String description;
	private String title;
	private String instagram_username;
	private String cr_author;
	private String cr_author_url;

	private Integer views;
	private Integer downloads;
	private Integer likes;

	private String tag;
	private String oss_url;
	private Integer is_oss_url_orginal;
	private Integer flag_sync_jhk_img;
	private Integer flag_download_img;
	private Integer flag_sync_user_img_favourite;
	
	private Date img_create_at;
	private Date img_update_at;

	public Date getImg_create_at() {
		return img_create_at;
	}

	public void setImg_create_at(Date img_create_at) {
		this.img_create_at = img_create_at;
	}

	public Date getImg_update_at() {
		return img_update_at;
	}

	public void setImg_update_at(Date img_update_at) {
		this.img_update_at = img_update_at;
	}

	public String getImg_max_url() {
		return img_max_url;
	}

	public void setImg_max_url(String img_max_url) {
		this.img_max_url = img_max_url;
	}
	
	public String getUnsplash_user_id() {
		return unsplash_user_id;
	}

	public void setUnsplash_user_id(String unsplash_user_id) {
		this.unsplash_user_id = unsplash_user_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getBoard_id() {
		return board_id;
	}

	public void setBoard_id(Long board_id) {
		this.board_id = board_id;
	}

	public Long getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Long group_id) {
		this.group_id = group_id;
	}

	public String getOriginal_link() {
		return original_link;
	}

	public void setOriginal_link(String original_link) {
		this.original_link = original_link;
	}

	public Integer getImg_width() {
		return img_width;
	}

	public void setImg_width(Integer img_width) {
		this.img_width = img_width;
	}

	public Integer getImg_height() {
		return img_height;
	}

	public void setImg_height(Integer img_height) {
		this.img_height = img_height;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public String getUnsplash_image_id() {
		return unsplash_image_id;
	}

	public void setUnsplash_image_id(String unsplash_image_id) {
		this.unsplash_image_id = unsplash_image_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInstagram_username() {
		return instagram_username;
	}

	public void setInstagram_username(String instagram_username) {
		this.instagram_username = instagram_username;
	}

	public String getCr_author() {
		return cr_author;
	}

	public void setCr_author(String cr_author) {
		this.cr_author = cr_author;
	}

	public String getCr_author_url() {
		return cr_author_url;
	}

	public void setCr_author_url(String cr_author_url) {
		this.cr_author_url = cr_author_url;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public Integer getDownloads() {
		return downloads;
	}

	public void setDownloads(Integer downloads) {
		this.downloads = downloads;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getOss_url() {
		return oss_url;
	}

	public void setOss_url(String oss_url) {
		this.oss_url = oss_url;
	}

	public Integer getIs_oss_url_orginal() {
		return is_oss_url_orginal;
	}

	public void setIs_oss_url_orginal(Integer is_oss_url_orginal) {
		this.is_oss_url_orginal = is_oss_url_orginal;
	}

	public Integer getFlag_sync_jhk_img() {
		return flag_sync_jhk_img;
	}

	public void setFlag_sync_jhk_img(Integer flag_sync_jhk_img) {
		this.flag_sync_jhk_img = flag_sync_jhk_img;
	}

	public Integer getFlag_download_img() {
		return flag_download_img;
	}

	public void setFlag_download_img(Integer flag_download_img) {
		this.flag_download_img = flag_download_img;
	}

	public Integer getFlag_sync_user_img_favourite() {
		return flag_sync_user_img_favourite;
	}

	public void setFlag_sync_user_img_favourite(Integer flag_sync_user_img_favourite) {
		this.flag_sync_user_img_favourite = flag_sync_user_img_favourite;
	}

}
