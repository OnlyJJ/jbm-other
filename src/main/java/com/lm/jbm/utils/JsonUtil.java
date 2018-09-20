package com.lm.jbm.utils;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class JsonUtil {
	
	/**
	 * json字符串转为json对象
	 * @param jsonString
	 * @return
	 */
	public static JSONObject strToJsonObject(String jsonString){
		JSONObject json = null;
 		try{
			if(StringUtils.isNotEmpty(jsonString)){
				json = JSONObject.parseObject(jsonString);
			}
		}catch(Exception e){
		}
		
		return json;
	}
	
	/**
	 * json对象转为Java bean对象
	 * @param json
	 * @return
	 */
//	public static Object jsonToBean(JSONObject json){
//		Object obj = null;
//		try{
//			if(null != json){
//				obj = JSON.
//			}
//		}catch(Exception e){
//		}
//		
//		return obj;
//	}
	
	/**
	 * Java bean对象转为json字符�?
	 * @param obj
	 * @return
	 */
	public static String beanToJsonString(Object obj){
		String json = null;
		try{
			if(null != obj){
				json = JSON.toJSONString(obj);
			}
		}catch(Exception e){
			try {
				json = JSONArray.toJSONString(obj);
			} catch (Exception e2) {
			}
		}
		
		return json;
	}
	
	/**
	 * Java bean对象转为json字符�?
	 * @param obj
	 * @return
	 */
	public static String arrayToJsonString(Object obj){
		String json = null;
		try{
			if(null != obj){
				json = JSONArray.toJSONString(obj);
			}
		}catch(Exception e){
		}
		
		return json;
	}
	
	
	/**
	 * json字符串转为JSONArray
	 * @param jsonString
	 * @return
	 */
	public static JSONArray strToJSONArray(String jsonString){
		JSONArray jsonArray = null;
 		try{
			if(StringUtils.isNotEmpty(jsonString)){
				jsonArray = JSONArray.parseArray(jsonString);
			}
		}catch(Exception e){
		}
		
		return jsonArray;
	}

}
