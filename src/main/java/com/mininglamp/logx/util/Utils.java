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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mininglamp.logx.config.Logx;
import com.mininglamp.logx.http.Const;
import com.mininglamp.logx.store.ApplicationContext;

/**
 * @author czy
 * @since Ver1.0
 */
public class Utils {
	private static Logx logx = Logx.instance();
	/**
	 * If auto create session when it's invalid.
	 */
	private static boolean autoCreateSession;
	
	static{
		autoCreateSession = !"false".equals(logx.getProperty("log.manage.autoCreateSession"));
	}
	
	public final static int DEFAULT_BUFFER_SIZE = 1024*4;
	
	public static String read(InputStream in){
		InputStreamReader reader;
		try{
			reader = new InputStreamReader(in,"UTF-8");
		}catch(UnsupportedEncodingException e){
			throw new IllegalStateException(e.getMessage(),e);
		}
		return read(reader);
	}
	public static String read(Reader reader){
		try{
			StringWriter writer = new StringWriter();
			
			char[] buffer = new char[DEFAULT_BUFFER_SIZE];
			int n=0;
			while(-1!=(n=reader.read(buffer))){
				writer.write(buffer,0,n);
			}
			return writer.toString();
		}catch(IOException e){
			throw new IllegalStateException("read error",e);
		}
	}
	public static String readFromResource(String resource) throws IOException{
		InputStream in = null;
		try{
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
			if(in == null){
				in = Utils.class.getResourceAsStream(resource);
			}
			if(in == null){
				return null;
			}
			String text = Utils.read(in);
			return text;
		}finally{
			CloseUtil.closeIO(in);
		}
	}
	public static boolean isExistResource(String resource) throws IOException{
		InputStream in = null;
		boolean existFlag = true;
		try{
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
			if(in == null){
				in = Utils.class.getResourceAsStream(resource);
			}
			if(in == null){
				existFlag = false;
			}else{
				existFlag = true;
			}
			return existFlag;
		}finally{
			CloseUtil.closeIO(in);
		}
	}
	public static byte[] readByteArrayFromResource(String resource) throws IOException{
		InputStream in = null;
		try{
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
			if(in == null){
				return null;
			}
			return readByteArray(in);
		}finally{
			CloseUtil.closeIO(in);
		}
	}
	public static byte[] readByteArray(InputStream input) throws IOException{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input,output);
		return output.toByteArray();
	}
	
	public static long copy(InputStream input,OutputStream output) throws IOException{
		final int EOF = -1;
		
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		
		long count = 0;
		int n = 0;
		while(EOF != (n = input.read(buffer))){
			output.write(buffer,0,n);
			count +=n;
		}
		return count;
	}
	public static long copy(String resource,OutputStream output) throws IOException{
		long count;
		InputStream in = null;
		try {
			in = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(resource);
			final int EOF = -1;
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			count = 0;
			int n = 0;
			while (EOF != (n = in.read(buffer))) {
				output.write(buffer, 0, n);
				count += n;
			}
			return count;
		} catch (Exception e) {
		} finally{
			CloseUtil.closeIO(in);
		}
		return 0;
	}
	
	public static String read(Reader reader,int length){
		try{
			char[] buffer = new char[length];
			
			int offset = 0;
			int rest = length;
			int len;
			while((len = reader.read(buffer,offset,rest))!=-1){
				rest -=len;
				offset +=len;
				
				if(rest==0){
					break;
				}
			}
			return new String(buffer,0,length-rest);
		}catch(IOException ex){
			throw new IllegalStateException("read error",ex);
		}
	}
	public static String toString(java.util.Date date){
		if(date == null){
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	public static String getStackTrace(Throwable ex){
		StringWriter buf = new StringWriter();
		ex.printStackTrace(new PrintWriter(buf));
		
		return buf.toString();
	}
	public static String toString(StackTraceElement[] stackTrace){
		StringBuilder buf = new StringBuilder();
		for(StackTraceElement item : stackTrace){
			buf.append(item.toString());
			buf.append("\n");
		}
		return buf.toString();
	}
	public static Class<?> loadClass(String className){
		Class<?> clazz = null;
		if(className == null){
			return null;
		}
		try{
			return Class.forName(className);
		}catch(ClassNotFoundException e){
		}
		ClassLoader ctxClassLoader = Thread.currentThread().getContextClassLoader();
		if(ctxClassLoader != null){
			try{
				clazz = ctxClassLoader.loadClass(className);
			}catch(ClassNotFoundException e){
			}
		}
		return clazz;
	}
	/**
	 * Get the actionid if not exist,will create and input into the request attribute.
	 * @param request
	 * @return
	 */
	public static String getActionId(HttpServletRequest request){
		String actionId = null;
		if(request!=null){
			Object obj = request.getAttribute("logx-actionid");
			if(obj !=null&&obj instanceof String) actionId = (String) obj;
			else{
				UUID uuid = UUID.randomUUID();
				actionId = uuid.toString().replace("-", "");
				request.setAttribute("logx-actionid", actionId);
			}
		}
		return actionId;
	}
	/**
	 * Add the running actionid and request time into servletcontext.
	 * @param servletContext
	 * @param request
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public static void addActionInfo2ServletContext(HttpServletRequest request) {
		ServletContext servletContext = request.getSession(true).getServletContext();
		Map<String,Map<String,Object>> map;
		Object obj = servletContext.getAttribute("logx-action-running");
		if(obj == null ||!(obj instanceof Map)){
			synchronized (servletContext) {
				map = new HashMap<String,Map<String,Object>>();
				servletContext.setAttribute("logx-action-running", map);
			}
		}else{
			map = (Map<String,Map<String,Object>>) obj;
		}
		Map<String,Object> actionInfo = new HashMap<String,Object>();
		
		String params = WebUtil.getRequestParameters(request,Character.toString(Const.LOG_PARAMETER_SEPARATOR));
		if(params == null){
			params = "获取参数失败";
		}else if(params.length()>200){
			params = "参数过多,请从日志详情页面查看";
		}
		String actionId = getActionId(request);
		String ip = WebUtil.getIpAdd(request);
		String uri = request.getRequestURI();
		String sessionid = getSessionId(request);
		
		actionInfo.put("actionId", actionId);
		actionInfo.put("actionTime", System.currentTimeMillis());
		actionInfo.put("params", params);
		actionInfo.put("ip", ip);
		actionInfo.put("uri", uri);
		actionInfo.put("sessionId", sessionid);
		
		map.put(actionId, actionInfo);
	}
	/**
	 * Add the running actionid and request time into servletcontext.
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public static void addActionInfo2ApplicationContext(HttpServletRequest request) {
		ApplicationContext app = getApplicationContext();
		
		Map<String,Map<String,Object>> map;
		Object obj = app.getAttribute("logx-action-running");
		if(obj == null ||!(obj instanceof Map)){
			synchronized (app) {
				map = new HashMap<String,Map<String,Object>>();
				app.setAttribute("logx-action-running", map);
			}
		}else{
			map = (Map<String,Map<String,Object>>) obj;
		}
		Map<String,Object> actionInfo = new HashMap<String,Object>();
		
		String params = WebUtil.getRequestParameters(request,Character.toString(Const.LOG_PARAMETER_SEPARATOR));
		if(params == null){
			params = "获取参数失败";
		}else if(params.length()>200){
			params = "参数过多,请从日志详情页面查看";
		}
		String actionId = getActionId(request);
		String ip = WebUtil.getIpAdd(request);
		String uri = request.getRequestURI();
		String sessionid = getSessionId(request);
		
		actionInfo.put("actionId", actionId);
		actionInfo.put("actionTime", System.currentTimeMillis());
		actionInfo.put("params", params);
		actionInfo.put("ip", ip);
		actionInfo.put("uri", uri);
		actionInfo.put("sessionId", sessionid);
		
		map.put(actionId, actionInfo);
	}
	/**
	 * Remove the running actionid from the servletcontext.
	 * @param servletContext
	 * @param request
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public static void removeActionInfoFromServletContext(HttpServletRequest request) {
		HttpSession session = null;
		try {
			session = request.getSession(true);
		} catch (Exception e) {
			session = (HttpSession) request.getAttribute(Const.CURRENT_SESSION);
		}
		ServletContext servletContext = session.getServletContext();
		Map<String,Long> map;
		Object obj = servletContext.getAttribute("logx-action-running");
		if(obj != null && obj instanceof Map){
			 map = (Map<String, Long>) obj;
			 map.remove(getActionId(request));
		}
	}
	/**
	 * Remove the running actionid from the applicationContext.
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public static void removeActionInfoFromApplicationContext(HttpServletRequest request) {
		ApplicationContext app = getApplicationContext();
		Map<String,Long> map;
		
		Object obj = app.getAttribute("logx-action-running");
		if(obj != null && obj instanceof Map){
			map = (Map<String, Long>) obj;
			map.remove(getActionId(request));
		}
	}
	/**
	 * Get all of the running action.
	 * @param request
	 * @return
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public static List<Map> getActionInfoInServletContext(HttpServletRequest request) {
		ServletContext servletContext = request.getSession(true).getServletContext();
		Map<String,Map<String,Object>> map;
		Object obj = servletContext.getAttribute("logx-action-running");
		if(obj != null && obj instanceof Map){
			map = (Map<String, Map<String,Object>>) obj;
			List<Map> list = new ArrayList<Map>();
			Long now = System.currentTimeMillis();
			Collection<Map<String,Object>> values = map.values();
			for (Map<String, Object> subMap : values) {
				Long actionTime = (Long) subMap.get("actionTime");
				Long period = now - actionTime;
				subMap.put("periodTime", period);
				list.add(subMap);
			}
			return list;
		}
		return null;
	}
	/**
	 * Get all of the running action.
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Map> getActionInfoInApplicationContext(HttpServletRequest request) {
		ApplicationContext app = getApplicationContext();
		Map<String,Map<String,Object>> map;
		Object obj = app.getAttribute("logx-action-running");
		if(obj != null && obj instanceof Map){
			map = (Map<String, Map<String,Object>>) obj;
			List<Map> list = new ArrayList<Map>();
			Long now = System.currentTimeMillis();
			Collection<Map<String,Object>> values = map.values();
			for (Map<String, Object> subMap : values) {
				Long actionTime = (Long) subMap.get("actionTime");
				Long period = now - actionTime;
				subMap.put("periodTime", period);
				list.add(subMap);
			}
			return list;
		}
		return null;
	}
	
	public static ApplicationContext getApplicationContext(){
		return ApplicationContext.instance();
	}
	/**
	 * Get session
	 * @param request
	 * @return
	 */
	public static HttpSession getSession(HttpServletRequest request){
		return request.getSession(autoCreateSession);
	}
	/**
	 * Get session id
	 * @param request
	 * @return
	 */
	public static String getSessionId(HttpServletRequest request){
		HttpSession session = getSession(request);
		return session == null?null:session.getId();
	}
}