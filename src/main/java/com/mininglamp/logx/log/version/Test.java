package com.mininglamp.logx.log.version;

import java.util.List;

public class Test {
	public static void main(String[] args) {
		LogVersionOne one = new LogVersionOne();
		List<String> list = one.loadConfig();
		System.out.println("--------------------返回数据----------------");
		for(String str: list){
			System.out.println(str);
		}
	}
}
