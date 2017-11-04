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
package com.mininglamp.logx.validate;
/**
 * Validate date string
 * @author czy
 *
 */
public class DateValidator implements Validator{
	/**
	 * Default seperator.
	 */
	private String sep = "-";
	
	public boolean validate(String str, String regex) {
		if(str == null) return false;
		return str.matches(regex);
	}
	
	public boolean validateDate(String str){
		if(str == null) return false;
		return validateDate(str,this.sep);
	}
	public boolean validateDate(String str,String sep){
		if(str == null) return false;
		return str.matches("\\d{4}"+sep+"(0[1-9]|1[0-2])"+sep+"(0[1-9]|[1,2][0-9]|3[0,1])");
	}
}
