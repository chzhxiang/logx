package com.mininglamp.logx.http.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ResponseWrapper extends HttpServletResponseWrapper{
	/**
	 * The resonse data store.
	 */
	private ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	@SuppressWarnings("unused")
	private HttpServletResponse response;
	private PrintWriter pw;
	private Map<String,String> headers = new HashMap<String,String>();
	
	
	@Override
	public void setHeader(String name, String value) {
		headers.put(name, value);
	}
	public Map<String,String> getHeaders(){
		return this.headers;
	}

	public ResponseWrapper(HttpServletResponse response) {
		super(response);
		this.response = response;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return new MyServletOutputStream(bytes);
	}
	
	public PrintWriter getWriter(){
		try{
			pw = new PrintWriter(new OutputStreamWriter(bytes,"utf-8"));
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return pw;
	}
	public byte[] getBytes(){
		if(pw!=null){
			pw.close();
			return bytes.toByteArray();
		}
		if(bytes!=null){
			try {
				bytes.flush();
				return bytes.toByteArray();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	class MyServletOutputStream extends ServletOutputStream{
		private ByteArrayOutputStream stream;
		
		public MyServletOutputStream(ByteArrayOutputStream stream) {
			this.stream = stream;
		}
		@Override
		public void write(int b) throws IOException {
			stream.write(b);
		}
	}
	
}
