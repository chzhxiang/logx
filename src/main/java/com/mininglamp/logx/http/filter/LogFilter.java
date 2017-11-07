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
package com.mininglamp.logx.http.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mininglamp.logx.config.Logx;
import com.mininglamp.logx.http.Const;
import com.mininglamp.logx.http.action.ActionFactory;
import com.mininglamp.logx.http.interceptor.InvokeAction;
import com.mininglamp.logx.log.LogManage;
import com.mininglamp.logx.util.RequestActionManage;
import com.mininglamp.logx.util.Utils;
import com.mininglamp.logx.validate.URLValidator;

/**
 * filter all request
 * 
 *
 */
public class LogFilter implements Filter{
	private String encoding = "UTF-8";
	private Logx logx;
	private URLValidator urlValidator = new URLValidator();
	private InvokeAction invokeAction = InvokeAction.instance();
	/**
	 * prefix to view
	 */
	private String prefix = "/logx";
	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		request.setCharacterEncoding(encoding);
		
		String target = request.getRequestURI();
		boolean logging = false;
		if(isValid(target)){
			logging = true;
			//log before
			LogManage.logStart(request);
			//添加请求
			RequestActionManage.addURL4All(target);
		}
		
		try{
			/**
			 * logx请求则不向下操作
			 */
			
			if(urlValidator.validate(target, "/\\w+" + prefix + "/.+|/\\w+" + prefix + "[/]{0,1}$")){
				invokeAction.doInvoke(request, response);
			}else{
				/**
				 * SESSION放入属性 防止退出时session失效
				 */
				request.setAttribute(Const.CURRENT_SESSION, Utils.getSession(request));
				/**
				 * 处理跨域
				 */
				handleCORS(target,response);
				
				chain.doFilter(request, response);
			}
		}catch(Exception e){
			if(logging){
				Map<String,Object>  map = new HashMap<String,Object>();
				map.put("errMsg", e.getMessage());
				LogManage.log(request,map);
			}
		}
		
		if(logging){
			//log after
			LogManage.logEnd(request);
		}
	}
	/**
	 * 跨域处理
	 * @param target 资源路径
	 * @param response response
	 */
	private void handleCORS(String target,HttpServletResponse response){
		//Switch 
		String isCors = logx.getProperty("log.app.cors");
		if(isCors == null||!"true".equals(isCors)) return;
		
		//resources,such as "woff,ttf"
		String corsResources = logx.getProperty("log.app.cors.resource");
		if(corsResources == null||(corsResources=corsResources.trim().toLowerCase()).length() == 0) return;
		
		//split
		String[] resourceArray = corsResources.split(",");
		for (String resource : resourceArray) {
			if(target.endsWith("." + resource.trim()))
				response.setHeader("Access-Control-Allow-Origin", "*");
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		logx = Logx.instance();
		prefix = ActionFactory.getPrefix();
	}

	private boolean isValid(String url){
		boolean valid = true;
		
		String urlPattern = logx.getProperty("filter.url.pattern");
		String exclude = logx.getProperty("filter.url.exclude");
		String excludeList = logx.getProperty("filter.url.list");
		
		if(urlPattern !=null ){
			valid = false;
			urlPattern = urlPattern.replace("*", ".*\\");
			if(url.matches(urlPattern)) valid = true;
		}
		if(valid && exclude !=null ){
			String[] excludeArr = exclude.split(",");
			for (String exPattern : excludeArr) {
				exPattern = exPattern.trim();
				exPattern = exPattern.replace(".","\\.").replace("*", ".*");
				
				if(url.matches(exPattern)) valid = false;
				else{
					if(exPattern.endsWith("/.*")){
						exPattern = exPattern.substring(0, exPattern.length()-3);
						if(url.matches(exPattern)) valid = false;
					}
				}
			}
		}
		if(valid && excludeList != null){
			String[] excludeUrls = excludeList.split(",");
			for (String excludeUrl : excludeUrls) {
				excludeUrl = excludeUrl.trim();
				if(url.equals(excludeUrl)) valid = false;
			}
		}
		//单独处理 过滤logx自身请求
		if(valid && (url.matches("/\\w+/logx/.+") || url.matches("/\\w+/logx$"))) valid = false;
		return valid;
	}
	
}
