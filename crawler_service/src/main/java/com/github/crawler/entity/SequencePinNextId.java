package com.github.crawler.entity;

/**
 * 全局唯一ID生成表
 * @author huangkai
 *
 */
public class SequencePinNextId {

	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "SequenceNextId [id=" + id + "]";
	}

}
