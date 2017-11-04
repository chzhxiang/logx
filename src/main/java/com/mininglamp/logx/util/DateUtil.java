/**
 * Copyright (c) 2016-2026 Mininglamp Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mininglamp.logx.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.mininglamp.logx.kit.StrKit;

public class DateUtil {
	/**
	 * @param time
	 * @param inputFormatt 输入格式
	 * @param outputFormat 输出格式
	 * @return
	 * @throws ParseException
	 */
	public static String getDateStrFormat(String time, String inputFormat, String outputFormat) throws ParseException{
		if(StrKit.isBlank(time)){
			throw new RuntimeException("时间不能为null");
		}
		if(StrKit.isBlank(inputFormat) || StrKit.isBlank(outputFormat)){
			throw new RuntimeException("时间格式不能为null");
		}
		SimpleDateFormat sdf = new SimpleDateFormat(inputFormat);
		Date d = sdf.parse(time);
		sdf = new SimpleDateFormat(outputFormat);
		return sdf.format(d);
	}
	
	/**
	 * @param time
	 * @param inputFormatt 输入格式
	 * @return
	 * @throws ParseException
	 */
	public static Date getDateStrFormat(String time, String inputFormat) throws ParseException{
		if(StrKit.isBlank(time)){
			throw new RuntimeException("时间不能为null");
		}
		if(StrKit.isBlank(inputFormat)){
			throw new RuntimeException("时间格式不能为null");
		}
		SimpleDateFormat sdf = new SimpleDateFormat(inputFormat);
		return sdf.parse(time);
	}
	
	/**
	 * 日期转字符串
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String dateToString(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		}
		return "";
	}
	/**
	 * 字符串转日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date stringToDate(String date, String pattern) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				return null;
			}
		}
		return null;
	}
	/**
	 * 验证日期是否有效
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static boolean validateDate(String date, String pattern) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			try {
				Date newDate=sdf.parse(date);
				return date.equals(sdf.format(newDate));
			} catch (ParseException e) {
				return false;
			}
		}
		return false;
	}
	/**
	 * 日期转字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		return dateToString(date, "yyyy-MM-dd hh:mm:ss");
	}
	public static String nowToString(String pattern){
		return dateToString(new Date(),pattern);
	}
	public static String nowToString(){
		return nowToString("yyyy-MM-dd hh:mm:ss");
	}
	/**
	 * 获取昨天日期字符串
	 * @return
	 */
	public static String getYesterday(){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return DateUtil.dateToString(cal.getTime(),"yyyyMMdd");
	}
	
	public static void main(String[] args) throws ParseException {
//		try{
//			Process proc = Runtime.getRuntime().exec("cmd /c dir c:\\Users/sjfx012/Desktop \tc");
//			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//			String data = "";
//			
//			for(int i=0; i<6; i++){
//				data = br.readLine();
//			}
//			System.out.println(data);
//			StringTokenizer st = new StringTokenizer(data);
//			String date = st.nextToken();
//			String time = st.nextToken();
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}
	
	
}
