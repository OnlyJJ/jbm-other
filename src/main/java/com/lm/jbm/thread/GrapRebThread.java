package com.lm.jbm.thread;


import java.net.Socket;
import java.util.Random;

import com.lm.jbm.service.JmService;
import com.lm.jbm.socket.SocketUtil;
import com.lm.jbm.utils.RandomUtil;




public class GrapRebThread implements Runnable {

	
	private String rebId;
	
	private String userId;
	
	public GrapRebThread(String rebId, String userId) {
		this.rebId = rebId;
		this.userId = userId;
	}

	public void run() {
		try {
			String ip = RandomUtil.getUserIp(userId);
			String session = LoginThread.serssionMap.get(userId);
			Thread.sleep(RandomUtil.getRandom(100, 500));
			JmService.grapReb(userId, session, rebId, ip);
		} catch (Exception e) {
		}
	}

	public String getRebId() {
		return rebId;
	}

	public void setRebId(String rebId) {
		this.rebId = rebId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
