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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mininglamp.logx.kit.StrKit;
/**
 * basic implement of Logger
 * @author czy
 *
 */
public abstract class LoggerIm implements Logger{
	public LoggerIm() {
		init();
	}
	
	public Map<String,Object> getUserDefined(HttpServletRequest request) {
		return null;
	}

	public final void log(String logStr) {
		String logLine = filterLogline(logStr);
		if(StrKit.notBlank(logLine)){
			logLine(logLine);
		}
	}
	
	protected abstract void logLine(String line);
	
	/**
	 * Filter \r\n
	 * @param logStr input log line string
	 * @return
	 */
	private String filterLogline(String logStr){
		if(logStr == null) return null;
		return logStr.replaceAll("(\r\n|\r|\n|\n\r)", " ");
	}
	/**
	 * Get slf4j logger.
	 * @return
	 */
	public org.slf4j.Logger getSlf4jLogger(){
		Class<?> clz=null;
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		for(StackTraceElement ste : stack){
			if(ste.getClassName().indexOf("LoggerIm")>-1){
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
	/**
	 * Get log4j logger
	 * @return
	 */
	public org.apache.log4j.Logger getLog4jLogger(){
		Class<?> clz=null;
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		for(StackTraceElement ste : stack){
			if(ste.getClassName().indexOf("LoggerIm")>-1){
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
		if(clz == null) clz = Log4jLogger.class;
		org.apache.log4j.Logger currentLog=org.apache.log4j.LogManager.getLogger(clz);
		return currentLog;
	}
}
