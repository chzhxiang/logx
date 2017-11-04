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
package com.mininglamp.logx.http.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import com.mininglamp.logx.config.URLMapping;
import com.mininglamp.logx.http.bean.RequestAction;
import com.mininglamp.logx.kit.JsonKit;
import com.mininglamp.logx.log.bean.ModelAndView;
import com.mininglamp.logx.util.RequestActionManage;
/**
 * Request action relation.
 * @author czy
 * @since 2016年5月3日14:20:54
 */
public class RequestActionService {
	private URLMapping urlMapping = URLMapping.instance();
	/**
	 * Request actions service
	 * @param request
	 * @return
	 */
	public String startAction(HttpServletRequest request){
		String taskId = request.getParameter("taskId");
		if(taskId != null) return endAction(request);
		
		ModelAndView modelAndView = new ModelAndView();
		RequestAction requestAction = RequestActionManage.start();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", requestAction.getId());
		map.put("list", requestAction.getAllActions());
		modelAndView.setObject(map);
		return JsonKit.toJson(modelAndView);
	}
	/**
	 * End
	 * @param request
	 * @return
	 */
	public String endAction(HttpServletRequest request){
		ModelAndView modelAndView = new ModelAndView();
		String taskId = request.getParameter("taskId");
		
		RequestAction requestAction = RequestActionManage.end(taskId);
		if(requestAction == null){
			modelAndView.setStatusCode("300");
			modelAndView.setMessage("未找到要停止的操作集合");
		}
		return JsonKit.toJson(modelAndView);
	}
	/**
	 * chaxun
	 * @param request
	 * @return
	 */
	public String getAction(HttpServletRequest request){
		String taskId = request.getParameter("taskId");
		if(taskId == null) return startAction(request);
		
		ModelAndView modelAndView = new ModelAndView();
		RequestAction requestAction = RequestActionManage.getRequestAction(taskId);
		if(requestAction == null) return startAction(request);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", requestAction.getId());
		map.put("list", requestAction.getAllActions());
		modelAndView.setObject(map);
		return JsonKit.toJson(modelAndView);
	}
	/**
	 * Remove all.
	 * @param request
	 * @return
	 */
	public String removeAllRequestActions(HttpServletRequest request){
		RequestActionManage.batchRemoveTask();
		ModelAndView modelAndView = new ModelAndView();
		return JsonKit.toJson(modelAndView);
	}
	/**
	 * Modify the action info.
	 * @param request
	 * @return
	 */
	public String modifyActionInfo(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		//parameters
		String url = request.getParameter("url");
		String text = request.getParameter("text");
		
		urlMapping.saveOrUpdate(url, text);
		
		return JsonKit.toJson(modelAndView);
	}
	/**
	 * Read config file of URLMapping.
	 * @param request
	 * @return
	 */
	public String getUrlmappingContent(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String content = urlMapping.readFileText();
		Properties pro = urlMapping.getProperties();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("content", content);
		map.put("pro", pro);
		map.put("timestamp", new Date().getTime());
		
		modelAndView.setObject(map);
		return JsonKit.toJson(modelAndView);
	}
	public String saveUrlmappingContent(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String content = request.getParameter("content");
		
		boolean flag = urlMapping.saveFileText(content);
		if(!flag) {
			modelAndView.setStatusCode("300");
			modelAndView.setMessage("保存失败");
		}
		return JsonKit.toJson(modelAndView);
	}
}
