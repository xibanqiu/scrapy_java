package com.github.crawler.mapper.provider.bean;

import java.util.ArrayList;
import java.util.List;

public class WhereOrderLimit {

	private List<Where> wheres = new ArrayList<>();
	private Order order = null;
	private Limit limit = null;
	private String force_index = null;

	public List<Where> getWheres() {
		return wheres;
	}

	public void addWheres(List<Where> wheres) {
		this.wheres.addAll(wheres);
	}

	public void addWhere(Where where) {
		this.wheres.add(where);
	}

	public Order getOrder() {
		return order;
	}

	public void addOrder(Order order) {
		this.order = order;
	}

	public Limit getLimit() {
		return limit;
	}

	public void addLimit(Limit limit) {
		this.limit = limit;
	}

	public String getForce_index() {
		return force_index;
	}

	public void setForce_index(String force_index) {
		this.force_index = force_index;
	}

}
