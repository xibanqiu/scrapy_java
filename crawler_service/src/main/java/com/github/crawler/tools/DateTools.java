package com.github.crawler.tools;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTools {
	protected final static Logger LOG = LoggerFactory.getLogger(DateTools.class);
	
	private static final String[] stringArray={"MMM dd, yyyy","MMM. dd, yyyy"};
	private static final Pattern pTime = Pattern.compile("[A-Z][a-z]{2}([a-z]+)(\\.)?");
	
	/**
	 * 几个小时前,几天前,几个月前 得到时间
	 * @param time
	 * @return
	 */
	public static Date getDateByBefore(String time) {

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        Date date = null;
        try {
        	  if(time.matches("\\d{1,2} hour[s]? ago")) {
              	
        		  c.add(Calendar.HOUR_OF_DAY, - Integer.parseInt(time.replaceAll("\\D", "")));
        		  date = c.getTime();
              }else if(time.matches("\\d{1,2} day[s]? ago")) {
              	
            	  c.add(Calendar.DATE, - Integer.parseInt(time.replaceAll("\\D", "")));
            	  date = c.getTime();
              }else if(time.matches("\\d{1,2} month[s]? ago")) {
              	
                  c.add(Calendar.MONTH, - Integer.parseInt(time.replaceAll("\\D", "")));
                  date = c.getTime();
              }else if(time.matches("\\d{1,2} year[s]? ago")) {
              	
                  c.add(Calendar.YEAR, - Integer.parseInt(time.replaceAll("\\D", "")));
                  date = c.getTime();
              }
        	  
		} catch (Exception e) {
			LOG.info("时间处理错误："+time);
		}
		return date;
	}
	
	/**
	 * Aug. 31, 2018  July 6, 2018 返回时间
	 * @param date
	 * @return
	 */
	public static Date getDate(String date) {

		try {
			Matcher mm = pTime.matcher(date);
			if(mm.find()) {
				date = date.substring(0,3)+date.substring(mm.end());
			}
			return DateUtils.parseDate(date,Locale.ENGLISH, stringArray);

		} catch (Exception e) {
			LOG.info("时间处理错误:"+date);
		}
		return null;
	}

	/**
	 * Natty是用Java编写的自然语言日期解析器
	 * @param stringDate
	 * @return
	 */
	public static Optional<Date> parseFirstDate(String stringDate){
		Optional<Date> optionalDate = Optional.empty();
		List<DateGroup> parse = new Parser().parse(stringDate);
		if(null != parse && parse.size() > 0){
			List<Date> dates = parse.get(0).getDates();
			if(null != dates && dates.size() > 0) optionalDate = Optional.of(dates.get(0));
		}

		return optionalDate;
	}


	/**
	 * Natty是用Java编写的自然语言日期解析器
	 * @param stringDate
	 * @return
	 */
	public static List<Date> parseDate(String stringDate){
		List<Date> list = new ArrayList<>();
		List<DateGroup> parse = new Parser().parse(stringDate);

		parse.forEach(dateGroup -> {
			list.addAll(dateGroup.getDates());
		});
		return list;
	}

	
}
