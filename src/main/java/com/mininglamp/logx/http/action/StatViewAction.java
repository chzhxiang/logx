/**
\ * Copyright (c) 2016-2026 Mininglamp Group Holding Ltd.
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

import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.mininglamp.logx.http.Const;
import com.mininglamp.logx.http.service.RequestActionService;
import com.mininglamp.logx.http.service.SessionService;
import com.mininglamp.logx.http.service.StatViewService;
import com.mininglamp.logx.kit.JsonKit;
import com.mininglamp.logx.log.bean.ModelAndView;
import com.mininglamp.logx.util.Utils;
/**
 * Handle dynamic data.
 * @author czy
 *
 */
public class StatViewAction extends ResourceAction{
	private StatViewService service = new StatViewService();
	private SessionService sessionService = new SessionService();
	private RequestActionService requestActionService = new RequestActionService();
	
	public StatViewAction() {
		super("support/http");
	}

	@Override
	public void init() throws ServletException {
		super.init();
	}
	
	
	
	/**
	 * process with data request
	 */
	@Override
	protected String process(HttpServletRequest request, String url) {
		//处理
		
		if(url.startsWith("api/getAllRunningAction.json")){
			return getAllRunningAction(request);
		}else if(url.startsWith("api/queryLog.json")){
			return queryLog(request);
		}else if(url.startsWith("api/getBrowsers.json")){
			return queryLog(request);
		}else if(url.startsWith("api/getLoginURL.json")){
			return wrapResult(getProperty(Const.LOG_VIEW_LOGINURL));
		}else if(url.startsWith("api/getAllSessions.json")){
			return wrapResult(getProperty(Const.LOG_VIEW_AllSESSION));
		}else if(url.startsWith("api/getCachSessionsInfo.json")){
			return getCachSessionsInfo(request);
		}else if(url.startsWith("api/startActions.json")){
			//启动请求数据暂存
			return requestActionService.startAction(request);
		}else if(url.startsWith("api/endActions.json")){
			//结束请求数据暂存
			return requestActionService.endAction(request);
		}else if(url.startsWith("api/removeAllActions.json")){
			//移除全部请求数据
			return requestActionService.removeAllRequestActions(request);
		}else if(url.startsWith("api/modifyActionInfo.json")){
			//修改请求数据
			return requestActionService.modifyActionInfo(request);
		}else if(url.startsWith("api/getActions.json")){
			//获取请求数据
			return requestActionService.getAction(request);
		}else if(url.startsWith("api/read/urlmapping.json")){
			//获取url映射文件内容
			return requestActionService.getUrlmappingContent(request);
		}else if(url.startsWith("api/write/urlmapping.json")){
			//保存url映射文件内容
			return requestActionService.saveUrlmappingContent(request);
		}
		
		return "test";
	}
	/**
	 * Get all running request.
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getAllRunningAction(HttpServletRequest request){
		ModelAndView modelAndView = new ModelAndView();
		List<Map> runningList = Utils.getActionInfoInApplicationContext(request);
		modelAndView.setObject(runningList);
		return JsonKit.toJson(modelAndView);
	}
	
	/**
	 * @param num
	 * @param period
	 * @return 获取缓存session间隔点
	 */
	private String getCachSessionsInfo(HttpServletRequest reqest){
		return sessionService.getCachSessionsInfo(reqest);
	}
	
	/**
	 * 通用查询日志
	 * @param request
	 * @return
	 */
	private String queryLog(HttpServletRequest request){
		return service.queryLog(request);
	}
	/**
	 * 包装结果，封装为ModelAndView格式并返回字符串
	 * @param statusCode
	 * @param message
	 * @param object
	 * @return
	 */
	private String wrapResult(String statusCode,String message,Object object){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setStatusCode(statusCode);
		modelAndView.setMessage(message);
		modelAndView.setObject(object);
		return JsonKit.toJson(modelAndView);
	}
	/**
	 * 包装返回结果，statusCode=200
	 * @param object
	 * @return
	 */
	private String wrapResult(Object object){
		return wrapResult("200",null,object);
	}
}
