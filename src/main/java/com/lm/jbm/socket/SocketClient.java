package com.lm.jbm.socket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.lm.jbm.thread.LoginThread;
import com.lm.jbm.thread.ThreadManager;
import com.lm.jbm.utils.PropertiesUtil;




public class SocketClient {

	public static Socket client = null;
	private static final String URL = PropertiesUtil.getValue("SOCKET_URL");
	private static final int PORT = Integer.parseInt(PropertiesUtil.getValue("SOKCET_PORT"));
	
	public synchronized static void init() {
		System.err.println("初始化...");
		try {
			if(client == null) {
				Thread.sleep(10000);
				client = new Socket(URL, PORT);
				
				LoginThread task = new LoginThread();
				ThreadManager.getInstance().execute(task);
				
				SocketInThread in = new SocketInThread();
				ThreadManager.getInstance().execute(in);
				Thread.sleep(2000);
				SocketHertThread hert = new SocketHertThread();
				ThreadManager.getInstance().execute(hert);
			}
		} catch (Exception e) {
		} 
	}
	
	public static Socket getInstance() {
		return client;
	}
	
	
	public static Socket creat() {
		Socket socket = null;
		try {
			socket = new Socket(URL, PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return socket;
	}
	@PreDestroy
	public void destory() {
		try {
			if(client != null) {
				client.close();
			}
		} catch (IOException e) {
		}
	}

}
