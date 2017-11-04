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
/**
 * Interceptor for data handle or ect,before action invoke.
 * @author czy
 * @since 2016年5月27日15:13:22
 */
public interface Interceptor {
	/**
	 * Do intercept.
	 * @param request
	 * @param response
	 * @param invotion 
	 * @throws IOException 
	 * @throws ServletException 
	 */
	void doIntercept(HttpServletRequest request,HttpServletResponse response, Invotion invotion) throws ServletException, IOException;
}
