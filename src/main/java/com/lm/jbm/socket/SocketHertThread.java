package com.lm.jbm.socket;


import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.lm.jbm.service.JmService;
import com.lm.jbm.thread.PeachThread;
import com.lm.jbm.thread.ThreadManager;
import com.lm.jbm.utils.JsonUtil;


public class SocketHertThread implements Runnable {
	public static long SEQID;
	public static long getSeqId() {
		SEQID++;
		return SEQID;
	}
	
	public SocketHertThread() {
	}

	public void run() {
		try {
			while(true) {
				JSONObject data = new JSONObject();
				while(true) {
					long seqId = getSeqId();
					data.put("seqID", seqId);
					data.put("funID", 11004);
					try {
						Thread.sleep(10000);
						SocketUtil.sendToImForHeartbeat(data.toString());
					} catch (Exception e) {
						System.out.println("发送心跳异常：" + e.getMessage());
						break;
					}
				}
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}

}
