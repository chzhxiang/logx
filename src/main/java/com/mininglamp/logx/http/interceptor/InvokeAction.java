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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mininglamp.logx.http.interceptor.impl.GzipInterceptor;

public class InvokeAction {
	/**
	 * Instance.
	 */
	private static InvokeAction invokeAction = new InvokeAction();
	private Invotion firstInvotion;
	/**
	 * Interceptor chain.
	 */
	private Map<Class<?>,Invotion> chain = new HashMap<Class<?>,Invotion>();
	
	private InvokeAction() {
		init();
	}
	private void init(){
		//Now only GzipInterceptor
		Interceptor interceptor = new GzipInterceptor();
		Class<?> clz = interceptor.getClass();
		Invotion invotion = new Invotion(clz,interceptor);
		invotion.setHead(invotion);
		firstInvotion = invotion;
		chain.put(clz, invotion);
		//Action Invotion
		Invotion actionInvotion = new Invotion();
		invotion.setNext(actionInvotion);
		actionInvotion.setPrevious(invotion);
		chain.put(null,actionInvotion);
		//TODO more...
		
	}
	
	public void doInvoke(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		//Start from the first Invotion.
		firstInvotion.invoke(request, response);
	}
	
	/**
	 * Get the InvokeAction instance.
	 * @return InvokeAction instance
	 */
	public static InvokeAction instance(){
		return invokeAction;
	}
	
}
