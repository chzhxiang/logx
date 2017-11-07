package com.mininglamp.logx.http.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import com.mininglamp.logx.kit.StrKit;
/**
 * Plain HttpSession.
 * @author czy
 *
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public class HttpSessionWrapperPlain implements HttpSession {
	private Hashtable<String,Object> attrs = new Hashtable<String,Object>();
	private Hashtable<String,Object> vals = new Hashtable<String,Object>();
	private long createTime = new Date().getTime();
	private String id = StrKit.getUUID();
	private int maxInactiveInterval;

	public Object getAttribute(String arg0) {
		return attrs.get(arg0);
	}

	
	public Enumeration getAttributeNames() {
		return attrs.keys();
	}

	public long getCreationTime() {
		return createTime;
	}

	public String getId() {
		return id;
	}

	public long getLastAccessedTime() {
		return createTime;
	}

	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public ServletContext getServletContext() {
		return null;
	}

	public HttpSessionContext getSessionContext() {
		
		return null;
	}

	public Object getValue(String arg0) {
		return vals.get(arg0);
	}

	public String[] getValueNames() {
		List<String> list = new ArrayList<String>();
		for(String str:vals.keySet()){
			list.add(str);
		}
		String[] arr = new String[list.size()];
		return list.toArray(arr);
	}
	
	public void invalidate() {
		this.attrs.clear();
		this.vals.clear();
	}

	public boolean isNew() {
		return false;
	}

	public void putValue(String arg0, Object arg1) {
		vals.put(arg0, arg1);
	}

	public void removeAttribute(String arg0) {
		this.attrs.remove(arg0);
	}

	public void removeValue(String arg0) {
		vals.remove(arg0);
	}

	public void setAttribute(String arg0, Object arg1) {
		attrs.put(arg0, arg1);
	}

	public void setMaxInactiveInterval(int arg0) {
		this.maxInactiveInterval = arg0;
	}

}
