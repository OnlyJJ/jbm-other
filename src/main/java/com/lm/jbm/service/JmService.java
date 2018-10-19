package com.lm.jbm.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lm.jbm.thread.SendGiftByGoldThread;
import com.lm.jbm.thread.ThreadManager;
import com.lm.jbm.utils.DateUtil;
import com.lm.jbm.utils.HttpUtils;
import com.lm.jbm.utils.JsonUtil;
import com.lm.jbm.utils.PropertiesUtil;
import com.lm.jbm.utils.RandomUtil;


public class JmService {
	public static ConcurrentHashMap<String, String> serssionMap = new ConcurrentHashMap<String, String>(512);
	public static ConcurrentHashMap<String, Object> userMap = new ConcurrentHashMap<String, Object>(512);
	
	public static final String U1 = PropertiesUtil.getValue("U1");
	public static final String U7 = PropertiesUtil.getValue("U7");
	public static final String U8 = PropertiesUtil.getValue("U8");
	public static final String U9 = PropertiesUtil.getValue("U9");
	public static final String U15 = PropertiesUtil.getValue("U15");
	public static final String U16 = PropertiesUtil.getValue("U16");
	public static final String U19 = PropertiesUtil.getValue("U19");
	public static final String U24 = PropertiesUtil.getValue("U24");
	public static final String U66 = PropertiesUtil.getValue("U66");
	public static final String U84 = PropertiesUtil.getValue("U84");

	public static String login(String userId, String pwd, String ip) {
		try {
			JSONObject json = new JSONObject();
			JSONObject userbaseinfo = new JSONObject();
			userbaseinfo.put("a", userId);
			userbaseinfo.put("b", pwd);
			userbaseinfo.put("j", ip);
			json.put("userbaseinfo", userbaseinfo);
			String str = json.toString();
			String strRes = HttpUtils.post(U1, str);
			JSONObject res = JsonUtil.strToJsonObject(strRes);
			String sessionId = null;
			if (res != null) {
				JSONObject session = JsonUtil.strToJsonObject(res
						.getString("session"));
				if (session != null && session.containsKey("b")) {
					sessionId = session.get("b").toString();
				}
			}
			return sessionId;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	public static int findOnline(String roomId, String anchorId, String[] configs) throws Exception{
		try {
			JSONObject json = new JSONObject();
			JSONObject roomonlineinfo = new JSONObject();
			roomonlineinfo.put("b", roomId);
			JSONObject page = new JSONObject();
			page.put("b", "1");
			page.put("c", "50");
			json.put("roomonlineinfo", roomonlineinfo);
			json.put("page", page);
			String str = json.toString();
			String res = HttpUtils.post(U15, str);
			int real = 0;
			int total = 0;
			if(StringUtils.isNotEmpty(res)) {
				JSONObject data = JsonUtil.strToJsonObject(res);
				if(data != null) {
					if(data.containsKey("page")) {
						JSONObject pageData = JsonUtil.strToJsonObject(data.getString("page"));
						total = Integer.parseInt(pageData.getString("a"));
					}
					if(total >0) {
						if(data.containsKey("onlineuserinfo")) {
							List<String> userids = null;
							if(configs != null && configs.length >0) {
								userids = Arrays.asList(configs);
							}
							List<String> watchIds = null;
							String ids = PropertiesUtil.getValue("watchIds");
							if(StringUtils.isNotEmpty(ids)) {
								String[] wids = ids.split(",");
								watchIds = Arrays.asList(wids);
							}
							JSONArray array = JsonUtil.strToJSONArray(data.getString("onlineuserinfo"));
							if(array != null && array.size() >0) {
								int size = array.size();
								for(int i=0; i<size; i++) {
									JSONObject obj = array.getJSONObject(i);
									String userId = obj.getString("a");
									if(userId.indexOf("robot") != -1) {
										continue;
									}
									if(anchorId.equals(userId)) {
										continue;
									}
									if(userids != null && userids.contains(userId)) {
										continue;
									}
									if(watchIds != null && watchIds.contains(userId)) {
										continue;
									}
									real++;
								}
							}
						}
					}
				}
			}
			System.err.println("房间：" + roomId + "，当前真实用户+游客数：" + real);
			return real;
		} catch(Exception e) {
			System.err.println(e.getMessage());
			throw new Exception("查询房间信息错误，退出当前循环!");
		}
	}

	public static String inRoom(String roomId, String userId) {
		try {
			JSONObject json = new JSONObject();
			JSONObject roomonlineinfo = new JSONObject();
			roomonlineinfo.put("a", 1);
			roomonlineinfo.put("b", roomId);
			JSONObject onlineUserInfo = new JSONObject();
			onlineUserInfo.put("a", userId);
			roomonlineinfo.put("c", onlineUserInfo);
			json.put("roomonlineinfo", roomonlineinfo);
			String str = json.toString();
			return HttpUtils.post(U16, str);
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	public static String outRoom(String roomId, String userId) {
		try {
			JSONObject json = new JSONObject();
			JSONObject roomonlineinfo = new JSONObject();
			roomonlineinfo.put("a", 2);
			roomonlineinfo.put("b", roomId);
			JSONObject onlineUserInfo = new JSONObject();
			onlineUserInfo.put("a", userId);
			roomonlineinfo.put("c", onlineUserInfo);
			json.put("roomonlineinfo", roomonlineinfo);
			String str = json.toString();
			return HttpUtils.post(U16, str);
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	
	
	
	public static boolean checkFreeTime() {
		try {
			Date now = new Date();
			String str1 = DateUtil.format2Str(now, "yyyy-MM-dd") + " 23:00:00";
			String str2 = DateUtil.format2Str(now, "yyyy-MM-dd") + " 23:55:00";
			Date d = DateUtil.parse(str1, "yyyy-MM-dd HH:mm:ss");
			Date d2 = DateUtil.parse(str2, "yyyy-MM-dd HH:mm:ss");
			if(now.after(d) && now.before(d2)) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	public static void sendGift(String[] userIds) throws Exception {
		if(userIds == null || userIds.length <=0) {
			return;
		}
//		if(!checkFreeTime()) {
//			System.err.println("不在送礼时间范围内，不处理！");
//			return;
//		}
		int day = DateUtil.getDay();
		if(day % 2 == 0) { // 双号送背包
			sendGiftPack(userIds);
		} else { // 单号送金币
			SendGiftByGoldThread t = new SendGiftByGoldThread();
			ThreadManager.getInstance().execute(t);
		}
	}
	
	public static void sendGiftPack(String[] userIds) throws Exception {
		String roomId = PropertiesUtil.getValue("gift_roomId");
		String anchorId = PropertiesUtil.getValue("gift_room_userId");
		boolean boxgift = PropertiesUtil.getValue("boxgift").equals("0") ? true : false;
		for(String userId : userIds) {
			String ip = RandomUtil.getUserIp(userId);
			String sessionId = serssionMap.get(userId);
			if(StringUtils.isEmpty(sessionId)) {
				sessionId = login(userId, RandomUtil.getPwd(), ip);
				if(StringUtils.isNotEmpty(sessionId)) {
					serssionMap.put(userId, sessionId);
				}
			}
			JSONObject json = new JSONObject();
			JSONObject session = new JSONObject();
			session.put("b", sessionId);
			
			JSONObject userbaseinfo = new JSONObject();
			userbaseinfo.put("a", userId);
			userbaseinfo.put("j", ip);
			
			json.put("session", session);
			json.put("userbaseinfo", userbaseinfo);
			
			// 加入房间
			inRoom(roomId, userId);
			// 关注主播
			attention(json, ip, roomId, anchorId, userId);
			// 免费礼物
			sendFree(ip, roomId, anchorId, userId, sessionId);
			// 获取背包
			String bag = HttpUtils.post3(U84, json.toString(), ip); 
			
			Map<String, Object> gifts = null;
			if(StringUtils.isNotEmpty(bag)) {
				JSONObject bagData = JsonUtil.strToJsonObject(bag);
				if(bagData != null && bagData.containsKey("list")) {
					gifts = new HashMap<String, Object>();
					JSONArray array = JsonUtil.strToJSONArray(bagData.getString("list"));
					if(array!= null && array.size() >0) {
						int size = array.size();
						for(int i=0; i<size; i++ ) {
							JSONObject giftVo = array.getJSONObject(i);
							int giftId = giftVo.getIntValue("a");
							if(boxgift) {
								if(giftId == 294) {
									continue;
								}
							}
							int number = giftVo.getIntValue("b");
							gifts.put(String.valueOf(giftId), number);
						}
					}
				}
			}
			// 送礼
			if(gifts != null && gifts.size() >0) {
				JSONObject anchorinfo = new JSONObject();
				anchorinfo.put("a", anchorId);
				anchorinfo.put("b", roomId);
				int sleep = 1;
				for(String key : gifts.keySet()) {
					int number = (Integer) gifts.get(key);
					int giftId = Integer.parseInt(key);
					while(true) {
						if(number <= 0) {
							break;
						}
						// 判断下成员
						try {
							int online = findOnline(roomId, anchorId, userIds);
							if(online > 0) {
								break;
							}
						} catch(Exception e) {
							System.err.println(e.getMessage());
							break;
						}
						sleep++;
						if(sleep % 20 == 0) {
							Thread.sleep(10000);
						} else {
							Thread.sleep(1000);
						}
						int num = 10;
						int ran = RandomUtil.getRandom(0, 10);
						if(ran % 2 == 0 && number > 100) {
							num = 100;
						}
						if(number <= 10) {
							num = 1;
						}
						number -= num;
						
						JSONObject gift = new JSONObject();
						gift.put("a", giftId);
						gift.put("b", num);
						gift.put("u", 1);
						gift.put("v", "false");
						
						json.put("anchorinfo", anchorinfo);
						json.put("gift", gift);
						
						String res = HttpUtils.post3(U9, json.toString(), ip);
						if(StringUtils.isNotEmpty(res)) {
							JSONObject data = JsonUtil.strToJsonObject(res);
							if(data != null && data.containsKey("result")) {
								JSONObject ret = JsonUtil.strToJsonObject(data.getString("result"));
								String msg = ret.getString("b");
								if(!msg.toLowerCase().equals("success")) {
									break;
								}
								System.err.println("送礼成功!");
							}
						}
					}
				}
			} 
			// 退出房间
			outRoom(roomId, userId);
		}
	}
	
	
	
	public static void sendGiftByGold(String[] userIds) throws Exception {
		if(userIds == null || userIds.length <=0) {
			return;
		}
//		if(!checkFreeTime()) {
//			System.err.println("不在送礼时间范围内，不处理！");
//			return;
//		}
		String roomId = PropertiesUtil.getValue("gift_roomId");
		String anchorId = PropertiesUtil.getValue("gift_room_userId");
		for(String userId : userIds) {
			String ip = RandomUtil.getUserIp(userId);
			String sessionId = serssionMap.get(userId);
			if(StringUtils.isEmpty(sessionId)) {
				sessionId = login(userId, RandomUtil.getPwd(), ip);
				if(StringUtils.isNotEmpty(sessionId)) {
					serssionMap.put(userId, sessionId);
				}
			}
			JSONObject json = new JSONObject();
			JSONObject session = new JSONObject();
			session.put("b", sessionId);
			
			JSONObject userbaseinfo = new JSONObject();
			userbaseinfo.put("a", userId);
			userbaseinfo.put("j", ip);
			
			json.put("session", session);
			json.put("userbaseinfo", userbaseinfo);
			
			// 加入房间
			inRoom(roomId, userId);
			Thread.sleep(2000);
			// 关注主播
			attention(json, ip, roomId, anchorId, userId);
			Thread.sleep(2000);
			// 免费礼物
			sendFree(ip, roomId, anchorId, userId, sessionId);
			Thread.sleep(2000);
			// 获取金币
			String bag = HttpUtils.post3(U66, json.toString(), ip); 
			
			if(StringUtils.isNotEmpty(bag)) {
				JSONObject data = JsonUtil.strToJsonObject(bag);
				if(data != null && data.containsKey("anchorinfo")) {
					JSONObject account = JsonUtil.strToJsonObject(data.getString("anchorinfo"));
					if(account!= null) {
						int gold = account.getIntValue("t");
						// 送礼
						JSONObject anchorinfo = new JSONObject();
						anchorinfo.put("a", anchorId);
						anchorinfo.put("b", roomId);
						int sleep = 1;
						while(true) {
							if(gold < 100) {
								break;
							}
							// 判断下成员
							try {
								int online = findOnline(roomId, anchorId, userIds);
								if(online > 0) {
									break;
								}
							} catch(Exception e) {
								System.err.println(e.getMessage());
								break;
							}
							sleep++;
							if(sleep % 20 == 0) {
								Thread.sleep(10000);
							} else {
								Thread.sleep(1000);
							}
							int giftId = getGiftRandom();
							int singleGold = 100;
							if(giftId == 143) {
								singleGold = 500;
							}
							int num = 10;
							int ran = RandomUtil.getRandom(0, 10);
							if(ran % 2 == 0 && gold >= 100 * singleGold) {
								num = 100;
							}
							if(gold >= 10 * singleGold && gold < 100 * singleGold) {
								num = 10;
							} else if(gold >= 5 * singleGold && gold < 10 * singleGold) {
								num = 5;
							} else if(gold <5 * singleGold) {
								num = 1;
							}
							gold -= num*singleGold;
							
							JSONObject gift = new JSONObject();
							gift.put("a", giftId);
							gift.put("b", num);
							gift.put("u", 0);
							gift.put("v", "true");
							
							json.put("anchorinfo", anchorinfo);
							json.put("gift", gift);
							
							String res = HttpUtils.post3(U9, json.toString(), ip);
							if(StringUtils.isNotEmpty(res)) {
								JSONObject retData = JsonUtil.strToJsonObject(res);
								if(retData != null && retData.containsKey("result")) {
									JSONObject ret = JsonUtil.strToJsonObject(retData.getString("result"));
									String msg = ret.getString("b");
									if(!msg.toLowerCase().equals("success")) {
										break;
									}
									System.err.println("送礼成功!");
								}
							}
						}
					}
				}
			}
			// 退出房间
			outRoom(roomId, userId);
		}
	}
	
	public static int getGiftRandom() {
		int[] giftIds = {138,140,143,138,140,143,138,140,143,138,140,143};
		return giftIds[new Random().nextInt(giftIds.length)];
	}
	
	/**
	 * 关注主播
	 * @author Shao.x
	 * @date 2018年10月11日
	 * @param json
	 * @param ip
	 * @param roomId
	 * @param userId
	 */
	private static void attention(JSONObject json, String ip, String roomId, String anchorId, String userId) {
		// 获取关注列表
		try {
			JSONObject aten = json;
			JSONObject page = new JSONObject();
			page.put("b", "1");
			page.put("c", "2000");
			aten.put("page", page);
			List<String> attenList = new ArrayList<String>();
			String attention = HttpUtils.post3(U7, aten.toString(), ip); 
			if(StringUtils.isNotEmpty(attention)) {
				JSONObject attenData = JsonUtil.strToJsonObject(attention);
				if(attenData != null && attenData.containsKey("data")) {
					JSONArray array = JsonUtil.strToJSONArray(attenData.getString("data"));
					if(array!= null && array.size() >0) {
						int size = array.size();
						for(int i=0; i<size; i++ ) {
							JSONObject vo = array.getJSONObject(i);
							String uid = vo.getString("b");
							if(StringUtils.isNotEmpty(uid)) {
								attenList.add(uid);
							}
						}
					}
				}
			}
			if(!attenList.contains(roomId)) {
				System.err.println("未关注主播，先关注：userId：" + userId);
				JSONObject at = json;
				JSONObject anchorinfo = new JSONObject();
				anchorinfo.put("a", anchorId);
				anchorinfo.put("j", 1);
				at.put("anchorinfo", anchorinfo);
				HttpUtils.post3(U8, at.toString(), ip); 
			}
		} catch(Exception e) {
			
		}
	}
	
	
	
	private static void sendFree(String ip, String roomId, String anchorId, String userId, String sessionId) {
		// 获取关注列表
		try {
			JSONObject json = new JSONObject();
			JSONObject anchorinfo = new JSONObject();
			anchorinfo.put("a", anchorId);
			
			JSONObject userbaseinfo = new JSONObject();
			userbaseinfo.put("a", userId);
			
			JSONObject session = new JSONObject();
			session.put("b", sessionId);
			
			json.put("anchorinfo", anchorinfo);
			json.put("userbaseinfo", userbaseinfo);
			json.put("session", anchorinfo);
			
			String res = HttpUtils.post3(U19, json.toString(), ip); 
			if(StringUtils.isNotEmpty(res)) {
				JSONObject data = JsonUtil.strToJsonObject(res);
				if(data != null && data.containsKey("onlineuserinfo")) {
					JSONObject vo = JsonUtil.strToJsonObject(data.getString("onlineuserinfo"));
					if(vo!= null) {
						int num = vo.getIntValue("i");
						if(num >0) {
							JSONObject anchorinfo1 = new JSONObject();
							anchorinfo1.put("b", roomId);
							JSONObject freegiftupdateinfo = new JSONObject();
							freegiftupdateinfo.put("a", "1");
							freegiftupdateinfo.put("c", anchorId);
							
							JSONObject param = new JSONObject();
							param.put("anchorinfo", anchorinfo1);
							param.put("freegiftupdateinfo", freegiftupdateinfo);
							param.put("userbaseinfo", userbaseinfo);
							param.put("session", session);
							
							for(int i=0;i<num;i++) {
								System.err.println("免费茄子送一波：userId：" + userId);
								HttpUtils.post3(U24, param.toString(), ip); 
								Thread.sleep(1000);
							}
						}
					}
				}
			}
		} catch(Exception e) {
			System.err.println("送茄子失败：" + userId);
		}
	}
}
