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
package com.mininglamp.logx.http.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

import com.mininglamp.logx.http.listener.LogxListener;
import com.mininglamp.logx.kit.JsonKit;
import com.mininglamp.logx.log.bean.ModelAndView;
/**
 * Session相关
 * @author czy
 *
 */
public class SessionService {
	public String allSessionsInfo(){
		return wrapResult(LogxListener.getAllSessionsInfo());
	}
	public String getLoginSessionsInfo(){
		return wrapResult(LogxListener.getLoginSessionsInfo());
	}
	/**
	 * 获取之前一段时间内的Session情况
	 * @param num
	 * @param period
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getCachSessionsInfo(HttpServletRequest request){
		int num = Integer.valueOf(request.getParameter("num"));
		long period = Long.valueOf(request.getParameter("period"));
		List<Map> list = new ArrayList<Map>();
		long crrenttime = System.currentTimeMillis();
		if(num == 1){
			int allCount = LogxListener.getAllSessionsInfo().size();
			int loginCount = LogxListener.getLoginSessionsInfo().size();
			Map map = new HashMap();
			map.put("time", crrenttime);
			map.put("onLineNum", allCount);
			map.put("loginNum", loginCount);
			list.add(map);
		}else{
			List<Map> onLineList = getSpotByCach(LogxListener.cachOnLineSession, "onLine", crrenttime,num,period);
			List<Map> loginList = getSpotByCach(LogxListener.cachLoginSession, "login", crrenttime,num,period);
			for(int i=0;i<onLineList.size();i++){
				onLineList.get(i).put("loginNum", loginList.get(i).get("loginNum"));
			}
			list.addAll(onLineList);
		}
		return wrapResult(list);
	}
	
	/**
	 * 获取时间点sessionInfo
	 */
	@SuppressWarnings("unchecked")
	private List<Map> getSpotByCach(List<Map<String,Long>> cachList, String sessionType, long time,int num,long period){
		List<Map> list = new ArrayList<Map>();
		Stack<Map> stack = new Stack<Map>();
		synchronized (cachList) {
			for(int i=0; i<num; i++){
				int listCur=cachList.size()-1;
				boolean periodFlag = false;
				while(listCur>=0){
					if((time-period*i) > cachList.get(listCur).get("time")){
						Map<String,Long> cacheMap = new HashMap<String,Long>();
						cacheMap.put("time", time-period*i);
						cacheMap.put(sessionType+"Num", cachList.get(listCur).get("num"));
						stack.push(cacheMap);
						periodFlag = true;
						break;
					}
					listCur--;
				}
				//没有符合的，就取缓存中最大时间的点
				if(!periodFlag){
					Map<String,Long> map = new HashMap<String,Long>();
					map.put("time", time-period*i);
					map.put(sessionType+"Num", cachList.size()==0?0:cachList.get(cachList.size()-1).get("num"));
					stack.push(map);
				}
			}
		}
		
		for(Map map:stack){
			list.add(map);
		}
		
		return list;
	}
	
	/**
	 * 包装结果，封装为ModelAndView格式并返回字符串
	 * @param statusCode
	 * @param message
	 * @param object
	 * @return
	 */
	private String wrapResult(String statusCode,String message,Object object){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setStatusCode(statusCode);
		modelAndView.setMessage(message);
		modelAndView.setObject(object);
		return JsonKit.toJson(modelAndView);
	}
	/**
	 * 包装返回结果，statusCode=200
	 * @param object
	 * @return
	 */
	private String wrapResult(Object object){
		return wrapResult("200",null,object);
	}
}
