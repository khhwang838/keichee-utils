package com.keichee.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 날짜 관련 유틸
 * @author Kihyun
 */
public class DateUtils {

	public static final DateUtils instance =  new DateUtils();
	private final DateTimeFormatter formatter_19 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	private final DateTimeFormatter formatter_23 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
	private final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	
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
	
	/**
	 * 현재 시간을 UTC 시간으로 반환
	 * @return dttm23
	 */
	public String getCurrentDttmAsUTC() {
		DateTime dt = new DateTime(DateTimeZone.UTC);
		return dt.toString();
	}
	/**
	 * 로컬 시간을 UTC 시간으로 변경
	 * @param dttm
	 * @return UTC dttm23
	 * @throws ParseException
	 */
	public String getLocalToUTC(String dttm23) throws ParseException {
		DateTime dt = DateTime.parse(dttm23, formatter_23);
		dt = dt.toDateTime(DateTimeZone.UTC);
		return dt.toString();
	}
	/**
	 * UTC 시간을 로컬 시간으로 변경
	 * @param dttm23
	 * @return Local dttm23
	 * @throws ParseException
	 */
	public String getUtcToLocal(String dttm23) throws ParseException {
		dttm23 = dttm23.replace("T", " ").replace("Z", "");
		SimpleDateFormat df = new SimpleDateFormat(DEFAULT_FORMAT);
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date d = df.parse(dttm23);
		DateTime dt = new DateTime(d);
		dt = dt.toDateTime(DateTimeZone.getDefault());
		return dt.toString().replace("T", " ").substring(0, 23);
	}
	
	public static void main(String[] args) throws ParseException {
		DateUtils du = new DateUtils();
		
		String nowLocal = du.getCurrentDttm(du.DEFAULT_FORMAT);
		System.out.println("nowLocal: " + nowLocal);
		
		String nowUtc = du.getLocalToUTC(nowLocal);
		System.out.println("nowUTC: " + nowUtc);
		
		nowLocal = du.getUtcToLocal(nowUtc);
		System.out.println("nowLocal: " + nowLocal);
		
	}
}
