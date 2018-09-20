package com.lm.jbm.thread;


import java.net.Socket;
import java.util.Random;

import com.lm.jbm.service.JmService;
import com.lm.jbm.socket.SocketUtil;
import com.lm.jbm.utils.RandomUtil;




public class PeachThread implements Runnable {

	
	private String roomId;
	
	private String userId;
	
	private int way;
	
	public PeachThread(String roomId, String userId, int way) {
		this.roomId = roomId;
		this.userId = userId;
		this.way = way;
	}

	public void run() {
		try {
			Socket socket = null;
			String ip = RandomUtil.getUserIp(userId);
			String session = LoginThread.serssionMap.get(userId);
			int sleepTime1 = 1000; // 延迟进入房间时间
			int sleepTime2 = 1000; // 进入房间后延迟摘桃时间
			boolean flag = JmService.checkFreeTime(); // 是否是 01:00 ~ 10:30
			boolean isInroom = true;
			if(way <= 10) { // 人太少
				if(flag) { // 01:00 ~ 10:30，间隔6~12秒
					sleepTime1 = RandomUtil.getRandom(3000, 6000);
					sleepTime2 = RandomUtil.getRandom(3000, 6000);
				} else { // 其他时间段， 间隔5~9秒
					sleepTime1 = RandomUtil.getRandom(3000, 6000);
					sleepTime2 = RandomUtil.getRandom(2000, 3000);
				}
			} else if(way > 10 && way <= 20) { // 人少
				if(flag) {  // 01:00 ~ 10:30，间隔5~10秒
					sleepTime1 = RandomUtil.getRandom(3000, 5000);
					sleepTime2 = RandomUtil.getRandom(2000, 5000);
				} else { // 其他时间段，间隔3~8秒
					sleepTime1 = RandomUtil.getRandom(2000, 5000);
					sleepTime2 = RandomUtil.getRandom(1000, 3000);
				}
			} else if(way > 20 && way <= 30) { // 一般，
				if(flag) {  // 01:00 ~ 10:30，间隔4~8秒
					sleepTime1 = RandomUtil.getRandom(2000, 5000);
					sleepTime2 = RandomUtil.getRandom(2000, 3000);
				} else { // 其他时间段，间隔3~6秒
					sleepTime1 = RandomUtil.getRandom(1000, 3000);
					sleepTime2 = RandomUtil.getRandom(2000, 3000);
				}
			} else if(way > 30 && way <= 40) { // 人多，
				if(flag) {  // 01:00 ~ 10:30，间隔3~5秒
					sleepTime1 = RandomUtil.getRandom(2000, 3000);
					sleepTime2 = RandomUtil.getRandom(1000, 2000);
				} else { // 其他时间段，间隔2~4秒
					sleepTime1 = RandomUtil.getRandom(1000, 2000);
					sleepTime2 = RandomUtil.getRandom(1000, 2000);
				}
			} else { // 人很多，
				if(flag) { // 01:00 ~ 10:30，间隔2.5~5秒
					sleepTime1 = RandomUtil.getRandom(1500, 3000);
					sleepTime2 = RandomUtil.getRandom(1000, 2000);
				} else {  // 其他时间段，间隔1.3~3秒
					isInroom = false;
					sleepTime1 = RandomUtil.getRandom(800, 2000);
					sleepTime2 = RandomUtil.getRandom(300, 1000);
				}
			}
			if(isInroom) {
				Thread.sleep(sleepTime1);
				socket = SocketUtil.inRoom(roomId, userId);
			} else {
				JmService.inRoom(roomId, userId);
			}
			Thread.sleep(sleepTime2);
			JmService.pluck(roomId, userId, session, ip);
			if(socket != null) {
				socket.close();
			}
			if(!isInroom) {
				JmService.outRoom(roomId, userId);
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

}
