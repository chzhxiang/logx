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
package com.mininglamp.logx.kit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Map relative operations.
 * @author czy
 * @since 2016年4月15日14:19:49
 */
public class MapKit {
	/**
	 * Convert value type of Array to String.
	 * @param map
	 * @return<String,String>
	 */
	public static Map<String,String> convertArray2String(Map<String,String[]> map){
		Map<String,String> resultMap = new HashMap<String,String>();
		
		if(map == null || map.isEmpty()) return resultMap;
		
		Set<String> keys = map.keySet();
		for (String key : keys) {
			String[] arr = map.get(key);
			if(arr == null){
				resultMap.put(key, null);
			}else{
				resultMap.put(key, Arrays.toString(arr));
			}
		}
		return resultMap;
	}
	/**
	 * Convert value type of Array to String.
	 * @param map
	 * @return <String,Object>
	 */
	public static Map<String,Object> convertArray2Object(Map<String,String[]> map){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		if(map == null || map.isEmpty()) return resultMap;
		
		Set<String> keys = map.keySet();
		for (String key : keys) {
			String[] arr = map.get(key);
			if(arr == null){
				resultMap.put(key, null);
			}else{
				resultMap.put(key, Arrays.toString(arr));
			}
		}
		return resultMap;
	}
}
