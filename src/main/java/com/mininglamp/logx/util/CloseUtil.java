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
package com.mininglamp.logx.util;


import java.io.Closeable;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class CloseUtil {
	/**
	 * 关闭IO流
	 */
	/*
	public static void closeIO(Closeable... io){
		for(Closeable temp:io){
			try {
				if (null != temp) {
					temp.close();
				}
			} catch (Exception e) {
			}
		}
	}*/
	/**
	 * 使用泛型方法实现关闭IO流
	 * @param io
	 */
	public static <T extends Closeable> void closeIO(T... io){
		for(Closeable temp:io){
			try {
				if (null != temp) {
					temp.close();
				}
			} catch (Exception e) {
			}
		}
	}
	public static  void closeSocket(ServerSocket socket){
		try {
			if (null != socket) {
				socket.close();
			}
		} catch (Exception e) {
		}
	}
	public static  void closeSocket(Socket socket){
		try {
			if (null != socket) {
				socket.close();
			}
		} catch (Exception e) {
		}
}
	public static  void closeSocket(DatagramSocket socket){
			try {
				if (null != socket) {
					socket.close();
				}
			} catch (Exception e) {
			}
	}
}
