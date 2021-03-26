package com.github.crawler.mapper.provider.bean;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class Sets {

	private List<Set> sets = new ArrayList<>();

	public void add(Set set) {
		sets.add(set);
	}

	public void add(List<Set> sets) {
		sets.addAll(sets);
	}

	public List<Set> getSets() {
		return sets;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
