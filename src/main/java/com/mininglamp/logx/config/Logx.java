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
package com.mininglamp.logx.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.mininglamp.logx.util.CloseUtil;

/**
 * Config manager
 *
 */
public class Logx {
	
	private static Logx logx = new Logx();
	/**
	 * reload the config file must more then the time.
	 * default is five minutes.
	 */
	@SuppressWarnings("unused")
	private static long RELOAD_INTERVAL = 1000 * 60 * 5; 
	/**
	 * Config prop
	 */
	Map<String,Object> logxMap;
	
	
	private Logx() {
		init();
	}
	
	private void init(){
		logxMap = initProperties("/logx.properties");
		
	}
	/**
	 * load properties file
	 * @param fileName
	 * @return
	 */
	private Map<String,Object> initProperties(String fileName){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("fileName", fileName);
		
		Properties pro = new Properties();
		FileInputStream fis = null;
		try {
			String path = URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource(fileName).getPath(),"utf-8");
			map.put("filePath", path);
			File file = new File(path);
			Long lastModified = file.lastModified();
			map.put("lastModified", lastModified);
			fis = new FileInputStream(file);
			pro.load(fis);
			map.put("pro", pro);
			map.put("readtime", System.currentTimeMillis());
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Logx>>文件["+fileName+"]未找到");
		} catch (IOException e) {
			throw new RuntimeException("Logx>>文件["+fileName+"]读取失败");
		} catch (RuntimeException e){
		}finally{
			CloseUtil.closeIO(fis);
		}
		return map;
	}
	
//	public synchronized String getProperty(String key){
//		if(logxMap.isEmpty()||logxMap.get("pro")==null) return null;
//		
//		Properties pro = (Properties) logxMap.get("pro");
//		FileInputStream fis = null;
//		try {
//			long readtime = (Long) logxMap.get("readtime");
//			if(System.currentTimeMillis() - readtime > RELOAD_INTERVAL){
//				File file = new File((String) logxMap.get("filePath"));
//				Long lastModified = file.lastModified();
//				if (logxMap.get("lastModified") == null||logxMap.get("lastModified").equals(lastModified)) {
//					pro.clear();
//					fis = new FileInputStream(file);
//					pro.load(fis);
//					logxMap.put("lastModified", lastModified);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			CloseUtil.closeIO(fis);
//		}
//		return pro.getProperty(key);
//	}
	
	public String getProperty(String key){
		if(logxMap.isEmpty()||logxMap.get("pro")==null) return null;
		Properties pro = (Properties) logxMap.get("pro");
		return pro.getProperty(key);
	}
	
	public static Logx instance(){
		return logx;
	}
	
	
}
