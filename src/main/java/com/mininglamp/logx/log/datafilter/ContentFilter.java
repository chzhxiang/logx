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
package com.mininglamp.logx.log.datafilter;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.mininglamp.logx.config.Logx;
import com.mininglamp.logx.http.Const;
import com.mininglamp.logx.kit.StrKit;

/**
 * Filt the log content.
 * @author czy
 * 2016年3月26日15:59:51
 */
public class ContentFilter {
	/**
	 * 返回列maxLength
	 */
	private int length = 0; 
	/**
	 * 是否抛出异常
	 */
	private String handle = "";
	/**
	 * Main logx config info object.
	 */
	private Logx logx = Logx.instance();
	
	private static ContentFilter contentFilter = new ContentFilter();
	
	private ContentFilter() {
		/**
		 * load log column maxLength
		 */
		load();
	}
	private void load(){
		handle = logx.getProperty("log.column.value.handle");
		if(StrKit.isBlank(handle)) handle = "interception";
		handle = handle.trim();
		String maxLen = logx.getProperty("log.column.value.maxLength");
		try{
			if(StrKit.notBlank(maxLen)){
				length = Integer.parseInt(maxLen);
			}else{
				length = 0;
			}
		}catch(Exception e){
			length = 0;
			e.printStackTrace();
		}
	}
	/**
	 * 限制用户自定义字段和值的长度
	 * @param userMap
	 */
	public void handleUserMap(Map<String,Object> userMap){
		load();
		
		if(userMap == null) return;
		
		if(length > 0){
			if("exception".equals(handle)){
				//throw exception
				Set<String> keySet = userMap.keySet();
				for (String string : keySet) {
					if(string != null&&string.length()>length){
						throw new RuntimeException("自定义日志内容键["+string+"]超过最大允许长度["+length+"]");
					}
				}
				Collection<Object> valueSet = userMap.values();
				for (Object object : valueSet) {
					if(object != null&&object.toString().length()>length){
						throw new RuntimeException("自定义日志内容值["+object.toString()+"]超过最大允许长度["+length+"]");
					}
				}
			}else if("interception".equals(handle)){
				//intercept the string
				Set<String> keySet = userMap.keySet();
				for (String _key : keySet) {
					Object _obj = userMap.get(_key);
					
					String _value = "",key,value;
					if(_obj != null) _value = _obj.toString();
					if(_key != null&&_key.length()>length){
						key = _key.substring(0,length);
						userMap.remove(_key);
					}else{
						key = _key;
					}
					if(_value != null&&_value.length()>length){
						value = _value.substring(0,length) + "|" + _value.length();
					}else{
						value = _value;
					}
					userMap.put(key, value);
				}
			}
			
		}
	}
	
	public static ContentFilter instance(){
		return contentFilter;
	}
	/**
	 * Filter the log column to remove invalid character.
	 * @param str
	 * @return
	 */
	public String filterStr(String str){
		return str == null?null:str.replaceAll("\\"+Const.LOG_COLUMN_SEPARATOR, Const.LOG_COLUMN_SHIFT_STR);
	}
}
