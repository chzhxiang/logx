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
package com.mininglamp.logx.http.interceptor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mininglamp.logx.http.action.ActionFactory;

public class Invotion implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String classString;
	private Class<?> clz;
	private Interceptor interceptor;
	private Invotion previous;
	private Invotion next;
	private Invotion head;
	
	public void invoke(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		if(interceptor == null){
			ActionFactory.invoke(request, response);
		}else{
			interceptor.doIntercept(request, response,next);
		}
	}
	public Invotion() {
	}
	public Invotion(Class<?> clz, Interceptor interceptor) {
		this.clz = clz;
		if(clz != null) this.classString = clz.getName();
		this.interceptor = interceptor;
	}
	public String getClassString() {
		return classString;
	}
	public void setClassString(String classString) {
		this.classString = classString;
	}
	public Class<?> getClz() {
		return clz;
	}
	public void setClz(Class<?> clz) {
		this.clz = clz;
		this.classString = clz.getName();
	}
	public Interceptor getInterceptor() {
		return interceptor;
	}
	public void setInterceptor(Interceptor interceptor) {
		this.interceptor = interceptor;
	}
	public Invotion getNext() {
		return next;
	}
	public void setNext(Invotion next) {
		this.next = next;
	}
	public Invotion getHead() {
		return head;
	}
	public void setHead(Invotion head) {
		this.head = head;
	}
	public Invotion getPrevious() {
		return previous;
	}
	public void setPrevious(Invotion previous) {
		this.previous = previous;
	}
}