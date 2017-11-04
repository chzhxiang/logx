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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mininglamp.logx.kit.StrKit;
import com.mininglamp.logx.util.CloseUtil;
import com.mininglamp.logx.util.DateUtil;

public abstract class DailyRollingLogger extends LoggerIm{
	/**
	 * The total filepattern contains full path and date pattern.
	 */
	private String filePattern;
	/**
	 * basic log path
	 */
	private String path;
	/**
	 * The date pattern.
	 */
	private String datePattern;

	@Override
	protected void logLine(String line) {
		
	}

	public List<String> getLog(String startTime, String endTime) {
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
				filePathList.add(getFullyDatePath(sT));
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
		this.filePattern = getFilePattern();
		if(StrKit.isBlank(this.filePattern)){
			throw new RuntimeException("");
		}
		//filePattern = "${catalina.home}/log4j/psbcias/info.log_%d{yyyy-MM-dd}.log";
		String tmpPath = this.filePattern.substring(0, this.filePattern.indexOf(".log")+4);
		Pattern pattern=Pattern.compile("\\$\\{.+\\}");
		Matcher matcher = pattern.matcher(tmpPath);
		while(matcher.find()){
			String keyP = matcher.group();
			String key = keyP.substring(2, keyP.length()-1);
			String value = System.getProperty(key);
			if(value == null) throw new RuntimeException("The system property of '"+key+"' can not be found.");
			tmpPath = tmpPath.replace(keyP, value);
		}
		this.path = tmpPath;
		this.datePattern = this.filePattern.substring(this.filePattern.indexOf(".log")+4);
		if(StrKit.isBlank(this.datePattern)) throw new RuntimeException("The dailyrolling log mast have a date pattern.");
	}
	/**
	 * 获取日志完整路径
	 * @param date
	 * @return
	 */
	private String getFullyDatePath(Date date){
		Pattern pattern=Pattern.compile("%\\w\\{.*\\}");
		Matcher matcher = pattern.matcher(this.datePattern);
		String datePattern = null,fullPattern = null;
		while(matcher.find()){
			fullPattern = matcher.group();
			datePattern = fullPattern.substring(fullPattern.indexOf("{")+1,fullPattern.indexOf("}"));
		}
		
		String dateTime = DateUtil.dateToString(date, datePattern);
		String filePath = null;
		if(!dateTime.equals(DateUtil.nowToString(datePattern))){
			filePath = path + this.datePattern.replace(fullPattern, dateTime);
	    }else{
	    	filePath = path;
	    }
		return filePath;
	}
	public abstract String getFilePattern();
	
	protected abstract String handleLine(String line);
	
}
