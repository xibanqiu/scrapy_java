package com.github.crawler.mapper.provider.bean;

public class OrderBy {

	private String column;
	private String sort;

	/**
	 * 是否封面图
	 * 
	 * @author huangkai
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

	public OrderBy() {
		super();
	}

	public OrderBy(String column, SortE st) {
		super();
		this.column = column;
		this.sort = st.name();
	}
	
	public OrderBy(String column, String sort) {
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

}
