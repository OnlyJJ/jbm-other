package com.lm.jbm.thread;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.lm.jbm.service.JmService;
import com.lm.jbm.utils.RandomUtil;




public class LoginThread implements Runnable {
	
	public static ConcurrentHashMap<String, String> serssionMap = new ConcurrentHashMap<String, String>(512);
	
	public static ConcurrentHashMap<String, String> ipMap = new ConcurrentHashMap<String, String>(512);

	public LoginThread() {}

	public void run() {
		while(true) {
			try {
				String[] userIds = RandomUtil.getUserIds();
				if(userIds != null) {
					for(String userId : userIds) {
						String ip = "";
						if(ipMap.contains(userId)) {
							ip = ipMap.get(userId);
						} else {
							ip = RandomUtil.getIp();
							ipMap.put(userId, ip);
						}
						String ret = JmService.login(userId, RandomUtil.getPwd(), ip);
						if(StringUtils.isNotEmpty(ret)) {
							serssionMap.put(userId, ret);
						}
						Thread.sleep(2000);
					}
				}
				Thread.sleep(1000*60*10);
			} catch (Exception e) {
				break;
			}
		}
	}

}
