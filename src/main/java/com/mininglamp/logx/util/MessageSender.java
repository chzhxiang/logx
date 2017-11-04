package com.mininglamp.logx.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.mininglamp.logx.config.Logx;
import com.mininglamp.logx.log.LogManage;

/**
 * 消息发送
 * 统一发送到logm
 * @author czy
 * 2016年8月29日16:19:12
 */
public class MessageSender {
	/**
	 * 消息发送地址
	 */
	private static final String MESSAGE_URL = "http://21.32.3.70:8200/message/";
	/**
	 * appCode
	 */
	private static String appCode = Logx.instance().getProperty("log.app.code");
	/**
	 * 消息发送
	 * @param level 级别
	 * @param title 标题
	 * @param content 正文
	 * @param data 数据
	 * @return
	 */
	private static boolean send(String level,String title,String content,String data){
		if(title == null || content == null) return false;
		
		Map<String, String> map = new HashMap<String,String>();
		map.put("app", appCode);
		map.put("title", title);
		map.put("content", content);
		map.put("data", data);
		
		try {
			HttpClient.postStream(MESSAGE_URL + "error",map);
		} catch (IOException e) {
			LogManage.logInfo("Logm消息发送失败",e.getMessage(),"appCode",appCode);
		}
		
		return true;
	}
	/**
	 * error
	 * @param title 标题
	 * @param content 正文
	 * @param data 数据
	 * @return
	 */
	public static boolean error(String title,String content,String data){
		return send("error", title, content, data);
	}
	/**
	 * info
	 * @param title 标题
	 * @param content 正文
	 * @param data 数据
	 * @return
	 */
	public static boolean info(String title,String content,String data){
		return send("info", title, content, data);
	}
	/**
	 * debug
	 * @param title 标题
	 * @param content 正文
	 * @param data 数据
	 * @return
	 */
	public static boolean debug(String title,String content,String data){
		return send("debug", title, content, data);
	}
}
