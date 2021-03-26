package com.github.crawler.mapper.provider.bean;

public class Limit {

	private Integer rows;
	private Integer offset;

	public Limit() {
		super();
	}

	public Limit(Integer rows, Integer offset) {
		super();
		this.rows = rows;
		this.offset = offset;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	
	public static Limit toLimit(int rows,int page){
		return new Limit(rows,(page-1)*rows);
	}

}
