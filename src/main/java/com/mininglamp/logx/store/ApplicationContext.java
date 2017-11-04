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
package com.mininglamp.logx.store;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
/**
 * ApplicationContext store logx app info.
 * @author czy
 * 2016年10月12日17:07:20
 */
public class ApplicationContext {
	private static ApplicationContext instance = new ApplicationContext();
	
	private String _id;
	
	private Map<String,Object> attrs = new HashMap<String,Object>();
	
	private ApplicationContext() {
		init();
	}
	
	private void init(){
		this._id = UUID.randomUUID().toString();
	}
	
	
	public void setAttribute(String key,Object value){
		this.attrs.put(key, value);
	}
	
	public Object getAttribute(String key){
		return attrs.get(key);
	}
	/**
	 * Get the logx appcontext id.
	 * @return
	 */
	public String getId(){
		return this._id;
	}
	public static ApplicationContext instance(){
		return instance;
	}
}
