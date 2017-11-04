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

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.mininglamp.logx.http.bean.RequestAction;
/**
 * 请求信息统计
 * @author czy
 *
 */
public class RequestActionManage {
	private static long id;
	private static Map<String,RequestAction> actions = new HashMap<String,RequestAction>();
	
	
	private RequestActionManage() {
	}
	
	/**
	 * 获取编号
	 * @return
	 */
	public static synchronized String getId(){
		long t=++id;
		return String.valueOf(t);
	}
	/**
	 * 当前任务加入任务列表
	 * @param task
	 */
	private static void addTask(RequestAction requestAction){
		actions.put(requestAction.getId(), requestAction);
	}
	/**
	 * 创建请求动作并返回
	 * @return
	 */
	public static RequestAction createRequestAction(){
		RequestAction requestAction=new RequestAction();
		addTask(requestAction);
		//检查超时未操作
		checkRequestActions();
		return requestAction;
	}
	/**
	 * 移除无用动作集合
	 */
	private static void checkRequestActions() {
		Collection<RequestAction> sets = actions.values();
		Long now = new Date().getTime();
		//移除启动时间大于1天的
		Long limit = now - 1000*3600*24;
		for (RequestAction requestAction : sets) {
			if(requestAction.getStartTime()<limit) removeRequestAction(requestAction.getId());
		}
	}
	/**
	 * Get actions.
	 * @param id
	 * @return
	 */
	public static RequestAction getRequestAction(String id){
		return actions.get(id);
	}

	/**
	 * 移除操作请求
	 * @param id 编号
	 * @return
	 */
	public static synchronized RequestAction removeRequestAction(String id){
		return actions.remove(id);
	}
	
	public static RequestAction start(){
		return createRequestAction();
	}
	public static RequestAction end(String id){
		return actions.remove(id);
	}
	/**
	 * 移除全部
	 */
	public static synchronized void batchRemoveTask(){
		actions.clear();
	}
	/**
	 * 批量添加地址信息
	 * @param url
	 */
	public static synchronized void addURL4All(String url){
		Collection<RequestAction> sets = actions.values();
		for (RequestAction requestAction : sets) {
			requestAction.add(url);
		}
	}
}
