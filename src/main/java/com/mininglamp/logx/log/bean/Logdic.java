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
package com.mininglamp.logx.log.bean;
/**
 * used for log column introduce
 * It's stable and changed not frequently.
 *
 */
public class Logdic {
	
	private String actioncode;//操作类别
	private String actioninfo;//操作信息
	private String url;//对应请求地址
	
	public String getActioncode() {
		return actioncode;
	}
	public void setActioncode(String actioncode) {
		this.actioncode = actioncode;
	}
	public String getActioninfo() {
		return actioninfo;
	}
	public void setActioninfo(String actioninfo) {
		this.actioninfo = actioninfo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "Logdic [actioncode=" + actioncode + ", actioninfo="
				+ actioninfo + ", url=" + url + "]";
	}
}