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
package com.mininglamp.logx.http.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mininglamp.logx.config.Logx;
import com.mininglamp.logx.http.Const;
import com.mininglamp.logx.kit.StrKit;
import com.mininglamp.logx.util.Utils;
/**
 * Base action to handle request resource.
 * @author czy
 *
 */
public abstract class ResourceAction {
	/**
	 * Main logx config info object.
	 */
	private static Logx logx = Logx.instance();
	
	protected String resourcePath;
	protected String encoding = Const.DEFAULT_ENCODING;
	protected String prefix;
	
	public ResourceAction(String resourcePath) {
		this.resourcePath = resourcePath;
		try {
			init();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	protected String getFilePath(String fileName){
		return resourcePath + fileName;
	}
	public void init() throws ServletException{
		initAuthEnv();
	}
	private void initAuthEnv(){
		prefix = "/logx";
	}
	protected void returnResourceFile(String fileName,String uri,HttpServletResponse response) throws IOException{
		String filePath=getFilePath(fileName);
		if(filePath.endsWith(".html")){
			response.setContentType("text/html; charset=utf-8");
		}
		if(filePath.endsWith(".jpg")){
			byte[] bytes = Utils.readByteArrayFromResource(filePath);
			if(bytes!=null){
				response.getOutputStream().write(bytes);
			}
			
			return;
		}
		
		boolean isExist = Utils.isExistResource(filePath);
		if(!isExist){
			response.sendRedirect(uri + "/index.html");
			return;
		}
		if(fileName.endsWith(".css")){
			response.setContentType("text/css;charset=utf-8");
		}else if(fileName.endsWith(".js")){
			response.setContentType("text/javascript;charset=utf-8");
			
		}else if(fileName.endsWith(".woff")){
			response.setContentType("application/x-font-woff");
			
		}else if(fileName.endsWith(".ttf")){
			response.setContentType("application/x-font-ttf");
			
		}
		ServletOutputStream outputStream = response.getOutputStream();
		Utils.copy(filePath, outputStream);
		
	}
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextPath = request.getContextPath();
		String requestURI = request.getRequestURI();
		
		response.setCharacterEncoding(encoding);
		
		if(contextPath == null){
			contextPath = "";
		}
		String uri = contextPath + prefix;
		String path = requestURI.substring(uri.length());
		
		
		if("".equals(path)){
			if(contextPath.equals("") || contextPath.equals("/")){
				response.sendRedirect(prefix + "/index.html");
			}else{
				response.sendRedirect(contextPath + prefix + "/index.html");
			}
			return;
		}
		
		if("/".equals(path)){
			response.sendRedirect("index.html");
			return;
		}
		
		if(path.contains(".json")){
			String fullUrl = path;
			if(request.getQueryString() != null && request.getQueryString().length()>0){
				fullUrl +="?" + request.getQueryString();
			}
			if(fullUrl.startsWith("/")) fullUrl = fullUrl.substring(1);
			response.getWriter().print(process(request,fullUrl));
			return;
		}
		returnResourceFile(path,uri,response);
	}
	/**
	 * Get value in logx.properties by key.
	 * @param key key in logx.proeprties
	 * @return value
	 */
	protected String getProperty(String key){
		if(StrKit.isBlank(key)) return null;
		
		return logx.getProperty(key);
	}
	
	protected abstract String process(HttpServletRequest request, String url);
	/**
	 * FIX 处理时间过长
	 * @param response
	 * @param src
	 * @throws IOException
	 */
	@Deprecated
	protected void gzipOutput(HttpServletResponse response,byte[] src) throws IOException{
		ByteArrayOutputStream baout = new ByteArrayOutputStream();
		GZIPOutputStream gout = new GZIPOutputStream(baout);
		gout.write(src);
		gout.close();
		response.setHeader("Content-Encoding", "gzip");
		response.setContentLength(src.length);
		
		byte[] desc = baout.toByteArray();
		
		response.getOutputStream().write(desc);
	}
}
