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
package com.mininglamp.logx.log.version;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mininglamp.logx.http.Const;
import com.mininglamp.logx.http.bean.HttpRequestWrapperPlain;
import com.mininglamp.logx.kit.JsonKit;
import com.mininglamp.logx.kit.StrKit;
import com.mininglamp.logx.log.datafilter.ContentFilter;
import com.mininglamp.logx.util.CloseUtil;
import com.mininglamp.logx.util.Utils;
import com.mininglamp.logx.util.XMLUtil;
/**
 * <b>Basic column is :</b><br/>
 * Time</br>
 * Version</br>
 * Actionid</br>
 * 
 * @since Ver1.0
 * @author CZY
 *
 */
public abstract class LogVersion implements Parser{
	//The version.
	private String version;
	/**
	 * Content filter.
	 */
	private static ContentFilter contentFilter = ContentFilter.instance();
	/**
	 * store all logversion object,to parse the version of logstr.
	 */
	private List<LogVersion> versionList = new ArrayList<LogVersion>();
	
	public LogVersion(String version) {
		this.version = version;
	}

	/**
	 * get log string,
	 * handle subversion,
	 * now subversion contains X.1
	 */
	public final StringBuffer logStr(HttpServletRequest request) {
		String[] arr = null;
		String _version = version;
		/**
		 * If not explorer request,it must be HttpRequestWrapperPlain instance.
		 */
		if(!(request instanceof HttpRequestWrapperPlain)){
			arr = logArr(request);
		}else{
			_version += explorerSubVersion;
		}
		StringBuffer sb = new StringBuffer();
		
		String actionid = Utils.getActionId(request);
		String time = getLogTime();
		
		sb.append(time);
		sb.append(Const.LOG_COLUMN_SEPARATOR);
		sb.append(_version);
		sb.append(Const.LOG_COLUMN_SEPARATOR);
		sb.append(actionid);
		sb.append(Const.LOG_COLUMN_SEPARATOR);
		
		if(arr != null && arr.length>0){
			
			for (String column : arr) {
				sb.append(validateLogstr(column));
				sb.append(Const.LOG_COLUMN_SEPARATOR);
			}
		}
		
		return sb;
	}
	/**
	 * 通用处理内容
	 * 目前暂定固定字段为时间、版本号、请求编号
	 * 2016年3月21日16:32:47
	 * CZY
	 */
	@SuppressWarnings("unchecked")
	public final Map<String, Object> parseLogstr(String logStr) {
		if(StrKit.isBlank(logStr)) return null;
		
		String[] arr = logStr.split("\\"+Const.LOG_COLUMN_SEPARATOR);
		if(arr.length<3) return null;
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put(VersionConst.LOG_ACTION_TIME, arr[0]);
		map.put(VersionConst.LOG_VERSION, arr[1]);
		map.put(VersionConst.LOG_ACTION_ID, arr[2]);
		/**
		 * TODO
		 */
		for(LogVersion logVer:versionList){
			if(logVer.version !=null && logVer.version.trim().equals(arr[1]))
				logVer.parseLog(Arrays.copyOfRange(arr, 3,arr.length));
		}
		
		Map<String,Object> versionMap = null;
		if(arr[1] != null && !arr[1].endsWith(explorerSubVersion)){
			versionMap = parseLog(Arrays.copyOfRange(arr, 3,arr.length-1));
		}
		if(versionMap != null)
			map.putAll(versionMap);
		try{
			Map<String,Object> userDefined = JsonKit.jsonToMap(arr[arr.length-1]);
			map.putAll(userDefined);
		}catch(Exception e){
		}
		return map;
	}
	/**
	 * handle log column
	 * @param request
	 * @return
	 */
	public abstract String[] logArr(HttpServletRequest request);
	protected abstract Map<String,Object> parseLog(String[] logArr);
	
	/**
	 * get the request actionid
	 * @since 2016-3-31
	 * Please call Utils.getActionId
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private String getActionId(HttpServletRequest request){
		String actionId = null;
		if(request!=null){
			Object obj = request.getAttribute("logx-actionid");
			if(obj !=null&&obj instanceof String) actionId = (String) obj;
			else{
				UUID uuid = UUID.randomUUID();
				actionId = uuid.toString().replace("-", "");
				request.setAttribute("logx-actionid", actionId);
			}
		}
		return actionId;
	}
	/**
	 * Get the log time.
	 * @return
	 */
	protected String getLogTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String newDate=sdf.format(new Date());
		return newDate;
	}
	
	/**
	 * 处理特殊字符
	 * @param arr 字段值
	 * @return
	 */
	protected String validateLogstr(String ...arr){
		StringBuilder sb = new StringBuilder();
		if(arr!=null){
			for (String column : arr) {
				sb.append(contentFilter.filterStr(column));
				sb.append(Const.LOG_COLUMN_SEPARATOR);
			}
			if(sb.length()>0) sb = sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	
	/*********************************	请求参数处理	***************************************/
	protected String getRequestParameters(HttpServletRequest request){
		return getRequestParameters(request,Character.toString(Const.LOG_PARAMETER_SEPARATOR));
	}
	@SuppressWarnings("unchecked")
	protected String getRequestParameters(HttpServletRequest request,String sep){
		if(request == null) return null;
		StringBuilder param= new StringBuilder();
		for (Enumeration<String> eh = request.getParameterNames(); eh.hasMoreElements();) {
			String parName = eh.nextElement();
			Object parValueObj = request.getParameter(parName);
			String parValue = "";
			if(parValueObj != null)	parValue = parValueObj.toString();
			
			if(parName!=null) parName = parName.replaceAll(sep, Const.LOG_PARAMETER_SHIFT_STR);
			if(parValue!=null) parValue = parValue.replaceAll(sep, Const.LOG_PARAMETER_SHIFT_STR);
			
			param.append(parName+"="+parValue+sep);
		}
		if(param.length()>0) param = param.deleteCharAt(param.length()-1);
		
		return param.toString();
	}
	/**
	 * 解析useragent获取有效信息
	 * @param userAgent
	 * @return
	 */
	public String parseUserAgent(String userAgent){
		return parseUserAgentPlatform(userAgent)+"|"+parseUserAgentBrowser(userAgent);
	}
	/**
	 * 获取浏览器信息
	 * @param userAgent
	 * @return
	 */
	public String parseUserAgentBrowser(String userAgent){
		if(StrKit.isBlank(userAgent)) return null;
		
		if(userAgent.indexOf("Chrome")>0){
			userAgent=userAgent.toString().substring(userAgent.indexOf("Chrome")+7, userAgent.indexOf("Chrome")+9);
			userAgent="chrome/"+userAgent;
		}else if(userAgent.indexOf("Firefox")>0){
			userAgent=userAgent.toString().substring(userAgent.indexOf("Firefox")+8, userAgent.indexOf("Firefox")+10);
			userAgent="firefox/"+userAgent;
		}else if(userAgent.indexOf("rv")>0){
			userAgent=userAgent.toString().substring(userAgent.indexOf("rv")+3, userAgent.indexOf("rv")+5);
			userAgent="ie/"+userAgent;
		}else if(userAgent.indexOf("MSIE")>0){
			userAgent=userAgent.toString().substring(userAgent.indexOf("MSIE")+5, userAgent.indexOf("MSIE")+6);
			userAgent="ie/"+userAgent;
		}
		return userAgent;
	}
	/**
	 * 获取客户端操作系统
	 * @param userAgent
	 * @return
	 */
	public String parseUserAgentPlatform(String userAgent){
		if(StrKit.isBlank(userAgent)) return null;
		
		userAgent = userAgent.toLowerCase();
		
		if(userAgent.indexOf("ipad")>-1){
			return "ipad";
		}else if(userAgent.indexOf("iphone os")>-1){
			return "iphone";
		}else if(userAgent.indexOf("android")>-1){
			return "android";
		}else if(userAgent.indexOf("windows ce")>-1){
			return "windowsce";
		}else if(userAgent.indexOf("windows mobile")>-1){
			return "windowsmobile";
		}else if(userAgent.indexOf("windows nt 5.0")>-1){
			return "win2k";
		}else if(userAgent.indexOf("windows nt 5.1")>-1){
			return "xp";
		}else if(userAgent.indexOf("windows nt 6.0")>-1){
			return "vista";
		}else if(userAgent.indexOf("windows nt 6.1")>-1){
			return "win7";
		}else if(userAgent.indexOf("windows nt 6.2")>-1){
			return "win8";
		}else if(userAgent.indexOf("windows nt 6.3")>-1){
			return "win81";
		}else if(userAgent.indexOf("mac os")>-1){
			return "mac";
		}else{
			return "";
		}
		
	}
	
	/**
	 * 加载日志配置文件
	 * @return
	 */
	public List<String> loadConfig(){
		List<String> list = new ArrayList<String>();
		//得到文件名
		String fileName = this.getClass().getSimpleName()+".xml";
		InputStream in = null;
		try {
			String path = URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource("/log/"+fileName).getPath(),"utf-8");
			in = new FileInputStream(path);
			Document doc = XMLUtil.parseToXml(in);
			NodeList nodeList = XMLUtil.getAllChildByTagName(doc, "name");
			for(int i=0;i<nodeList.getLength();i++){
				Node node = nodeList.item(i);
				NamedNodeMap m = node.getAttributes();
				list.add(m.item(0).getNodeValue());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("LogVersion*.xml>>文件["+fileName+"]读取失败");
		}finally{
			CloseUtil.closeIO(in);
		}
		return list;
	}

	public String getVersion() {
		return version;
	}
}
