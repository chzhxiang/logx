package com.mininglamp.logx.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * HTTP请求工具
 * @author 池宗洋
 * 2016年8月29日20:21:19
 */
public class HttpClient {
	private static final String DEFAULT_USER_AGENT="Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.89 Safari/537.36";
	public static void main(String[] args) throws IOException {
		Map<String,String> params=new HashMap<String, String>();
		params.put("User-Agent",DEFAULT_USER_AGENT);
		String str=getString("http://21.32.3.70/hbpost",params);
		System.out.println(str);
	}
	/**
	 * 抓取页面作为字符串返回
	 * @param urlStr 地址
	 * @param header 头信息
	 * @return 页面文本
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static String getString(String urlStr,Map<String,String> header)throws IOException{
		URLConnection conn=getConnection(urlStr, header);
		InputStream is=conn.getInputStream();
		InputStreamReader isr=new InputStreamReader(is,"utf-8");
		StringBuilder sb=new StringBuilder();
		char[] a=new char[1024];
		int n=0;
		while((n=isr.read(a))>0){
			sb.append(a);
		}
		return sb.toString();
	}
	/**
	 * 抓取页面作为字符串返回
	 * @param urlStr 地址
	 * @param header 头信息
	 * @return 输入流
	 * @throws IOException
	 */
	public static InputStream getStream(String urlStr,Map<String,String> header)throws IOException{
		URLConnection conn=getConnection(urlStr, header);
		InputStream is=conn.getInputStream();
		return is;
	}
	private static URLConnection getConnection(String urlStr,Map<String,String> header) throws IOException{
		URL url=new URL(urlStr);
		URLConnection conn=url.openConnection();
		conn.setDoOutput(true);
		if(header!=null&&!header.isEmpty()){
			Set<String> keys=header.keySet();
			for (String key : keys) {
				conn.addRequestProperty(key, header.get(key));
			}
		}
		conn.connect();
		return conn;
	}
	/**
	 * post请求发送
	 * @param urlStr 地址
	 * @return
	 * @throws IOException
	 */
	public static String postStream(String urlStr) throws IOException{
		return postStream(urlStr, null);
	}
	/**
	 * post请求发送
	 * @param urlStr 地址
	 * @param params 参数
	 * @return
	 * @throws IOException
	 */
	public static String postStream(String urlStr,Map<String,String> params) throws IOException{
		return postStream(urlStr,params,null);
	}
	/**
	 * 
	 * @param urlStr 地址
	 * @param header 头信息
	 * @param map 想要传输的数据
	 * @return 返回请求相应--字符串
	 * @throws IOException
	 */
	public static String postStream(String urlStr,Map<String,String> params,Map<String,String> headers) throws IOException{
		StringBuffer paramSb = new StringBuffer();
		//循环map,取出对应的key和value,拼接成字符串
		if(params != null){
			Set<String> paramKeys = params.keySet();
			for (String key : paramKeys) {
				paramSb.append(key);
				paramSb.append("=");
				paramSb.append(params.get(key));
				paramSb.append("&");
			}
			//去掉字符串末尾的“&”
			if(paramSb.length() > 0)
				paramSb.deleteCharAt(paramSb.length()-1);
		}
		//调用函数，建立Http连接
		HttpURLConnection conn = (HttpURLConnection) HttpClient.postConnection(urlStr,headers);
		// 实例化OutputStream、StringBuffer、OutputStreamWriter
		OutputStream os = conn.getOutputStream();
		StringBuffer resultBuffer = new StringBuffer();
		OutputStreamWriter osw = new OutputStreamWriter(os,"utf-8");
		//发送数据
		osw.write(paramSb.toString());
		osw.flush();
		// 接收数据，实例化InputStream、InputStreamReader、BufferedReader
		InputStream is = conn.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isr);
		
		String tempLine = null;
		//按行读取接收到的数据
		while((tempLine = reader.readLine()) != null){
			resultBuffer.append(tempLine);
		}
		// 关闭发送连接和接收连接
		if(os != null)os.close();
		if(osw != null)osw.close();
		if(is != null)is.close();
		if(isr != null)isr.close();
		if(reader != null)reader.close();
		return resultBuffer.toString();
	}
	//模拟浏览器，建立连接
	private static URLConnection postConnection(String urlStr,Map<String,String> headers) throws IOException{
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		//设置header 头信息
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setConnectTimeout(10000);
		addDefaultHeader(conn);
		if(headers!=null&&!headers.isEmpty()){
			Set<String> keys = headers.keySet();
			for(String key:keys){
				conn.addRequestProperty(key, headers.get(key));
			}
		}
		//建立连接
		conn.connect();
		return conn;
	}
	/**
	 * 加入通用header
	 * @param conn
	 */
	private static void addDefaultHeader(HttpURLConnection conn){
		conn.setRequestProperty("Accept-Charset", "UTF-8");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
		conn.setRequestProperty("User-Agent",DEFAULT_USER_AGENT);
	}
}
