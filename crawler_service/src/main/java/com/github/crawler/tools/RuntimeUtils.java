package com.github.crawler.tools;

/**
 * 计算程序执行时间
 * @author huangkai
 *
 */
public class RuntimeUtils {

	private long begin;
	private long end;

	public RuntimeUtils() {
		this.begin = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		end = System.currentTimeMillis();
		long ms = end - begin;
		return "Runtime [ ms=" + ms + "]";
	}
	
	public String toString2() {
		end = System.currentTimeMillis();
		long ms = end - begin;
		return "Runtime [ ms=" + ms + ", begin=" + begin + ", end=" + end + "]";
	}

}
