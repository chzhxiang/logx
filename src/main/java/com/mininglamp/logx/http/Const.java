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
package com.mininglamp.logx.http;
/**
 * 
 * Const for the System
 *
 */
public class Const {
	
	public static String DEFAULT_ENCODING = "UTF-8";
	/*********************** 列分隔符 **************************/
	/**
	 * 列分隔符
	 */
	public static final char LOG_COLUMN_SEPARATOR = '^';		//原始
	/**
	 * 列分隔符替代字符串
	 */
	public static final String LOG_COLUMN_SHIFT_STR = "_._";	//替代
	
	/*********************** 参数分隔符 **************************/
	/**
	 * 参数分隔符
	 */
	public static final char LOG_PARAMETER_SEPARATOR = '`';		//原始
	/**
	 * 参数分隔符替代字符串
	 */
	public static final String LOG_PARAMETER_SHIFT_STR = "_-_";	//替代
	
	/*********************** Action **************************/
	/**
	 * 系统登录地址 用于精确分析登陆系统的浏览器、操作系统等信息
	 */
	public static final String LOG_VIEW_LOGINURL = "log.view.loginURL";	
	/**
	 * 获取所有sessions
	 */
	public static final String LOG_VIEW_AllSESSION = "log.view.allSession";	
	/**
	 * 获取所有session时间点
	 */
	public static final String LOG_VIEW_ALLSESSIONINFO = "log.view.allSessionInfo";
	/**
	 * 当前SESSION
	 */
	public static final String CURRENT_SESSION = "currentSession";
	/**
	 * 应用
	 */
	public static final String APPLICATION_CONTEXT = "logxApplicationContext";
	
}
