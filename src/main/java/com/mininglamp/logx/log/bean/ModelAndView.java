package com.mininglamp.logx.log.bean;

/**
 * 返回前台bean
 * 
 * @author czy 2015年9月7日14:39:01
 */
public class ModelAndView {
	/**
	 * 200=成功
	 * 300=失败
	 * 350=部分失败
	 */
	private String statusCode = "200";
	private String message;
	private Long _; // 将请求的时间戳原样返回到前台，用于前台做请求匹配
	private Object object;

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long get_() {
		return _;
	}

	public void set_(Long _) {
		this._ = _;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return "ModelAndView [statusCode=" + statusCode + ", message="
				+ message + ", object=" + object + "]";
	}

}
