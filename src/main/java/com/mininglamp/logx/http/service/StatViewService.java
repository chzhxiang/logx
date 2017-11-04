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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.mininglamp.logx.kit.JsonKit;
import com.mininglamp.logx.kit.StrKit;
import com.mininglamp.logx.log.LogManage;
import com.mininglamp.logx.log.bean.ModelAndView;
import com.mininglamp.logx.util.DateUtil;
import com.mininglamp.logx.util.WebUtil;
import com.mininglamp.logx.validate.DateValidator;
import com.mininglamp.logx.validate.TextCompareValidator;
import com.mininglamp.logx.validate.Validator;

public class StatViewService {
	private DateValidator dateValidator = new DateValidator();
	private Validator textCompareValidator = new TextCompareValidator();
	/**
	 * Query log with startTime and endTime.
	 * @param request
	 * @return
	 */
	public String queryLog(HttpServletRequest request){
		ModelAndView modelAndView = new ModelAndView();
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		/**
		 * Validate
		 */
		if(StrKit.isBlank(startTime)||StrKit.isBlank(endTime)||!dateValidator.validateDate(startTime)||!dateValidator.validateDate(endTime)){
			modelAndView.setMessage("缺少日期或格式错误,应为yyyy-MM-dd.参数:[startTime][endTime]");
			modelAndView.setStatusCode("300");
			return JsonKit.toJson(modelAndView);
		}
		try {
			DateUtil.getDateStrFormat(startTime, "yyyy-MM-dd");
			DateUtil.getDateStrFormat(endTime, "yyyy-MM-dd");
		} catch (Exception e) {
			modelAndView.setMessage("日期格式错误,应为yyyy-MM-dd");
			modelAndView.setStatusCode("300");
			return JsonKit.toJson(modelAndView);
		}
		/**
		 * Query
		 */
		List<Map<String, Object>> list = LogManage.getLogSummary(startTime, endTime);
		list = filterValid(list, WebUtil.getRequestParametersMap(request));
		modelAndView.setObject(list);
		return JsonKit.toJson(modelAndView);
	}
	
	/**
	 * Filter result by request parameters.
	 * @param list
	 * @param requestParameters
	 * @return
	 */
	private List<Map<String, Object>> filterValid(List<Map<String, Object>> list,Map<String, String> requestParameters){
		List<Map<String,Object>> resultList = new ArrayList<Map<String, Object>>();
		Set<String> keys = requestParameters.keySet();
		for (Map<String, Object> map : list) {
			boolean valid = true;
			for (String key : keys) {
				String value = requestParameters.get(key);
				if(value != null && value instanceof String && StrKit.notBlank(value)){
					String logValue = (String) map.get(key);
					if(logValue!=null&&!textCompareValidator.validate(logValue, value)) valid = false;
				}
			}
			if(valid) resultList.add(map);
		}
		return resultList;
	}
}
