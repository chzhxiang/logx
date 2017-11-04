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
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.mininglamp.logx.util.CloseUtil;
import com.mininglamp.logx.util.Utils;

/**
 * Config manager
 *
 */
public class URLMapping {
	
	private static URLMapping mapping = new URLMapping();
	/**
	 * reload the config file must more then the time.
	 * default is five minutes.
	 */
	private static long RELOAD_INTERVAL = 1000 * 60 * 5; 
	/**
	 * Config prop
	 */
	Map<String,Object> map;
	
	
	private URLMapping() {
		init();
	}
	
	private boolean init(){
		map = initProperties("/url-mapping.properties");
		if(map == null) return false;
		else return true;
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
			String path = URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource("/").getPath(),"utf-8");
			path += fileName;
			map.put("filePath", path);
			File file = new File(path);
			if(!file.exists()){
				file.createNewFile();
			}
			fis = new FileInputStream(file);
			Long lastModified = file.lastModified();
			map.put("lastModified", lastModified);
			pro.load(fis);
			map.put("pro", pro);
			map.put("readtime", System.currentTimeMillis());
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Logx>>文件["+fileName+"]未找到");
		} catch (IOException e) {
			throw new RuntimeException("Logx>>文件["+fileName+"]读取失败");
		} catch (Exception e){
			e.printStackTrace();
		}finally{
			CloseUtil.closeIO(fis);
		}
		return map;
	}
	
	public synchronized String getProperty(String key){
		if(map.isEmpty()||map.get("pro")==null) return null;
		
		Properties pro = (Properties) map.get("pro");
		FileInputStream fis = null;
		try {
			long readtime = (Long) map.get("readtime");
			if(System.currentTimeMillis() - readtime > RELOAD_INTERVAL){
				File file = new File((String) map.get("filePath"));
				Long lastModified = file.lastModified();
				if (map.get("lastModified") == null||map.get("lastModified").equals(lastModified)) {
					pro.clear();
					fis = new FileInputStream(file);
					pro.load(fis);
					map.put("lastModified", lastModified);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			CloseUtil.closeIO(fis);
		}
		return pro.getProperty(key);
	}
	
	public synchronized void setProperty(String key,String val){
		if(map.isEmpty()||map.get("pro")==null) return;
		
		Properties pro = (Properties) map.get("pro");
		FileOutputStream fos = null;
		try {
			File file = new File((String) map.get("filePath"));
			fos = new FileOutputStream(file);
			pro.put(key, val);
			pro.store(fos, "Save ["+key+"]-["+val+"]");
			Long lastModified = file.lastModified();
			map.put("lastModified", lastModified);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			CloseUtil.closeIO(fos);
		}
	}
	/**
	 * Read config file content.
	 * @return
	 */
	public String readFileText(){
		if(map.isEmpty()||map.get("pro")==null) return null;
		
		File file = new File((String) map.get("filePath"));
		String content = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			content = Utils.read(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			CloseUtil.closeIO(fis);
		}
		return content;
	}
	
	public static URLMapping instance(){
		return mapping;
	}
	public void saveOrUpdate(String url,String text){
		setProperty(url,text);
	}
	/**
	 * 保存配置文件内容
	 * @param content
	 * @return
	 */
	public boolean saveFileText(String content) {
		if(map.isEmpty()||map.get("pro")==null) {
			if(!init()) return false;
		}
		
		File file = new File((String) map.get("filePath"));
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			Utils.copy(content, fos);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			CloseUtil.closeIO(fos);
		}
		return false;
	}
	/**
	 * 获取配置信息
	 * @return
	 */
	public synchronized Properties getProperties(){
		if(map.isEmpty()||map.get("pro")==null) return null;
		
		Properties pro = (Properties) map.get("pro");
		FileInputStream fis = null;
		try {
			long readtime = (Long) map.get("readtime");
			if(System.currentTimeMillis() - readtime > RELOAD_INTERVAL){
				File file = new File((String) map.get("filePath"));
				Long lastModified = file.lastModified();
				if (map.get("lastModified") == null||map.get("lastModified").equals(lastModified)) {
					pro.clear();
					fis = new FileInputStream(file);
					pro.load(fis);
					map.put("lastModified", lastModified);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			CloseUtil.closeIO(fis);
		}
		return pro;
	}
}
