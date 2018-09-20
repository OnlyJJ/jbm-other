package com.lm.jbm.socket;

import java.io.IOException;
import java.net.Socket;

import com.lm.jbm.thread.LoginThread;
import com.lm.jbm.thread.ThreadManager;


public class SocketRestartThread implements Runnable {
	public SocketRestartThread() {
	}

	public void run() {
		if(!SocketUtil.rebuildFlag) {
			SocketUtil.rebuildFlag = true;
			try {
				if(SocketClient.client != null) {
					SocketClient.client.close();
				}
			} catch (IOException e) {
			} finally {
				SocketClient.client = null;
			}
			while(true) {
				try {
					SocketClient.init();
					SocketUtil.RECOUNT = 0;
					SocketUtil.rebuildFlag = false;
					break;
				} catch(Exception e) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
				}
			}
		}
	}

}
