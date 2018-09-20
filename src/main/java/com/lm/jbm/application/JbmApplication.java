package com.lm.jbm.application;



import com.lm.jbm.socket.SocketClient;
import com.lm.jbm.utils.PropertiesUtil;



public class JbmApplication {
	
	public static void main(String[] args) {
		try {
			// false：开发环境，true：生产
			PropertiesUtil.load(true);
			SocketClient.init();
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
