package com.github.crawler.mapper.provider.bean;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class Wheres {

	private List<Where> wheres = new ArrayList<>();

	public void add(Where where) {
		wheres.add(where);
	}

	public void add(List<Where> ws) {
		wheres.addAll(ws);
	}

	public List<Where> getWheres() {
		return wheres;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
