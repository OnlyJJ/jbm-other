package com.lm.jbm.service;


import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lm.jbm.thread.GrapBoxThread;
import com.lm.jbm.thread.GrapRebThread;
import com.lm.jbm.thread.LoginThread;
import com.lm.jbm.thread.PeachThread;
import com.lm.jbm.thread.ThreadManager;
import com.lm.jbm.utils.DateUtil;
import com.lm.jbm.utils.HttpUtils;
import com.lm.jbm.utils.JsonUtil;
import com.lm.jbm.utils.PropertiesUtil;
import com.lm.jbm.utils.RandomUtil;


public class JmService {
	
	public static final String U1 = PropertiesUtil.getValue("U1");
	public static final String U9 = PropertiesUtil.getValue("U9");
	public static final String U15 = PropertiesUtil.getValue("U15");
	public static final String U16 = PropertiesUtil.getValue("U16");
	public static final String U32 = PropertiesUtil.getValue("U32");
	public static final String G48 = PropertiesUtil.getValue("G48");
	public static final String U53 = PropertiesUtil.getValue("U53");
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
	
	public static int findOnline(String roomId) {
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
							JSONArray array = JsonUtil.strToJSONArray(data.getString("onlineuserinfo"));
							if(array != null && array.size() >0) {
								int size = array.size();
								for(int i=0; i<size; i++) {
									JSONObject obj = array.getJSONObject(i);
									String userId = obj.getString("a");
									if(userId.indexOf("robot") != -1 || userId.indexOf("pesudo") != -1) {
										break;
									}
									real++;
								}
							}
						}
					}
				}
			}
			System.err.println("房间：" + roomId + "，当前真实用户数：" + real);
			return real;
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
		return 0;
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
	
	public static String pluck(String roomId, String userId, String sessionId, String ip) {
		try {
			JSONObject json = new JSONObject();
			JSONObject session = new JSONObject();
			session.put("b", sessionId);
			
			JSONObject userbaseinfo = new JSONObject();
			userbaseinfo.put("a", userId);
			userbaseinfo.put("j", ip);
			
			JSONObject peachvo = new JSONObject();
			peachvo.put("b", roomId);
			json.put("session", session);
			json.put("userbaseinfo", userbaseinfo);
			json.put("peachvo", peachvo);
			
			String str = json.toString();
			String res = HttpUtils.post3(U53, str, ip);
			if(StringUtils.isNotEmpty(res)) {
				JSONObject data = JsonUtil.strToJsonObject(res);
				if(data != null && data.containsKey("peachvo")) {
					JSONObject peachvoJson = JsonUtil.strToJsonObject(data.getString("peachvo"));
					int num = peachvoJson.getIntValue("l");
					String name = peachvoJson.getString("m");
					StringBuilder msg = new StringBuilder();
					msg.append(userId).append("摘到：").append(name).append("X").append(num).append("个");
					System.err.println(msg.toString());
					return msg.toString();
				}
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
		return "";
	}
	
	public static void peach(String roomId) {
		try {
			String[] userIds = RandomUtil.getUserIds();
			List<String> list = Arrays.asList(userIds);
			Collections.shuffle(list);
			int index = 1;
			int real = findOnline(roomId);
//			int conf = Integer.parseInt(PropertiesUtil.getValue("real_count"));
//			if(real < conf) {
//				Thread.sleep(1000);
//			}
			for(int i=0; i<userIds.length; i++) {
				if(real >= 50) {
					if(index > 6) {
						return;
					}
				} else {
					if(index > RandomUtil.getTotal()) {
						return;
					}
				}
				String userId = list.get(i);
				PeachThread peach = new PeachThread(roomId, userId, real);
				ThreadManager.getInstance().execute(peach);
				index++;
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static void grapReb(String userId, String sessionId, String rebId, String ip) {
		try {
			JSONObject json = new JSONObject();
			JSONObject session = new JSONObject();
			session.put("b", sessionId);
			
			JSONObject userbaseinfo = new JSONObject();
			userbaseinfo.put("a", userId);
			userbaseinfo.put("j", ip);
			
			JSONObject redpacketsendvo = new JSONObject();
			redpacketsendvo.put("a", rebId);
			
			json.put("session", session);
			json.put("userbaseinfo", userbaseinfo);
			json.put("redpacketsendvo", redpacketsendvo);
			
			String str = json.toString();
			String res = HttpUtils.post3(U32, str, ip);
			if(StringUtils.isNotEmpty(res)) {
				JSONObject data = JsonUtil.strToJsonObject(res);
				if(data != null && data.containsKey("redpacketreceivevo")) {
					JSONObject peachvoJson = JsonUtil.strToJsonObject(data.getString("redpacketreceivevo"));
					int gold = peachvoJson.getIntValue("d");
					StringBuilder msg = new StringBuilder();
					msg.append(userId).append("抢红包，抢到：").append("X").append(gold).append("个金币");
					System.err.println(msg.toString());
				}
			}
		} catch(Exception e) {
			System.err.println("抢红包异常！");
		}
	}
	
	public static void grapReb(String rebId) {
		try {
			String[] userIds = RandomUtil.getUserIds();
			List<String> list = Arrays.asList(userIds);
			Collections.shuffle(list);
			int index = 1;
			for(int i=0; i<userIds.length; i++) {
				if(index > 2) {
					return;
				}
				String userId = list.get(i);
				GrapRebThread peach = new GrapRebThread(rebId, userId);
				ThreadManager.getInstance().execute(peach);
				index++;
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static void grapBox(String roomId) {
		try {
			String[] userIds = RandomUtil.getUserIds();
			List<String> list = Arrays.asList(userIds);
			Collections.shuffle(list);
			int index = 1;
			int way = 1;
			int real = findOnline(roomId);
			int conf = Integer.parseInt(PropertiesUtil.getValue("real_count"));
			if(real < conf) {
				Thread.sleep(1000);
				way = 0;
			}
			for(int i=0; i<userIds.length; i++) {
				if(index > 20) {
					return;
				}
				String userId = list.get(i);
				GrapBoxThread box = new GrapBoxThread(roomId, userId, way);
				ThreadManager.getInstance().execute(box);
				index++;
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static void grapBox(String roomId, String sessionId, String userId, String ip) {
		try {
			if(StringUtils.isEmpty(sessionId)) {
				sessionId = login(userId, RandomUtil.getPwd(), ip);
			}
			JSONObject json = new JSONObject();
			JSONObject session = new JSONObject();
			session.put("b", sessionId);
			
			JSONObject userbaseinfo = new JSONObject();
			userbaseinfo.put("a", userId);
			userbaseinfo.put("j", ip);
			
			JSONObject anchorinfo = new JSONObject();
			anchorinfo.put("b", roomId);
			
			json.put("session", session);
			json.put("userbaseinfo", userbaseinfo);
			json.put("anchorinfo", anchorinfo);
			
			JSONObject grabboxvo = new JSONObject();
			grabboxvo.put("b", sessionId);
			json.put("grabboxvo", grabboxvo);
			String str = json.toString();
			String res = HttpUtils.post3(G48, str, ip);
			if(StringUtils.isNotEmpty(res)) {
				JSONObject data = JsonUtil.strToJsonObject(res);
				if(data != null && data.containsKey("grabboxvo")) {
					JSONObject ret = JsonUtil.strToJsonObject(data.getString("grabboxvo"));
					int num = ret.getIntValue("h");
					String name = ret.getString("f");
					StringBuilder msg = new StringBuilder();
					msg.append(userId).append("抢宝箱，抢到：").append(name).append("X").append(num).append("个");
					System.err.println(msg.toString());
				}
			}
		} catch(Exception e) {
			System.err.println("抢宝箱异常！");
		}
	}
	
	public static boolean checkFreeTime() {
		try {
			Date now = new Date();
			String str1 = DateUtil.format2Str(now, "yyyy-MM-dd") + " 01:00:00";
			String str2 = DateUtil.format2Str(now, "yyyy-MM-dd") + " 10:30:00";
			Date d = DateUtil.parse(str1, "yyyy-MM-dd HH:mm:ss");
			Date d2 = DateUtil.parse(str2, "yyyy-MM-dd HH:mm:ss");
			if(now.after(d) && now.before(d2)) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	public static void sendGift(String userId) {
		try {
			String ip = RandomUtil.getUserIp(userId);
			String sessionId = LoginThread.serssionMap.get(userId);
			if(StringUtils.isEmpty(sessionId)) {
				sessionId = login(userId, RandomUtil.getPwd(), ip);
			}
			JSONObject json = new JSONObject();
			JSONObject session = new JSONObject();
			session.put("b", sessionId);
			
			JSONObject userbaseinfo = new JSONObject();
			userbaseinfo.put("a", userId);
			userbaseinfo.put("j", ip);
			
			json.put("session", session);
			json.put("userbaseinfo", userbaseinfo);
			
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
							int number = giftVo.getIntValue("b");
							gifts.put(String.valueOf(giftId), number);
						}
					}
				}
			}
			// 送礼
			if(gifts != null && gifts.size() >0) {
				String roomId = PropertiesUtil.getValue("gift_roomId");
				String anchorId = PropertiesUtil.getValue("gift_room_userId");
				JSONObject anchorinfo = new JSONObject();
				anchorinfo.put("a", anchorId);
				anchorinfo.put("b", roomId);
				for(String key : gifts.keySet()) {
					int number = (Integer) gifts.get(key);
					int giftId = Integer.parseInt(key);
					
					for(int i=0;i<100;i++) {
						if(number <= 0) {
							continue;
						}
						int num = 5;
						int ran = RandomUtil.getRandom(0, 10);
						if(ran % 2 == 0) {
							num = 10;
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
								System.err.println(msg);
								if(msg.toLowerCase().equals("success")) {
									break;
								}
							}
						}
						Thread.sleep(5000);
					}
				}
			}
		} catch(Exception e) {
			System.err.println("送礼异常！");
		}
	}
}
