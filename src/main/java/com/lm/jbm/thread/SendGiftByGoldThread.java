package com.lm.jbm.thread;



import com.lm.jbm.service.JmService;
import com.lm.jbm.utils.RandomUtil;




public class SendGiftByGoldThread implements Runnable {

	public SendGiftByGoldThread() {
	}

	public void run() {
		try {
			String[] userIds = RandomUtil.getGoldUserIds();
			JmService.sendGiftByGold(userIds);
		} catch (Exception e) {
			System.err.println("批量送礼异常，退出!");
			Thread.interrupted();
		}
	}
}
