package com.keichee.utils;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;

/**
 * 날짜 관련 유틸
 * @author Kihyun
 */
public class DateUtils {

	public static final DateUtils instance =  new DateUtils();
	private final DateTimeFormatter formatter_19 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	private final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 기본 날짜를 format에 맞게 반환
	 * @param format
	 * @return
	 */
	public String getDefaultDttm(String format) {
		SimpleDateFormat sdf = (format != null ? new SimpleDateFormat(format) : new SimpleDateFormat(DEFAULT_FORMAT));
		return sdf.format(new Date(0));
	}
	/**
	 * 현재 날짜를 format에 맞게 반환
	 * @param format
	 * @return
	 */
	public String getCurrentDttm(String format) {
		// any kind of format like ....yyyy-MM-dd HH:mm:ss 
		SimpleDateFormat sdf = (format != null ? new SimpleDateFormat(format) : new SimpleDateFormat(DEFAULT_FORMAT));
		return sdf.format(new Date());
	}
	
	/**
	 * 기준 일시로 부터 몇 일이 지났는지 여부
	 * @param dttm
	 * @param days
	 * @return 1 : dttm이 days(일)이상 경과했을 경우,
	 * 		   0 : dttm이 정확히 days(일) 경과한 경우,
	 * 		  -1 : dttm이 아직 days(일)을 경과하지 않은 경우
	 */
	public int daysPassed(String dttm, int days) {
		DateTime dt = DateTime.parse(dttm, formatter_19);
		DateTime dtc = DateTime.now();
		return dtc.compareTo(dt.plusDays(days));
	}
	
	/**
	 * 기준 일시로부터 몇 분이 지났는지 여부
	 * @param dttm
	 * @param minutes
	 * @return 1 : dttm이 minutes(분)이상 경과했을 경우,
	 * 		   0 : dttm이 정확히 minutes(분) 경과한 경우,
	 * 		  -1 : dttm이 아직 minutes(분)을 경과하지 않은 경우
	 */
	public int minutesPassed(String dttm, int minutes) {
		DateTime dt = DateTime.parse(dttm, formatter_19);
		DateTime dtc = DateTime.now();
		return dtc.compareTo(dt.plusMinutes(minutes));
	}
	
}
