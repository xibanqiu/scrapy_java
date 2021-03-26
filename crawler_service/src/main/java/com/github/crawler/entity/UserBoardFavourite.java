package com.github.crawler.entity;


import com.github.crawler.mapper.provider.bean.TableName;

@TableName("user_board_favourite")
public class UserBoardFavourite extends BaseEntity {

	private Long uid;
	private String title;
	private String info;
	private int img_count;
	
	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getUid() {
		return uid;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getInfo() {
		return info;
	}

	public void setImg_count(int img_count) {
		this.img_count = img_count;
	}

	public int getImg_count() {
		return img_count;
	}

}
