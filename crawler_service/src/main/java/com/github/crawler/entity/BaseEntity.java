package com.github.crawler.entity;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class BaseEntity {

	protected Long id;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	protected Date create_at;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	protected Date update_at;

	public BaseEntity() {
		super();
		Date date = new Date();
		this.create_at = date;
		this.update_at = date;
	}

	public void setUpdate_at(Date update_at) {
		this.update_at = update_at;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreate_at() {
		return create_at;
	}

	public void setCreate_at(Date create_at) {
		this.create_at = create_at;
	}

	public Date getUpdate_at() {
		return update_at;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}