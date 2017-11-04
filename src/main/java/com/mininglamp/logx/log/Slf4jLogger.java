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
package com.mininglamp.logx.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.DailyRollingFileAppender;

import com.mininglamp.logx.kit.StrKit;
import com.mininglamp.logx.util.CloseUtil;
import com.mininglamp.logx.util.DateUtil;


/**
 * SLF4J
 * @author czy
 * @since 2016年4月6日19:25:12
 */
public abstract class Slf4jLogger extends LoggerIm{
    private String appenderName;
    
	@SuppressWarnings("unchecked")
	public List<String> getDailyRollingLog(String startTime,String endTime) {
		/**
		 * Get dailyrolling appender
		 */
		Enumeration<org.apache.log4j.Appender> appenders = org.apache.log4j.Logger.getRootLogger().getAllAppenders();
		if(appenders == null){
			//No appender found
			throw new RuntimeException("Log4j的配置信息缺失，未找到Appendar");
		}
		/**
		 * Get appender by appenderName,default fileappender name is 'file'.
		 */
		DailyRollingFileAppender appender = (DailyRollingFileAppender)org.apache.log4j.Logger.getRootLogger().getAppender(appenderName);
		if(appender == null){
			while(appenders.hasMoreElements()){
				org.apache.log4j.Appender appEle = appenders.nextElement();
				if(appEle instanceof DailyRollingFileAppender) {
					appender = (DailyRollingFileAppender) appEle;
					break;
				}
			}
			if(appender == null){
				throw new RuntimeException("Log4j的配置信息缺失，未找到Appendar");
			}
		}
 	    String path=appender.getFile();
 	    File direc = new File(path);
 	    String fileName = direc.getName();
 	    if(direc.isFile()){
 	    	direc = direc.getParentFile();
 	    }
 	    for(String filePath: direc.list()){
 	    	if(StrKit.notBlank(filePath) && StrKit.notBlank(fileName) && filePath.contains(fileName)){
 	    		File file = new File(filePath);
 	    		@SuppressWarnings("unused")
				long lastModified = file.lastModified();
 	    	}
 	    }
	    //创建文件
		List<String> filePathList = new ArrayList<String>();
 	    try {
			Calendar sTC = Calendar.getInstance();
			Calendar eTC = Calendar.getInstance();
			sTC.setTime(DateUtil.getDateStrFormat(startTime, "yyyy-MM-dd"));
			eTC.setTime(DateUtil.getDateStrFormat(endTime, "yyyy-MM-dd"));
			while(sTC.getTimeInMillis()<=eTC.getTimeInMillis()){
				Date sT = sTC.getTime();
				String sTime = DateUtil.dateToString(sT, "yyyy-MM-dd");
				if(!sTime.equals(DateUtil.nowToString("yyyy-MM-dd"))){
				    String filePath = path + "_" + sTime + "." + "log";
				    filePathList.add(filePath);
			    }else{
			    	filePathList.add(path);
			    }
				sTC.add(Calendar.DATE, 1);
			}
			
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		//循环读取log文件
		List<String> logList = new ArrayList<String>();
		for(String filePath: filePathList){
			File file = new File(filePath);
	        if(!file.exists()){
	        	//文件不存在
	     	   continue;
	        }
	        InputStreamReader isr = null;
	        FileInputStream fis = null;
	        BufferedReader reader = null;
			try {
				fis = new FileInputStream(file);
				isr = new InputStreamReader(fis, "UTF-8");
		        reader = new BufferedReader(isr);
		        String tempString = "";
		        while((tempString=reader.readLine())!=null){
		        	tempString = handleLine(tempString);
		        	if(tempString != null) logList.add(tempString);
		        }
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				CloseUtil.closeIO(reader,isr,fis);
			}
		}
        
		return logList;
	}
	
	public void init() {
		this.appenderName = getAppenderName();
		System.out.println("Log4jLogger init."+appenderName);
	}

	@Override
	public Map<String,Object> getUserDefined(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		
		return map;
	}

	/**
	 * 通用获取当前方法调用类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public org.slf4j.Logger getLogger(){
		Class clz=null;
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		for(StackTraceElement ste : stack){
			if(ste.getClassName().indexOf("Slf4jLogger")>-1){
				clz = ste.getClass();
				continue;
			}
			if(clz!=null && ste.getClassName().indexOf("mininglamp")>-1){
				try {
					clz = Class.forName(ste.getClassName());
					break;
				} catch (ClassNotFoundException e) {
				}
			}
		}
		if(clz == null) clz = Slf4jLogger.class;
		org.slf4j.Logger currentLog=org.slf4j.LoggerFactory.getLogger(clz);
		return currentLog;
	}

	public abstract String getAppenderName();
	
	protected abstract String handleLine(String line);
}
