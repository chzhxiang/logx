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
package com.mininglamp.logx.http.interceptor.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mininglamp.logx.http.bean.ResponseWrapper;
import com.mininglamp.logx.http.interceptor.Interceptor;
import com.mininglamp.logx.http.interceptor.Invotion;

/**
 * Gzip the response data.
 * @author czy
 * @since 2016年5月27日15:22:06
 */
public class GzipInterceptor implements Interceptor{

	

	public void doIntercept(HttpServletRequest request,	HttpServletResponse response, Invotion invotion) throws ServletException, IOException {
		/**
		 * 启用Gzip压缩
		 */
		ResponseWrapper myResponse = new ResponseWrapper(response); 
		
		invotion.invoke(request, myResponse);
		/**
		 * 执行Gzip压缩
		 */
		byte[] originalByte = myResponse.getBytes();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(out);
		gzipOutputStream.write(originalByte);
		gzipOutputStream.close();
		byte[] resultByte = out.toByteArray();
		/**
		 * 将Header传入原始response
		 */
		Map<String,String> headers = myResponse.getHeaders();
		Set<String> keys = headers.keySet();
		for (String key : keys) {
			response.setHeader(key, headers.get(key));
		}
		response.setHeader("Content-Encoding", "gzip");
		response.getOutputStream().write(resultByte);
	}

}
