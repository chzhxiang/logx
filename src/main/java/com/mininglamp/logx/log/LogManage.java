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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.mininglamp.logx.config.Logx;
import com.mininglamp.logx.http.bean.HttpRequestWrapperPlain;
import com.mininglamp.logx.inter.LoginStatus;
import com.mininglamp.logx.kit.JsonKit;
import com.mininglamp.logx.kit.StrKit;
import com.mininglamp.logx.log.datafilter.ContentFilter;
import com.mininglamp.logx.log.version.Parser;
import com.mininglamp.logx.log.version.VersionConst;
import com.mininglamp.logx.util.DateUtil;
import com.mininglamp.logx.util.Utils;

/**
 * log manage utility
 * @author czy
 *
 */
public class LogManage{
	/**
	 * Main logx config info object.
	 */
	private static Logx logx = Logx.instance();
	/**
	 * The logger to read and write the log info.
	 */
	private static Logger logger;
	/**
	 * The log content build and parse.
	 */
	private static Parser parser;
	/**
	 * Content filter.
	 */
	private static ContentFilter contentFilter = ContentFilter.instance();
	
	private LogManage() {
	}
	
	static{
		/**
		 * load logger
		 */
		String loggerClass = logx.getProperty("log.manage.loggerClass");
		if(StrKit.notBlank(loggerClass)){
			try {
				logger = (Logger) Class.forName(loggerClass).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		/**
		 * load logversion
		 */
		String logVersionClass = logx.getProperty("log.version.logVersionClass");
		if(StrKit.notBlank(logVersionClass)){
			try {
				parser = (Parser) Class.forName(logVersionClass).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void log(HttpServletRequest request){
		log(request, null);
	}
	/**
	 * Start action log.
	 * @param request
	 */
	public static void logStart(HttpServletRequest request){
		log(request, null);
		Utils.addActionInfo2ApplicationContext(request);
	}
	/**
	 * End action log.
	 * @param request
	 */
	public static void logEnd(HttpServletRequest request){
		log(request, null);
		Utils.removeActionInfoFromApplicationContext(request);
	}
	/**
	 * 用户自行调用<br/>
	 * 参数顺序为：key,value,key,value ...<br/>
	 * 其中key需为String
	 * @param request
	 * @param objs
	 */
	public static void logInfo(HttpServletRequest request, Object ...objs){
		if(objs == null||objs.length == 0){
			log(request);
		}else{
			if(objs.length%2 != 0) throw new IllegalArgumentException("日志参数需为偶数个");
			Map<String,Object> resultMap = new HashMap<String,Object>();
			String key = null;
			for (int i=0;i<objs.length;i++) {
				Object obj = objs[i];
				if(i%2 == 0){
					if(obj instanceof String){
						key = (String) obj;
					}else{
						throw new IllegalArgumentException("日志参数Key必须为String");
					}
				}else{
					resultMap.put(key, obj);
				}
			}
			log(request, resultMap);
		}
	}
	/**
	 * 非页面请求状态下记录日志
	 * @param objs
	 */
	public static void logInfo(Object ...objs){
		HttpRequestWrapperPlain request = new HttpRequestWrapperPlain();
		logInfo(request, objs);
	}
	/**
	 * 自定义记录日志，第二个参数为自定义内容
	 * @param request 请求
	 * @param resultMap 自定义内容
	 */
	public static void log(HttpServletRequest request, Map<String,Object> resultMap){
		if(parser == null||logger == null) return;
		//系统字段
		StringBuffer logLine = parser.logStr(request);

		//存放返回结果resultMap和用户自定义内容userDefinedInfoAll
		Map<String,Object> userDefinedInfoAll = new TreeMap<String,Object>();
		//获取自定义内容
		Map<String,Object> userDefinedInfoMap = logger.getUserDefined(request);
		if(userDefinedInfoMap != null){
			userDefinedInfoAll.putAll(userDefinedInfoMap);
		}
		if(resultMap != null){
			userDefinedInfoAll.putAll(resultMap);
		}
		String userDefinedInfo = "";
		if(userDefinedInfoAll != null){
			contentFilter.handleUserMap(userDefinedInfoAll);
			userDefinedInfo = JsonKit.toJson(userDefinedInfoAll);
		}
		
		String str = logLine.append(contentFilter.filterStr(userDefinedInfo)).toString();
		logger.log(str);
	}
	/**
	 * 获取日志信息
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static List<Map<String,Object>> getLog(String startDateTime,String endDateTime){
		if(startDateTime == null||endDateTime == null) return null;
		
		List<String> logList = logger.getLog(startDateTime, endDateTime);
		List<Map<String,Object>> logMapList = new ArrayList<Map<String,Object>>();
		for (String logLine : logList) {
			//读取版本号解析内容
			Map<String,Object> parseMap = parser.parseLogstr(logLine);
			if(parseMap!=null) logMapList.add(parseMap);
		}
		return logMapList;
	}
	/**
	 * 获取汇总后的日志信息
	 * @param startDateTime
	 * @param endDateTime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String,Object>> getLogSummary(String startDateTime,String endDateTime){
		if(startDateTime == null||endDateTime == null) return null;
		
		List<String> logList = logger.getLog(startDateTime, endDateTime);
		List<Map<String,Object>> logMapList = new ArrayList<Map<String,Object>>();
		/**
		 * actionid relate
		 */
		Map<String,Map<String,Object>> idMaps = new HashMap<String,Map<String,Object>>();
		for (String logLine : logList) {
			//读取版本号解析内容
			Map<String,Object> parseMap = parser.parseLogstr(logLine);
			if(parseMap!=null){
				String actionTime = (String) parseMap.get(VersionConst.LOG_ACTION_TIME),
					actionId = (String) parseMap.get(VersionConst.LOG_ACTION_ID);
				Map<String,Object> idMap = idMaps.get(actionId);
				if(idMap == null){
					idMap = new HashMap<String,Object>();
					String startTimeF = DateUtil.dateToString(DateUtil.stringToDate(actionTime, "yyyyMMddHHmmssSSS"),"yyyy-MM-dd HH:mm:ss.SSS");
					idMap.put("logStartTime", actionTime);
					idMap.put("logStartTimeF", startTimeF);
					idMap.put("executeTime", "-1");
					idMap.putAll(parseMap);
					List<Map> items = new ArrayList<Map>();
					items.add(parseMap);
					idMap.put("items", items);
					idMap.put("itemCount", items.size());
					logMapList.add(idMap);
				}else{
					List<Map> items = (List<Map>) idMap.get("items");
					items.add(parseMap);
					
					String startTime = (String) idMap.get("logStartTime");
					long executeTime = DateUtil.stringToDate(actionTime, "yyyyMMddHHmmssSSS").getTime()-DateUtil.stringToDate(startTime, "yyyyMMddHHmmssSSS").getTime();
					idMap.put("executeTime", executeTime);
					idMap.put("itemCount", items.size());
					//check session ifnot null
					String sessionId = (String) idMap.get("sessionid");
					if(sessionId == null||"null".equals(sessionId)){
						idMap.put("sessionid", parseMap.get("sessionid"));
					}
				}
				
				idMaps.put(actionId, idMap);
				
			}
		}
		return logMapList;
	}
	/**
	 * 根据session属性获取是否登陆
	 * @param sessionAttrs
	 * @return
	 */
	public static boolean isLoginSession(Map<String,Object> sessionAttrs){
		if(logger instanceof LoginStatus){
			LoginStatus loginStatus = (LoginStatus) logger;
			return loginStatus.isLogin(sessionAttrs);
		}
		return false;
	}
}