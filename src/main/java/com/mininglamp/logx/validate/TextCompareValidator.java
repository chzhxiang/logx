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
 * Validate the text with the operator and value.<br/>
 * Now the given regex format is operator_value.<br/>
 * <b>Operator is below:</b><br/>
 * 1、eq		: string equal											      <br/>
 * 2、lk		: string contains                                             <br/>
 * 3、gt		: convert to double and compare >                             <br/>
 * 4、lt		: convert to double and compare <                             <br/>
 * 5、gte	: convert to double and compare >=                            <br/>
 * 6、lte	: convert to double and compare <=                            <br/>
 * @author czy
 * 2016年11月12日10:32:37
 */
public class TextCompareValidator implements Validator{
	/**
	 * operator enum.
	 * @author czy
	 * 2016年11月12日10:32:43
	 */
	enum OPERATOR{
		EQ("eq"),
		LK("lk"),
		GT("gt"),
		LT("lt"),
		GTE("gte"),
		LTE("lte");
		
		private String context;
		private OPERATOR(String op){
			this.context = op;
		}
		private String getContext(){
			return this.context;
		}
	}
	public boolean validate(String str, String regex) {
		if(str == null||regex == null) return false;
		String[] arr = regex.split("_");
		if(arr.length != 2) return str.contains(regex);
		return validateWithOp(str,arr[0],arr[1]);
	}
	/**
	 * switch operator
	 * @param str original string
	 * @param op operator 
	 * @param value pattern value
	 * @return
	 */
	private boolean validateWithOp(String str,String op,String value){
		boolean flag = false;
		try {
			if (OPERATOR.EQ.getContext().equals(op)) {
				flag = opEq(str, value);
			} else if (OPERATOR.LK.getContext().equals(op)) {
				flag = opLk(str, value);
			} else if (OPERATOR.GT.getContext().equals(op)) {
				flag = opGt(str, value);
			} else if (OPERATOR.LT.getContext().equals(op)) {
				flag = opLt(str, value);
			} else if (OPERATOR.GTE.getContext().equals(op)) {
				flag = opGte(str, value);
			} else if (OPERATOR.LTE.getContext().equals(op)) {
				flag = opLte(str, value);
			}
		} catch (Exception e) {
			return false;
		}
		return flag;
	}
	private boolean opEq(String str,String value){
		return str.equals(value);
	}
	private boolean opLk(String str,String value){
		return str.contains(value);
	}
	
	private boolean opGt(String str,String value){
		Double double1 = Double.parseDouble(str);
		Double double2 = Double.parseDouble(value);
		return double1 > double2;
	}
	private boolean opLt(String str,String value){
		Double double1 = Double.parseDouble(str);
		Double double2 = Double.parseDouble(value);
		return double1 < double2;
	}
	private boolean opGte(String str,String value){
		Double double1 = Double.parseDouble(str);
		Double double2 = Double.parseDouble(value);
		return double1 >= double2;
	}
	private boolean opLte(String str,String value){
		Double double1 = Double.parseDouble(str);
		Double double2 = Double.parseDouble(value);
		return double1 <= double2;
	}
}
