package com.lm.jbm.thread;



import com.lm.jbm.service.JmService;
import com.lm.jbm.utils.RandomUtil;




public class SendGiftThread implements Runnable {

	public SendGiftThread() {
	}

	public void run() {
		try {
			String[] userIds = RandomUtil.getUserIds();
			JmService.sendGift(userIds);
		} catch (Exception e) {
			System.err.println("批量送礼异常，退出!");
			Thread.interrupted();
		}
	}
}
