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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/**
 * Plain request object with none effect.
 * @author czy
 * @since 2016年6月1日16:17:36
 */
@SuppressWarnings("unchecked")
public class HttpRequestWrapperPlain implements HttpServletRequest {
	private Map<String,Object> attrs = new HashMap<String,Object>();
	private Hashtable<String,String> headers = new Hashtable<String,String>();
	private Hashtable<String,String[]> parameters = new Hashtable<String,String[]>();
	private HttpSession httpSession = new HttpSessionWrapperPlain();

	public String getAuthType() {
		return null;
	}

	public String getContextPath() {
		return null;
	}

	public Cookie[] getCookies() {
		return null;
	}

	public long getDateHeader(String arg0) {
		return 0;
	}

	public String getHeader(String arg0) {
		return headers.get(arg0);
	}

	public Enumeration getHeaderNames() {
		return headers.keys();
	}

	public Enumeration getHeaders(String arg0) {
		return headers.elements();
	}

	public int getIntHeader(String arg0) {
		return 0;
	}

	public String getMethod() {
		return null;
	}

	public String getPathInfo() {
		return null;
	}

	public String getPathTranslated() {
		
		return null;
	}

	public String getQueryString() {
		
		return null;
	}

	public String getRemoteUser() {
		
		return null;
	}

	public String getRequestURI() {
		
		return "";
	}

	public StringBuffer getRequestURL() {
		
		return null;
	}

	public String getRequestedSessionId() {
		
		return null;
	}

	public String getServletPath() {
		return null;
	}

	public HttpSession getSession() {
		return httpSession;
	}

	public HttpSession getSession(boolean arg0) {
		return httpSession;
	}

	public Principal getUserPrincipal() {
		return null;
	}

	public boolean isRequestedSessionIdFromCookie() {
		return false;
	}

	public boolean isRequestedSessionIdFromURL() {
		return false;
	}

	public boolean isRequestedSessionIdFromUrl() {
		
		return false;
	}

	public boolean isRequestedSessionIdValid() {
		
		return false;
	}

	public boolean isUserInRole(String arg0) {
		
		return false;
	}

	public Object getAttribute(String arg0) {
		return attrs.get(arg0);
	}

	public Enumeration getAttributeNames() {
		
		return null;
	}

	public String getCharacterEncoding() {
		
		return null;
	}

	public int getContentLength() {
		
		return 0;
	}

	public String getContentType() {
		
		return null;
	}

	public ServletInputStream getInputStream() throws IOException {
		
		return null;
	}

	public String getLocalAddr() {
		
		return null;
	}

	public String getLocalName() {
		
		return null;
	}

	public int getLocalPort() {
		
		return 0;
	}

	public Locale getLocale() {
		
		return null;
	}

	public Enumeration getLocales() {
		
		return null;
	}

	public String getParameter(String arg0) {
		
		return null;
	}

	public Map getParameterMap() {
		return parameters;
	}

	public Enumeration getParameterNames() {
		return parameters.keys();
	}

	public String[] getParameterValues(String arg0) {
		return parameters.get(arg0);
	}

	public String getProtocol() {
		return "HTTP";
	}

	public BufferedReader getReader() throws IOException {
		return null;
	}

	public String getRealPath(String arg0) {
		return null;
	}

	public String getRemoteAddr() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return "127.0.0.1";
		}
	}

	public String getRemoteHost() {
		
		return null;
	}

	public int getRemotePort() {
		
		return 0;
	}

	public RequestDispatcher getRequestDispatcher(String arg0) {
		
		return null;
	}

	public String getScheme() {
		
		return null;
	}

	public String getServerName() {
		
		return null;
	}

	public int getServerPort() {
		
		return 0;
	}

	public boolean isSecure() {
		
		return false;
	}

	public void removeAttribute(String arg0) {
		attrs.remove(arg0);
	}

	public void setAttribute(String arg0, Object arg1) {
		attrs.put(arg0, arg1);
	}

	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {
		

	}

}
