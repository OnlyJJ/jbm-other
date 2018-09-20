package com.lm.jbm.thread;


import java.net.Socket;
import java.util.Random;

import com.lm.jbm.service.JmService;
import com.lm.jbm.socket.SocketUtil;
import com.lm.jbm.utils.RandomUtil;




public class GrapBoxThread implements Runnable {

	
	private String roomId;
	
	private String userId;
	
	private int way;
	
	public GrapBoxThread(String roomId, String userId, int way) {
		this.roomId = roomId;
		this.userId = userId;
		this.way = way;
	}

	public void run() {
		try {
			Socket socket = null;
			String ip = RandomUtil.getUserIp(userId);
			String session = LoginThread.serssionMap.get(userId);
			if(way == 0) { // 人少时
				Thread.sleep(RandomUtil.getRandom(1000, 3000));
				socket = SocketUtil.inRoom(roomId, userId);
				Thread.sleep(RandomUtil.getRandom(100, 500));
				JmService.grapBox(roomId, session, userId, ip);
			} else { // 人多时
				Thread.sleep(RandomUtil.getRandom(100, 500));
				socket = SocketUtil.inRoom(roomId, userId);
				Thread.sleep(RandomUtil.getRandom(100, 500));
				JmService.grapBox(roomId, session, userId, ip);
			}
			if(socket != null) {
				socket.close();
			}
		} catch (Exception e) {
		}
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getWay() {
		return way;
	}

	public void setWay(int way) {
		this.way = way;
	}

}
