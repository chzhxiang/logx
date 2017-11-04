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
package com.mininglamp.logx.util;

public class HttpUtil {
	
	/**
	 * 获取平台信息[通过读取User-Agent信息]
	 * @param userAgent
	 * @return
	 */
	public static String getPlatform(String userAgent){
		String info = null;
		if(userAgent.indexOf("Windows NT 5.0") > -1){
			info = "Win2000";
		}else if(userAgent.indexOf("Windows NT 5.1") > -1){
			info = "WinXP";
		}else if(userAgent.indexOf("Windows NT 5.2") > -1){
			info = "Win2003";
		}else if(userAgent.indexOf("Windows NT 6.0") > -1){
			info = "WindowsVista";
		}else if(userAgent.indexOf("Windows NT 6.1") > -1 || userAgent.indexOf("Windows 7") > -1){
			info = "Win7";
		}else if(userAgent.indexOf("Windows 8") > -1){
			info = "Win8";
		}else{
			info = "Other";
		}
		return info;
	}
	/**
	 * 获取用户代理信息
	 * @param userAgent
	 * @return [操作系统,浏览器,浏览器版本]
	 */
	public static String[] parseUserAgent(String userAgent){
		//操作系统
		String os = getPlatform(userAgent);
		String explorer = "";
		String explorerVer = "";
        if(userAgent.indexOf("Chrome")>0){
        	explorerVer=userAgent.toString().substring(userAgent.indexOf("Chrome")+7, userAgent.indexOf("Chrome")+9);
        	explorer = "chrome";
        }else if(userAgent.indexOf("Firefox")>0){
        	explorerVer=userAgent.toString().substring(userAgent.indexOf("Firefox")+8, userAgent.indexOf("Firefox")+10);
        	explorer="firefox";
        }else if(userAgent.indexOf("rv")>0){
        	explorerVer=userAgent.toString().substring(userAgent.indexOf("rv")+3, userAgent.indexOf("rv")+5);
        	explorer="ie";
        }else if(userAgent.indexOf("MSIE")>0){
        	explorerVer=userAgent.toString().substring(userAgent.indexOf("MSIE")+5, userAgent.indexOf("MSIE")+6);
        	explorer="ie";
        }
        
        return new String[]{os,explorer,explorerVer};
	}
}
