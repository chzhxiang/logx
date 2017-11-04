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
package com.mininglamp.logx.log.version;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mininglamp.logx.config.URLMapping;
import com.mininglamp.logx.http.Const;
import com.mininglamp.logx.util.Utils;
import com.mininglamp.logx.util.WebUtil;

public final class LogVersionOne extends LogVersion{
	private URLMapping urlMapping = URLMapping.instance();
	public LogVersionOne() {
		super("1");
	}
	/**
	 * Modifyed by CZY
	 * @since 2016年4月6日11:49:09
	 * <br/>Add user platform info into log column 'userAgent',the content is 'platform|browser'.
	 */
	@Override
	public final String[] logArr(HttpServletRequest request) {
		HttpSession session = null;
		try {
			session = Utils.getSession(request);
		} catch (Exception e) {
			session = (HttpSession) request.getAttribute(Const.CURRENT_SESSION);
		}
		//请求地址,不带?后面内容
		String requestURI = request.getRequestURI();
		//请求参数
		String parameters = getRequestParameters(request);
		//SESSION编号
		String sessionid = session!=null?session.getId():null;
		//IP
		String ip = WebUtil.getIpAdd(request);
		//客户端信息(操作系统|浏览器)
		String userAgent = parseUserAgent(request.getHeader("user-agent"));
		
		return new String[]{parameters,requestURI,sessionid,ip,userAgent};
	}

	@Override
	public Map<String, Object> parseLog(String[] logArr) {
		Map<String, Object> map = new HashMap<String,Object>();
		if(logArr !=null &&logArr.length == 5){
			map.put(VersionConst.REQUEST_PARAMETERS, logArr[0]);
			String uri = logArr[1];
			map.put(VersionConst.REQUEST_URI, uri);
			map.put(VersionConst.REQUEST_URI_TEXT, urlMapping.getProperty(uri));
			map.put(VersionConst.SESSION_ID, logArr[2]);
			map.put(VersionConst.REQUEST_IP, logArr[3]);
			String userAgent = logArr[4];
			if(userAgent != null && userAgent.contains("|")){
				map.put(VersionConst.REQUEST_AGENT_PLATFORM, userAgent.split("\\|")[0]);
				map.put(VersionConst.REQUEST_EXPLORER_VER,userAgent.split("\\|")[1]);
			}else{
				map.put(VersionConst.REQUEST_EXPLORER_VER,userAgent);
			}
		}
		return map;
	}

	public String getSeparator() {
		return "^";
	}

}
