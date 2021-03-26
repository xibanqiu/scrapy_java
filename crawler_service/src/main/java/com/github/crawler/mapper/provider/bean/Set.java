package com.github.crawler.mapper.provider.bean;

public class Set {

	private String field;
	private Object value;

	public Set(String field, Object value) {
		super();
		this.field = field;
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "FilterRule [field=" + field + ", value=" + value + "]";
	}

}
