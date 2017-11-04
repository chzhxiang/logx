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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mininglamp.logx.http.Const;

public class WebUtil {


	/**
	 * 设置HttpSession Attribute的简化方法.
	 * 
	 * @param keyStr
	 * @param object
	 */
	public static void setSessionAttribute(HttpSession session, String keyStr,
			Object object) {
		session.setAttribute(keyStr, object);
	}

	/**
	 * 获取客户端IP地址
	 * 
	 * @return String 客户端IP地址
	 */
	public static String getIpAddress(HttpServletRequest request) {
		return request.getRemoteAddr();
	}

	/**
	 * 获取客户端IP地址 使用反向代理，获取真实的客户端IP
	 * 
	 * @return String 客户端IP地址
	 */
	public static String getIpAdd(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		// add begin yKF53986 2012/01/12 R003C11L12n01
		// 在反向代理 X-Forwarded-For中获取客户端真实的IP地址
		if (null != ip && 0 < ip.length() && !"unknown".equalsIgnoreCase(ip)) {
			String[] ipArr = ip.split(",");
			for (int i = 0; i < ipArr.length; i++) {
				if (!"unknown".equalsIgnoreCase(ipArr[i])
						&& !"".equals(ipArr[i].trim())) {
					ip = ipArr[i];
					break;
				}
			}
		}
		// add begin yKF53986 2012/01/12 R003C11L12n01
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	@SuppressWarnings("unchecked")
	public static String getRequestParameters(HttpServletRequest request,String sep){
		if(request == null) return null;
		StringBuilder param= new StringBuilder();
		for (Enumeration<String> eh = request.getParameterNames(); eh.hasMoreElements();) {
			String parName = eh.nextElement();
			Object parValueObj = request.getParameter(parName);
			String parValue = "";
			if(parValueObj != null)	parValue = parValueObj.toString();
			
			if(parName!=null) parName = parName.replaceAll(sep, Const.LOG_PARAMETER_SHIFT_STR);
			if(parValue!=null) parValue = parValue.replaceAll(sep, Const.LOG_PARAMETER_SHIFT_STR);
			
			param.append(parName+"="+parValue+sep);
		}
		if(param.length()>0) param = param.deleteCharAt(param.length()-1);
		
		return param.toString();
	}
	/**
	 * 获取请求参数集合
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getRequestParametersMap(HttpServletRequest request){
		if(request == null) return null;
		
		Map<String, String> paramMap = new HashMap<String, String>();
		for (Enumeration<String> eh = request.getParameterNames(); eh.hasMoreElements();) {
			String parName = eh.nextElement();
			Object parValueObj = request.getParameter(parName);
			String parValue = "";
			if(parValueObj != null)	parValue = parValueObj.toString();
			
			paramMap.put(parName, parValue);
		}
		
		return paramMap;
	}
}
