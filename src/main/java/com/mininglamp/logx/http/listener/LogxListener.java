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
package com.mininglamp.logx.http.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.mininglamp.logx.log.LogManage;
import com.mininglamp.logx.util.Utils;
import com.mininglamp.logx.util.WebUtil;


/**
 * 监听session创建和销毁
 * @author Administrator
 *
 */
public class LogxListener implements HttpSessionListener,ServletContextListener,ServletRequestListener{
	public LogxListener() {
	}
	/**
	 * 保存所有有效session信息
	 */
	private static Set<HttpSession> sessions = new HashSet<HttpSession>(); 
	
	/**
	 * 缓存在线数量值
	 */
	public static List<Map<String,Long>> cachOnLineSession = new ArrayList<Map<String,Long>>();
	
	/**
	 * 缓存登陆数量值
	 */
	public static List<Map<String,Long>> cachLoginSession = new ArrayList<Map<String,Long>>();
	/**
	 * 创建session
	 */
	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		HttpSession session = httpSessionEvent.getSession();
		sessions.add(session);
		calculationCach(session,true);
	}
	
	/**
	 * 销毁session
	 */
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		HttpSession session = httpSessionEvent.getSession();
		sessions.remove(session);
		calculationCach(session,false);
	}
	
	/**
	 * 计算缓存数据
	 */
	public synchronized static void calculationCach(HttpSession session, boolean isAdd){
		long currenttime = System.currentTimeMillis();
		
		Map<String,Long> map = new HashMap<String,Long>();
		map.put("time", currenttime);
		map.put("num", Long.valueOf(sessions.size()+""));
		cachOnLineSession.add(map);
		Map<String,Long> ms = new HashMap<String,Long>();
		ms.put("time", currenttime);
		ms.put("num", (long)getLoginSessionsInfo().size());
		cachLoginSession.add(ms);
		
		removeOverTime(cachOnLineSession, currenttime);
		removeOverTime(cachLoginSession, currenttime);
	}
	
	/**
	 * @param list
	 * @return 去除超时的缓存数据
	 */
	public static List<Map<String,Long>> removeOverTime(List<Map<String,Long>> list, long currenttime){
		Iterator<Map<String, Long>> it = list.iterator();
		while(it.hasNext()){
			Map<String,Long> map = it.next();
			if((currenttime-map.get("time")) >= 24*60*60*1000){
				it.remove();
			}
		}
		return list;
	}
	
	/**
	 * 获取session
	 * @return
	 */
	public static  Set<HttpSession> getAllSessions (){
		return sessions;
	}
	/**
	 * 所有session属性
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String,Object>> getAllSessionsInfo(){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		for (HttpSession session : sessions) {
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				Enumeration enumeration = session.getAttributeNames();
				Map<String, Object> attrMap = new HashMap<String, Object>();
				while (enumeration.hasMoreElements()) {
					Object obj = enumeration.nextElement();
					if (obj instanceof String) {
						attrMap.put(obj.toString(), session.getAttribute(obj
								.toString()));
					}
				}
				map.put("attrs", attrMap);
				map.put("sessionId", session.getId());
				map.put("createTime", session.getCreationTime());
				map.put("lastAccessedTime", session.getLastAccessedTime());
				map.put("maxInactiveInterval", session.getMaxInactiveInterval());
				map.put("time", new Date().getTime());
				list.add(map);
			} catch (Exception e) {
				sessions.remove(session);
				throw new RuntimeException(e);
			}
		}
		return list;
	}
	/**
	 * 登陆session属性
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String,Object>> getLoginSessionsInfo(){
		List<Map<String,Object>> list=getAllSessionsInfo();
		List<Map<String,Object>> resultList=new ArrayList<Map<String,Object>>();
		for (Map map : list) {
			if(map.get("attrs")!=null&&LogManage.isLoginSession((Map<String, Object>) map.get("attrs"))) resultList.add(map);
		}
		return resultList;
	}
	/**
	 * 失效指定session
	 * @param idArr id数组
	 * @return
	 */
	public static boolean invalidateSessions (String[] idArr){
		try {
			for (HttpSession session : sessions) {
				if (Arrays.binarySearch(idArr, session.getId()) > -1) {
					session.invalidate();
					sessions.remove(session);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
	}
	public void contextInitialized(ServletContextEvent servletContextEvent) {
	}
	public void requestDestroyed(ServletRequestEvent requestEvent) {
	}
	public void requestInitialized(ServletRequestEvent requestEvent) {
		try{
			HttpServletRequest request = (HttpServletRequest) requestEvent.getServletRequest();
			HttpSession session = Utils.getSession(request);
			if(session != null&&session.getAttribute("ip") == null) {
				String ip = WebUtil.getIpAdd(request);
				session.setAttribute("ip", ip);
			}
		}catch(Exception e){
		}
	}
}
