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
package com.mininglamp.logx.log;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Logger interface, to write and read log.
 * @author czy
 *
 */
interface Logger {
	/**
	 * init the logger 
	 * eg init the connect or the config
	 */
	void init();
	/**
	 * log the string
	 * @param logStr
	 */
	void log(String logStr);
	/**
	 * get the log
	 * @return logline
	 */
	List<String> getLog(String startTime,String endTime);
	/**
	 * Get user defined information,such as username,orgnazation,etc.
	 * @param request
	 * @return
	 */
	Map<String,Object> getUserDefined(HttpServletRequest request);
}
