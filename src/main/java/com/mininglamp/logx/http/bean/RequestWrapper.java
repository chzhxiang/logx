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
package com.mininglamp.logx.http.bean;

import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mininglamp.logx.util.HttpUtil;
import com.mininglamp.logx.util.Utils;
import com.mininglamp.logx.util.WebUtil;


public class RequestWrapper {
	private HttpServletRequest request;
	private HttpSession session;
	private String actionid;
	private String parameter;
	private String uri;
	private String ip;
	private String[] userAgent;
	
	private RequestWrapper() {
	}
	private RequestWrapper(HttpServletRequest request) {
		this.request = request;
		this.session = Utils.getSession(request);
		//操作编号
		handleActionid();
		//请求参数
		handleParameter();
		//获取IP
		handleAddr();
		//获取请求地址
		handleURI();
		//客户代理信息
		handleUserAgent();
	}
	/**
	 * 解析request对象，获取信息
	 * @param request
	 */
	public static RequestWrapper parse(HttpServletRequest request){
		if(request == null) return null;
		else return new RequestWrapper(request);
	}
	/**
	 * 处理操作ID
	 */
	private void handleActionid(){
		if(request.getAttribute("actionid")==null){
			UUID uuid=UUID.randomUUID();
			this.actionid = uuid.toString().replace("-", "");
			request.setAttribute("actionid", this.actionid);
		}else{
			this.actionid = (String)request.getAttribute("actionid");
		}
	}
	@SuppressWarnings("unchecked")
	private void handleParameter(){
		StringBuilder param = new StringBuilder("");
		for (Enumeration<String> eh = request.getParameterNames(); eh.hasMoreElements();) {
			String parName = eh.nextElement();
			Object parValue = request.getParameter(parName);
			param.append(parName+"="+parValue+"`");
		}
		String paramstr = "";
		if(param.length()>0){
			paramstr = param.substring(0, param.length()-1);
		}
		this.parameter = paramstr;
	}
	private void handleAddr(){
		this.ip = WebUtil.getIpAdd(request);
	}
	private void handleURI(){
		this.uri = request.getRequestURI();
	}
	private void handleUserAgent(){
		this.userAgent = HttpUtil.parseUserAgent(request.getHeader("user-agent"));
	}
	/**
	 * 获取session编号
	 * @return
	 */
	public String getSessionId(){
		if(session == null) return null;
		return session.getId();
	}
	
	/**
	 * 获取操作编号
	 * @return
	 */
	public String getActionid(){
		return this.actionid;
	}
	/**
	 * 获取请求参数
	 * @return
	 */
	public String getParameter() {
		return parameter;
	}
	/**
	 * 获取请求信息
	 * @return
	 */
	public String getUri() {
		return uri;
	}
	/**
	 * 获取客户IP
	 * @return
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * 获取客户代理信息
	 * @return
	 */
	public String[] getUserAgent(){
		return this.userAgent;
	}
}
