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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mininglamp.logx.config.URLMapping;
import com.mininglamp.logx.util.RequestActionManage;

public class RequestAction {
	private URLMapping urlMapping = URLMapping.instance();
	/**
	 * 编号
	 */
	private String id;
	//启动时间
	private Long startTime;
	//更新时间
	private Long updateTime;
	/**
	 * 请求地址队列
	 */
	private List<String> actionList = new ArrayList<String>();
	/**
	 * 请求次数统计
	 */
	private Map<String,Integer> actionStatistic = new HashMap<String,Integer>();
	
	public RequestAction() {
		this.id = RequestActionManage.getId();
		this.startTime = new Date().getTime();
	}

	public String getId() {
		return id;
	}

	public void add(String url){
		Integer count = actionStatistic.get(url);
		if(count == null){
			count = 0;
			actionList.add(url);
		}
		actionStatistic.put(url, ++count);
		updateTime = new Date().getTime();
		
		
	}
	public Long getStartTime() {
		return startTime;
	}
	public Long getUpdateTime() {
		return updateTime;
	}
	public Map<String, Integer> getActionStatistic() {
		return actionStatistic;
	}
	/**
	 * 获取详细信息
	 * @return
	 */
	public List<Map<String,Object>> getAllActions(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(String url: actionList){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", this.id);
			map.put("url", url);
			map.put("text", urlMapping.getProperty(url));
			map.put("count", actionStatistic.get(url));
			list.add(map);
		}
		Collections.reverse(list);
		return list;
	}
}
