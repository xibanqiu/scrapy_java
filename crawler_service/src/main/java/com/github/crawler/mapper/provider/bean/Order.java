package com.github.crawler.mapper.provider.bean;

public class Order {

	private String column;
	private String sort;

	/**
	 * 是否封面图
	 * 
	 * @author huangkai
	 *
	 */
	public enum SortE {
		/**
		 * 倒排序
		 */
		desc,
		/**
		 * 正排序
		 */
		asc;
	}

	public Order() {
		super();
	}

	public Order(String column, SortE sortE) {
		super();
		this.column = column;
		this.sort = sortE.name();
	}
	
	public Order(String column, String sort) {
		super();
		this.column = column;
		this.sort = sort;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public static Order asc(String column) {
		return new Order(column, SortE.asc);
	}

	public static Order desc(String column) {
		return new Order(column, SortE.desc);
	}
	
	public static Order ascById() {
		return new Order("id", SortE.asc);
	}

	public static Order descById() {
		return new Order("id", SortE.desc);
	}

}
