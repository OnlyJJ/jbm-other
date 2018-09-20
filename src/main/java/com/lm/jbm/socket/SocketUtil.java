package com.lm.jbm.socket;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.lm.jbm.service.JmService;
import com.lm.jbm.thread.LoginThread;
import com.lm.jbm.thread.ThreadManager;
import com.lm.jbm.utils.ByteUtil;
import com.lm.jbm.utils.GZipUtil;
import com.lm.jbm.utils.JsonUtil;
import com.lm.jbm.utils.RandomUtil;


public class SocketUtil {
	
	public static boolean rebuildFlag = false;
	public static int RECOUNT = 0;
	
	private SocketUtil() {}
	
	public static void inroomIm(String msg, Socket socket) throws Exception{
		DataOutputStream os = null;
		try {
			if(StringUtils.isNotEmpty(msg)) {
				msg=msg.replaceAll("\n|\r|\t|\b|\f", "");
				byte[] body = GZipUtil.compressToByte(msg);
				byte[] head = ByteUtil.toByteArray(body.length, 4);
				byte[] data = new byte[body.length+head.length];

				System.arraycopy(head, 0, data, 0, head.length);
				System.arraycopy(body, 0, data, head.length, body.length);
				os = new DataOutputStream(socket.getOutputStream());
				os.write(data);
				os.flush();
			}
		} catch(Exception e) {
			if(os != null) {
				os.close();
			}
			throw e;
		}
	}
	
	
	public static void sendToIm(String msg) throws Exception{
		DataOutputStream os = null;
		try {
			if(StringUtils.isNotEmpty(msg)) {
				msg=msg.replaceAll("\n|\r|\t|\b|\f", "");
				byte[] body = GZipUtil.compressToByte(msg);
				byte[] head = ByteUtil.toByteArray(body.length, 4);
				byte[] data = new byte[body.length+head.length];

				System.arraycopy(head, 0, data, 0, head.length);
				System.arraycopy(body, 0, data, head.length, body.length);
				os = new DataOutputStream(SocketClient.getInstance().getOutputStream());
				os.write(data);
				os.flush();
			}
		} catch(Exception e) {
			if(os != null) {
				os.close();
			}
			throw e;
		}
	}
	
	public static void sendToImForHeartbeat(String msg)  throws Exception{
		DataOutputStream os = null;
		try {
			if(StringUtils.isNotEmpty(msg)) {
				msg=msg.replaceAll("\n|\r|\t|\b|\f", "");
				byte[] body = GZipUtil.compressToByte(msg);
				byte[] head = ByteUtil.toByteArray(body.length, 4);
				byte[] data = new byte[body.length+head.length];

				System.arraycopy(head, 0, data, 0, head.length);
				System.arraycopy(body, 0, data, head.length, body.length);
				os = new DataOutputStream(SocketClient.getInstance().getOutputStream());
				os.write(data);
				os.flush();
			}
		} catch(Exception e) {
			if(os != null) {
				os.close();
			}
			throw e;
		}
	}
	
	public static String recieve() throws Exception {
		DataInputStream is = null;
		try {
			is = new DataInputStream(SocketClient.getInstance().getInputStream());
			String msg = getDataBody(is); 
			return msg;
		} catch(Exception e) {
			Thread.sleep(100);
			if(SocketClient.getInstance() != null) {
				SocketClient.getInstance().close();
			}
			throw e;
		}
	}
	
	public static String recieve(Socket socket) throws Exception {
		DataInputStream is = null;
		try {
			is = new DataInputStream(socket.getInputStream());
			String msg = getDataBody(is); 
			return msg;
		} catch(Exception e) {
			throw e;
		}
	}
	

	public static String getDataBody(InputStream is) throws IOException {
		String dataBody = null;
		byte[] head = getData(is, 4);
		int dataLength = ByteUtil.toInt(head);
		byte[] data = getData(is, dataLength);
		dataBody = GZipUtil.uncompressToString(data, "utf-8");
		return dataBody;
	}
	
	private static byte[] getData(InputStream is, int length) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[5120];
		int nIdx = 0; //累计读取了多少位
		int nReadLen = 0; //一次读取了多少位
		while (nIdx < length) { //循环读取足够长度的数据
			if(length - nIdx >= buffer.length){ //剩余数据大于缓存，则全部读取
				nReadLen = is.read(buffer);
			}else{ //剩余数据小于缓存，则注意拆分其他包，只取当前包剩余数据
				nReadLen = is.read(buffer, 0, length - nIdx);
			}
			if (nReadLen > 0) {
				baos.write(buffer, 0, nReadLen);
				nIdx = nIdx + nReadLen;
			} else {
				break;
			}
		}
		return baos.toByteArray();
	}
	
	public static void close()  throws Exception{
		 try {
			 if(SocketClient.getInstance()!=null && SocketClient.getInstance().isConnected()){
				 SocketClient.getInstance().close();
			 }
		} catch (IOException e) {
			throw e;
		}
	}
	
	
	public static void msgListen() throws Exception {
		String userId = RandomUtil.getListener();
		String roomId = RandomUtil.getRoomId();
		String pwd = RandomUtil.getPwd();
		String sessionId = JmService.login(userId, pwd, RandomUtil.getIp());
		StringBuffer authenticationSbf = new StringBuffer();
		authenticationSbf.append("").append("{\"funID\":11000,\"length\":100,\"data\":{\"sessionid\":\"").append(sessionId).append("\",\"uid\":\"").append(userId).append("\"}}");
		String imAuthenticationReqStr = authenticationSbf.toString();
		sendToIm(imAuthenticationReqStr);
		Thread.sleep(1000);
		String imAuthenticationResponseStr = recieve();
		JSONObject json = JsonUtil.strToJsonObject(imAuthenticationResponseStr);
		JSONObject data = json.getJSONObject("data");
		String token = data.getString("token");
		StringBuffer inRoomSbf = new StringBuffer();
		inRoomSbf.append("").append("{\"funID\":11006,\"length\":102,\"data\":{\"token\":\""+token+"\",\"roomId\":\""+roomId+"\"}}");
		String inRoomReqStr = inRoomSbf.toString();
		sendToIm(inRoomReqStr);
		System.err.println(userId + "进入房间:" + roomId + ",开始监听。。。");
	}
	
	
	public static Socket inRoom(String roomId, String userId) throws Exception {
		String sessionId = LoginThread.serssionMap.get(userId);
		if(StringUtils.isEmpty(sessionId)) {
			sessionId = JmService.login(userId, RandomUtil.getPwd(), RandomUtil.getIp());
		}
		StringBuffer authenticationSbf = new StringBuffer();
		authenticationSbf.append("").append("{\"funID\":11000,\"length\":100,\"data\":{\"sessionid\":\"").append(sessionId).append("\",\"uid\":\"").append(userId).append("\"}}");
		String imAuthenticationReqStr = authenticationSbf.toString();
		Socket socket = SocketClient.creat();
		inroomIm(imAuthenticationReqStr, socket);
		Thread.sleep(RandomUtil.getRandom(100, 500));
		String imAuthenticationResponseStr = recieve(socket);
		JSONObject json = JsonUtil.strToJsonObject(imAuthenticationResponseStr);
		JSONObject data = json.getJSONObject("data");
		String token = data.getString("token");
		StringBuffer inRoomSbf = new StringBuffer();
		inRoomSbf.append("").append("{\"funID\":11006,\"length\":102,\"data\":{\"token\":\""+token+"\",\"roomId\":\""+roomId+"\"}}");
		String inRoomReqStr = inRoomSbf.toString();
		inroomIm(inRoomReqStr,socket);
		System.err.println("加入房间：" + userId);
		JmService.inRoom(roomId, userId);
		return socket;
	}
}
