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

public class VersionConst {
	public static final String REQUEST_URI = "requestURI";				//请求地址
	public static final String REQUEST_URI_TEXT = "requestURIText";		//请求地址中文
	public static final String REQUEST_PARAMETERS = "parameters";		//请求参数
	public static final String REQUEST_IP = "ip";						//IP地址
	public static final String REQUEST_EXPLORER_VER = "explorerVer";	//浏览器版本
	public static final String REQUEST_AGENT_PLATFORM = "platform";		//操作系统
	
	public static final String SESSION_ID = "sessionid";				//SESSION编号
	public static final String LOG_VERSION = "logVersion";				//日志版本
	public static final String LOG_ACTION_TIME = "logActionTime";		//记录时间
	public static final String LOG_ACTION_ID = "logActionId";			//操作编号
	public static final String LOG_ACTION_STATUS = "logActionStatus";	//操作状态 [start/end]
}
